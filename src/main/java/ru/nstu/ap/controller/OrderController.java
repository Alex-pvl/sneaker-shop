package ru.nstu.ap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.nstu.ap.dto.order.OrderDTO;
import ru.nstu.ap.service.order.OrderService;
import ru.nstu.ap.service.user.UserService;
import ru.nstu.ap.utils.SecurityUtil;

@Controller
public class OrderController {
	@Autowired
	private OrderService orderService;
	@Autowired
	private UserService userService;

	@GetMapping("/orders")
	public String ordersPage(Model model) {
		String username = SecurityUtil.getSessionUser();
		if (username == null) {
			return "redirect:/login";
		}
		var user = userService.getByUsername(username);
		var orders = orderService.getOrders(user.getId(), OrderDTO::new).stream()
			.filter(o -> o.getCost()>0.0).toList();
		model.addAttribute("orders", orders);
		return "orders";
	}

	@PostMapping("/createOrder")
	public String createOrder(Model model) {
		String username = SecurityUtil.getSessionUser();
		if (username == null) {
			return "redirect:/login";
		}
		var user = userService.getByUsername(username);
		orderService.create(user.getId());
		model.addAttribute("orders", orderService.getOrders(user.getId(), OrderDTO::new).stream()
			.filter(o -> o.getCost()>0.0).toList());
		return "orders";
	}

	@GetMapping("/orders/{id}")
	public String orderDetailsPage(@PathVariable Integer id, Model model) {
		String username = SecurityUtil.getSessionUser();
		if (username == null) {
			return "redirect:/login";
		}
		var user = userService.getByUsername(username);
		var order = orderService.getById(user.getId(), id);
		boolean isPaid = order.isPaid();
		model.addAttribute("order", order);
		model.addAttribute("isPaid", isPaid);
		return "order-details";
	}

	@PostMapping("/payOrder")
	public String payOrder(@RequestParam("orderId") Integer id, Model model) {
		String username = SecurityUtil.getSessionUser();
		if (username == null) {
			return "redirect:/login";
		}
		var user = userService.getByUsername(username);
		try {
			orderService.pay(id);
		} catch (IllegalAccessException e) {
			return "redirect:/orders/" + id + "?fail";
		}
		model.addAttribute("orders", orderService.getOrders(user.getId(), OrderDTO::new));
		return "orders";
	}

	@PostMapping("/cancelOrder")
	public String cancelOrder(@RequestParam("orderId") Integer id, Model model) {
		String username = SecurityUtil.getSessionUser();
		if (username == null) {
			return "redirect:/login";
		}
		var user = userService.getByUsername(username);
		orderService.cancel(id);
		var orders = orderService.getOrders(user.getId(), OrderDTO::new).stream()
			.filter(o -> o.getCost()>0.0).toList();
		model.addAttribute("orders", orders);
		return "orders";
	}

	@PostMapping("/deleteOrder")
	public String deleteOrder(@RequestParam("orderId") Integer id, Model model) {
		String username = SecurityUtil.getSessionUser();
		if (username == null) {
			return "redirect:/login";
		}
		var user = userService.getByUsername(username);
		orderService.delete(id);
		var orders = orderService.getOrders(user.getId(), OrderDTO::new).stream()
			.filter(o -> o.getCost()>0.0).toList();
		model.addAttribute("orders", orders);
		return "orders";
	}
}
