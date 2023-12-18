package ru.nstu.ap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.nstu.ap.dto.cart.CartDTO;
import ru.nstu.ap.service.cart.CartService;
import ru.nstu.ap.service.user.UserService;
import ru.nstu.ap.utils.SecurityUtil;

@Controller
public class CartController {
	@Autowired
	private CartService cartService;
	@Autowired
	private UserService userService;

	@GetMapping("/cart")
	public String cartPage(Model model) {
		String username = SecurityUtil.getSessionUser();
		if (username == null) {
			return "redirect:/login";
		}
		var user = userService.getByUsername(username);
		var cart = cartService.getByUserId(user.getId());
		model.addAttribute("cart", cart);
		return "cart";
	}

	@PostMapping("/addToCart")
	public String addToCart(
		@RequestParam("offerId") int offerId,
		@RequestParam("selectedSize") int size,
		Model model
	) {
		String username = SecurityUtil.getSessionUser();
		if (username == null) {
			return "redirect:/login";
		}
		var user = userService.getByUsername(username);
		var cart = cartService.getByUserId(user.getId());
		var cartItem = cartService.addOffer(user.getId(), offerId, size);
		model.addAttribute("cart", cart);
		return "cart";
	}

}
