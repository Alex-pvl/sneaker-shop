package ru.nstu.ap.service.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nstu.ap.model.order.Order;
import ru.nstu.ap.model.order.OrderStatus;
import ru.nstu.ap.repository.catalog.OfferRepository;
import ru.nstu.ap.repository.order.OrderRepository;
import ru.nstu.ap.service.cart.CartService;
import ru.nstu.ap.service.user.UserService;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Service
public class OrderService {
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private OfferRepository offerRepository;
	@Autowired
	private OrderItemService orderItemService;
	@Autowired
	private CartService cartService;
	@Autowired
	private UserService userService;

	@Transactional(readOnly = true)
	public <T> List<T> getOrders(Integer userId, Function<Order, T> mapper) {
		return getAll(userId).stream().map(mapper).toList();
	}

	public List<Order> getAll(Integer userId) {
		return orderRepository.findAll().stream()
			.filter(o -> o.getUserId().equals(userId))
			.toList();
	}

	@Transactional(readOnly = true)
	public <T> T getOrder(Integer userId, Integer id, Function<Order, T> mapper) throws IllegalAccessException {
		return mapper.apply(getById(userId, id));
	}

	public Order getById(Integer userId, Integer id) throws IllegalAccessException {
		var order = orderRepository.findById(id)
			.orElseThrow(IllegalArgumentException::new);
		if (!order.getUserId().equals(userId)) {
			throw new IllegalAccessException("Unable to access");
		}
		return order;
	}

	@Transactional
	public Order create(Integer userId) {
		var order = createEmptyOrder(userId);
		var cart = cartService.getByUserId(userId);
		var items = orderItemService.mapFromCart(order.getId(), cart);
		order.setOrderItems(items);
		order.setCost(items.stream()
			.map(i -> i.getOffer().getPrice() * i.getQuantity())
			.mapToDouble(Double::doubleValue)
			.sum()
		);
		orderRepository.save(order);
		order.getOrderItems().forEach(i -> {
			var offer = i.getOffer();
			offer.decrementQuantity(i.getQuantity());
		});
		cartService.clear(userId);
		cartService.createEmptyCart(userId);
		return order;
	}

	@Transactional
	public void pay(Integer orderId) throws IllegalAccessException {
		var order = orderRepository.findById(orderId)
			.orElseThrow(IllegalArgumentException::new);
		var user = userService.getById(order.getUserId());
		var userBalance = user.getBalance();
		if (userBalance >= order.getCost()) {
			order.setPaid(true);
			order.setStatus(OrderStatus.DONE);
			user.setBalance(userBalance - order.getCost());
			orderRepository.save(order);
		} else throw new IllegalAccessException("Not enough money");
	}

	@Transactional
	public void cancel(Integer orderId) {
		var order = orderRepository.findById(orderId)
			.orElseThrow(IllegalArgumentException::new);
		order.setStatus(OrderStatus.CANCELED);
		order.getOrderItems().forEach(i -> {
			var offer = i.getOffer();
			offer.incrementQuantity(i.getQuantity());
			offer.setAvailable(offer.getQuantity() > 0);
			offerRepository.save(offer);
		});
		orderRepository.save(order);
	}

	@Transactional
	public void delete(Integer orderId) {
		var order = orderRepository.findById(orderId).orElseThrow();
		order.getOrderItems().forEach(i -> {
			var offer = i.getOffer();
			offer.incrementQuantity(i.getQuantity());
			offer.setAvailable(offer.getQuantity() > 0);
			offerRepository.save(offer);
		});
		orderRepository.deleteById(orderId);
	}

	@Transactional
	public void deleteItem(Integer orderId, Integer orderItemId) {
		var order = orderRepository.findOrderById(orderId);
		orderItemService.delete(orderItemId);
		orderRepository.save(order);
	}

	private Order createEmptyOrder(Integer userId) {
		var order = new Order();
		order.setOrderItems(Collections.emptyList());
		order.setUserId(userId);
		order.setStatus(OrderStatus.SAVED);
		order.setCost(0.0);
		order.setCreatedAt(new Date());
		order.setPaid(false);
		return orderRepository.save(order);
	}
}
