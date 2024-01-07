package ru.nstu.ap.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.nstu.ap.dto.user.UserDTO;
import ru.nstu.ap.service.user.UserService;
import ru.nstu.ap.utils.SecurityUtil;

@Controller
@AllArgsConstructor
public class UserController {
	private UserService userService;

	@GetMapping("/profile")
	public String profilePage(Model model) {
		String username = SecurityUtil.getSessionUser();
		boolean isAdmin = SecurityUtil.isAdmin();

		if (username == null) {
			return "redirect:/login";
		}

		var user = userService.getByUsername(username);

		model.addAttribute("isAdmin", isAdmin);
		model.addAttribute("user", new UserDTO(user));
		return "profile";
	}

	@PostMapping("/addBalance")
	public String addBalance(@RequestParam("balance") double balance, Model model) {
		String username = SecurityUtil.getSessionUser();
		boolean isAdmin = SecurityUtil.isAdmin();

		if (username == null) {
			return "redirect:/login";
		}

		var user = userService.addBalanceByUsername(username, balance);

		model.addAttribute("isAdmin", isAdmin);
		model.addAttribute("user", new UserDTO(user));
		return "profile";
	}
}
