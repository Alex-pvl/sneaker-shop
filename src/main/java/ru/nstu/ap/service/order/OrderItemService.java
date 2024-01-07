package ru.nstu.ap.service.order;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nstu.ap.model.cart.Cart;
import ru.nstu.ap.model.order.OrderItem;
import ru.nstu.ap.repository.order.OrderItemRepository;
import ru.nstu.ap.repository.order.OrderRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderItemService {
	private OrderItemRepository orderItemRepository;
	private OrderRepository orderRepository;

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
}
