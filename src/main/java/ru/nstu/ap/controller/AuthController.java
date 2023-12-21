package ru.nstu.ap.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.nstu.ap.dto.user.RegistrationDTO;
import ru.nstu.ap.model.user.User;
import ru.nstu.ap.service.cart.CartService;
import ru.nstu.ap.service.user.UserService;

@Controller
public class AuthController {
	@Autowired
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
	public String register(
		@Valid @ModelAttribute("user")RegistrationDTO user,
		BindingResult result, Model model
	) {
		var isNotValid = user == null || user.getUsername().isEmpty() || user.getEmail().isEmpty() || user.getName().isEmpty()
			|| user.getPassword().isEmpty() || user.getPassword().isBlank() || user.getName().isBlank() || user.getUsername().isBlank();
		if (isNotValid) {
			return "redirect:/register?fail";
		}
		User existingUserEmail = userService.getByEmail(user.getEmail());
		if (existingUserEmail != null && existingUserEmail.getEmail() != null && !existingUserEmail.getEmail().isEmpty()) {
			return "redirect:/register?fail";
		}
		User existingUserLogin = userService.getByUsername(user.getUsername());
		if (existingUserLogin != null && existingUserLogin.getUsername() != null && !existingUserLogin.getUsername().isEmpty()) {
			return "redirect:/register?fail";
		}
		if (result.hasErrors()) {
			model.addAttribute("user", user);
			return "register";
		}
		userService.saveUser(user);
		return "redirect:/login";
	}

	@GetMapping("/admin")
	public String adminView() {
		return "admin-index";
	}
}
