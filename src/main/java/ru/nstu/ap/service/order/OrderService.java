package ru.nstu.ap.service.order;

import lombok.AllArgsConstructor;
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
import java.util.Objects;
import java.util.function.Function;

@Service
@AllArgsConstructor
public class OrderService {
	private OrderRepository orderRepository;
	private OfferRepository offerRepository;
	private OrderItemService orderItemService;
	private CartService cartService;
	private UserService userService;

	@Transactional(readOnly = true)
	public <T> List<T> getOrders(Integer userId, Function<Order, T> mapper) {
		return getAll(userId).stream().filter(o -> o.getCost()>0.0).map(mapper).toList();
	}

	public List<Order> getAll(Integer userId) {
		return orderRepository.findAll().stream()
			.filter(o -> o.getUserId().equals(userId))
			.toList();
	}

	public Order getById(Integer userId, Integer id) {
		return orderRepository.findAll().stream()
			.filter(o -> Objects.equals(o.getUserId(), userId))
			.filter(o -> o.getId().equals(id))
			.findFirst()
			.orElseThrow(IllegalArgumentException::new);
	}

	@Transactional
	public void create(Integer userId) {
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
		} else {
			throw new IllegalAccessException("Not enough money");
		}
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
