package ru.nstu.ap.controller;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class CartController {
	private CartService cartService;
	private UserService userService;

	@GetMapping("/cart")
	public String cartPage(Model model) {
		String username = SecurityUtil.getSessionUser();
		if (username == null) {
			return "redirect:/login";
		}
		var user = userService.getByUsername(username);
		var cart = cartService.getCart(user.getId(), CartDTO::new);
		model.addAttribute("cart", cart);
		return "cart";
	}

	@PostMapping("/addToCart")
	public String addToCart(
		@RequestParam("offerId") int offerId,
		@RequestParam("selectedSize") String size,
		Model model
	) {
		int sizeValue;
		try {
			sizeValue = Integer.parseInt(size);
		} catch (Exception e) {
			return "redirect:/offers/%s?fail".formatted(offerId);
		}

		String username = SecurityUtil.getSessionUser();
		if (username == null) {
			return "redirect:/login";
		}

		var user = userService.getByUsername(username);
		cartService.addOffer(user.getId(), offerId, sizeValue);

		model.addAttribute("cart", cartService.getByUserId(user.getId()));
		return "cart";
	}

	@PostMapping("/incrementQuantity")
	public String incrementQuantity(
		@RequestParam("cartItemId") int cartItemId,
		Model model
	) {
		String username = SecurityUtil.getSessionUser();
		if (username == null) {
			return "redirect:/login";
		}

		var user = userService.getByUsername(username);
		cartService.updateItem(user.getId(), cartItemId, true);
		var cart = cartService.getCart(user.getId(), CartDTO::new);

		model.addAttribute("cart", cart);
		return "cart";
	}

	@PostMapping("/decrementQuantity")
	public String decrementQuantity(
		@RequestParam("cartItemId") int cartItemId,
		Model model
	) {
		String username = SecurityUtil.getSessionUser();
		if (username == null) {
			return "redirect:/login";
		}

		var user = userService.getByUsername(username);
		cartService.updateItem(user.getId(), cartItemId, false);
		var cart = cartService.getCart(user.getId(), CartDTO::new);

		model.addAttribute("cart", cart);
		return "cart";
	}

	@PostMapping("/removeCartItem")
	public String removeCartItem(
		@RequestParam("cartItemId") int cartItemId,
		Model model
	) {
		String username = SecurityUtil.getSessionUser();
		if (username == null) {
			return "redirect:/login";
		}

		var user = userService.getByUsername(username);
		cartService.deleteItem(user.getId(), cartItemId);
		var cart = cartService.getCart(user.getId(), CartDTO::new);

		model.addAttribute("cart", cart);
		return "cart";
	}

	@PostMapping("/clearCart")
	public String clearCart(Model model) {
		String username = SecurityUtil.getSessionUser();
		if (username == null) {
			return "redirect:/login";
		}

		var user = userService.getByUsername(username);
		cartService.clear(user.getId());
		cartService.createEmptyCart(user.getId());
		var cart = cartService.getCart(user.getId(), CartDTO::new);

		model.addAttribute("cart", cart);
		return "cart";
	}
}
