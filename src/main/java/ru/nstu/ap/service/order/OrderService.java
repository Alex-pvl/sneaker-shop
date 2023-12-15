package ru.nstu.ap.service.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nstu.ap.model.cart.Cart;
import ru.nstu.ap.model.order.Order;
import ru.nstu.ap.model.order.OrderStatus;
import ru.nstu.ap.repository.order.OrderRepository;
import ru.nstu.ap.service.cart.CartService;

import java.util.List;
import java.util.function.Function;

@Service
public class OrderService {
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private OrderItemService orderItemService;
	@Autowired
	private CartService cartService;

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
	public Integer create(Integer userId) {
		var order = new Order();
		order.setUserId(userId);
		var cart = cartService.getByUserId(userId);
		var items = orderItemService.mapFromCart(order.getId(), cart);
		order.setOrderItems(items);
		order.setStatus(OrderStatus.SAVED);
		order.setCost(items.stream()
			.map(i -> i.getOffer().getPrice() * i.getQuantity())
			.mapToDouble(Double::doubleValue)
			.sum()
		);
		order.setPaid(false);
		orderRepository.save(order);
		order.getOrderItems().forEach(i -> {
			var offer = i.getOffer();
			offer.decrementQuantity(i.getQuantity());
		});
		return order.getId();
	}

	@Transactional
	public void updateItem(Integer orderId, Integer orderItemId, boolean increment) {
		var order = orderRepository.findById(orderId)
			.orElseThrow(IllegalArgumentException::new);
		if (increment) {
			orderItemService.incrementQuantity(orderItemId, orderId);
		} else {
			orderItemService.decrementQuantity(orderItemId, orderId);
		}
		order.getOrderItems().forEach(i -> {
			var offer = i.getOffer();
			if (increment) {
				offer.decrementQuantity(1);
			} else {
				offer.incrementQuantity(1);
			}
		});
		orderRepository.save(order);
	}

	@Transactional
	public void process(Integer orderId) {
		var order = orderRepository.findById(orderId)
			.orElseThrow(IllegalArgumentException::new);
		order.setStatus(OrderStatus.PROCESSING);
		orderRepository.save(order);
	}

	@Transactional
	public void pay(Integer orderId) {
		var order = orderRepository.findById(orderId)
			.orElseThrow(IllegalArgumentException::new);
		order.setPaid(true);
		order.setStatus(OrderStatus.DONE);
		orderRepository.save(order);
	}

	@Transactional
	public void cancel(Integer orderId) {
		var order = orderRepository.findById(orderId)
			.orElseThrow(IllegalArgumentException::new);
		order.setStatus(OrderStatus.CANCELED);
		order.getOrderItems().forEach(i -> {
			var offer = i.getOffer();
			offer.incrementQuantity(i.getQuantity());
		});
		orderRepository.save(order);
	}

	@Transactional
	public void delete(Integer orderId) {
		orderRepository.deleteById(orderId);
	}
}
