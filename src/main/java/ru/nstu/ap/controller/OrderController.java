package ru.nstu.ap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.nstu.ap.model.order.Order;
import ru.nstu.ap.model.order.OrderStatus;
import ru.nstu.ap.repository.order.OrderRepository;

import java.util.Collections;

@RestController
public class OrderController {
	@Autowired
	private OrderRepository orderRepository;

	@PostMapping("/order")
	public Order create(@RequestBody Params params) {
		var order = new Order();
		order.setOrderItems(Collections.emptyList());
		order.setCost(params.sum);
		order.setPaid(false);
		order.setUserId(1);
		order.setStatus(OrderStatus.SAVED);
		return orderRepository.save(order);
	}

	record Params(double sum) {}
}
