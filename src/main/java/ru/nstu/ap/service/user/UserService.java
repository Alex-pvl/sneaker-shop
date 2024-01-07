package ru.nstu.ap.service.user;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nstu.ap.dto.user.RegistrationDTO;
import ru.nstu.ap.model.user.User;
import ru.nstu.ap.repository.user.RoleRepository;
import ru.nstu.ap.repository.user.UserRepository;
import ru.nstu.ap.service.cart.CartService;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
	private UserRepository userRepository;
	private RoleRepository roleRepository;
	private PasswordEncoder passwordEncoder;
	private CartService cartService;

	@Transactional(readOnly = true)
	public User getById(Integer id) {
		return userRepository.findUserById(id);
	}

	@Transactional(readOnly = true)
	public User getByEmail(String email) {
		return userRepository.findUserByEmail(email);
	}

	@Transactional(readOnly = true)
	public User getByUsername(String username) {
		return userRepository.findUserByUsername(username);
	}

	@Transactional
	public User addBalanceByUsername(String username, Double amount) {
		var user = getByUsername(username);
		user.setBalance(user.getBalance() + amount);
		return userRepository.save(user);
	}

	@Transactional
	public void register(RegistrationDTO user) throws IllegalArgumentException {
		var isNotValid = user == null || user.getUsername().isEmpty() ||
				user.getEmail().isEmpty() || user.getName().isEmpty() ||
				user.getPassword().isEmpty() || user.getPassword().isBlank() ||
				user.getName().isBlank() || user.getUsername().isBlank();

		if (isNotValid) {
			throw new IllegalArgumentException("Validation error");
		}

		User existingUserEmail = getByEmail(user.getEmail());
		var alreadyExistsEmail = existingUserEmail != null &&
				existingUserEmail.getEmail() != null && !existingUserEmail.getEmail().isEmpty();
		if (alreadyExistsEmail) {
			throw new IllegalArgumentException("User with this email already exists");
		}

		User existingUserLogin = getByUsername(user.getUsername());
		var alreadyExistsLogin = existingUserLogin != null &&
				existingUserLogin.getUsername() != null && !existingUserLogin.getUsername().isEmpty();
		if (alreadyExistsLogin) {
			throw new IllegalArgumentException("User with this username already exists");
		}

		saveUser(user);
	}

	private void saveUser(RegistrationDTO registration) {
		var user = new User();
		var role = roleRepository.findRoleByName("USER");

		user.setName(registration.getName());
		user.setUsername(registration.getUsername());
		user.setEmail(registration.getEmail());
		user.setPassword(passwordEncoder.encode(registration.getPassword()));
		user.setRoles(List.of(role));
		user.setBalance(0.0);

		userRepository.save(user);
		cartService.createEmptyCart(user.getId());
	}
}
