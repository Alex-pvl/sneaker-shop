package ru.nstu.ap.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nstu.ap.dto.user.RegistrationDTO;
import ru.nstu.ap.model.user.User;
import ru.nstu.ap.repository.user.RoleRepository;
import ru.nstu.ap.repository.user.UserRepository;

import java.util.List;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;

	@Transactional(readOnly = true)
	public User getByEmail(String email) {
		return userRepository.findUserByEmail(email);
	}

	@Transactional(readOnly = true)
	public User getByLogin(String login) {
		return userRepository.findUserByUsername(login);
	}

	@Transactional
	public void saveUser(RegistrationDTO registration) {
		var user = new User();
		user.setName(registration.getName());
		user.setUsername(registration.getUsername());
		user.setEmail(registration.getEmail());
		user.setPassword(registration.getPassword());
		var role = roleRepository.findRoleByName("USER");
		user.setRoles(List.of(role));
		user.setBalance(0.0);
		userRepository.save(user);
		System.out.println(user.getName());
	}
}
