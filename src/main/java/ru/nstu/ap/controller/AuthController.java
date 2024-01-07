package ru.nstu.ap.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.nstu.ap.dto.user.RegistrationDTO;
import ru.nstu.ap.service.user.UserService;

@Controller
@AllArgsConstructor
public class AuthController {
	private UserService userService;

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/register")
	public String registerView(Model model) {
		RegistrationDTO user = new RegistrationDTO();
		model.addAttribute("user", user);
		return "register";
	}

	@PostMapping("/register/save")
	public String register(@Valid @ModelAttribute("user") RegistrationDTO user) {
		try {
			userService.register(user);
		} catch (IllegalArgumentException e) {
			return "redirect:/register?fail";
		}
		return "redirect:/login";
	}

	@GetMapping("/admin")
	public String adminView() {
		return "admin/admin";
	}
}
