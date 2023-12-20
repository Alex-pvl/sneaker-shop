package ru.nstu.ap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.nstu.ap.dto.user.UserDTO;
import ru.nstu.ap.model.user.User;
import ru.nstu.ap.service.user.UserService;
import ru.nstu.ap.utils.SecurityUtil;

@Controller
public class UserController {
	@Autowired
	private UserService userService;

	@GetMapping("/profile")
	public String profilePage(Model model) {
		String username = SecurityUtil.getSessionUser();
		if (username == null) {
			return "redirect:/login";
		}
		User user = userService.getByUsername(username);
		boolean isAdmin = username != null && username.equals("admin");
		model.addAttribute("isAdmin", isAdmin);
		model.addAttribute("user", new UserDTO(user));
		return "profile";
	}

	@PostMapping("/addBalance")
	public String addBalance(@RequestParam("balance") int balance, Model model) {
		String username = SecurityUtil.getSessionUser();
		if (username == null) {
			return "redirect:/login";
		}
		boolean isAdmin = username != null && username.equals("admin");
		User user = userService.addBalanceByUsername(username, 1.0 * balance);
		model.addAttribute("isAdmin", isAdmin);
		model.addAttribute("user", new UserDTO(user));
		return "profile";
	}
}
