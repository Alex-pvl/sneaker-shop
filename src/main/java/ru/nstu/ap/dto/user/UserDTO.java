package ru.nstu.ap.dto.user;

import lombok.Getter;
import ru.nstu.ap.model.user.User;

@Getter
public class UserDTO {
	private final Integer id;
	private final String name;
	private final String username;
	private final String email;
	private final Double balance;

	public UserDTO(User user) {
		this.id = user.getId();
		this.name = user.getName();
		this.username = user.getUsername();
		this.email = user.getEmail();
		this.balance = user.getBalance();
	}
}
