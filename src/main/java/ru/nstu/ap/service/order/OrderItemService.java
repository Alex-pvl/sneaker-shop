package ru.nstu.ap.service.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nstu.ap.model.cart.Cart;
import ru.nstu.ap.model.cart.CartItem;
import ru.nstu.ap.model.order.OrderItem;
import ru.nstu.ap.repository.catalog.OfferRepository;
import ru.nstu.ap.repository.order.OrderItemRepository;
import ru.nstu.ap.repository.order.OrderRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderItemService {
	@Autowired
	private OrderItemRepository orderItemRepository;
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private OfferRepository offerRepository;

	@Transactional(readOnly = true)
	public OrderItem getById(Integer id) {
		return orderItemRepository.findById(id)
			.orElseThrow(IllegalArgumentException::new);
	}

	@Transactional
	public List<OrderItem> mapFromCart(Integer orderId, Cart cart) {
		var order = orderRepository.findById(orderId)
			.orElseThrow(IllegalArgumentException::new);

		var list = new ArrayList<OrderItem>();
		cart.getCartItems().forEach(i -> {
			var item = new OrderItem();
			item.setOrder(order);
			item.setOffer(i.getOffer());
			item.setSize(i.getSize());
			item.setQuantity(i.getQuantity());
			list.add(item);
			orderItemRepository.save(item);
		});

		return list;
	}

	@Transactional
	public void create(Integer orderId, CartItem cartItem) {
		var order = orderRepository.findById(orderId)
			.orElseThrow(IllegalArgumentException::new);

		var item = new OrderItem();
		item.setOrder(order);
		item.setOffer(cartItem.getOffer());
		item.setSize(cartItem.getSize());
		item.setQuantity(cartItem.getQuantity());

		orderItemRepository.save(item);
	}

	@Transactional
	public void delete(Integer id) {
		var item = orderItemRepository.findOrderItemById(id);
		item.getOffer().incrementQuantity(item.getQuantity());
		item.getOffer().setAvailable(item.getOffer().getQuantity() > 0);
		offerRepository.save(item.getOffer());
		orderItemRepository.deleteById(id);
	}
}
