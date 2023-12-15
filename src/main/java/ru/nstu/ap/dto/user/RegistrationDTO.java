package ru.nstu.ap.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationDTO {
	private Integer id;
	@NotNull
	private String name;
	@NotNull
	private String username;
	@NotNull
	private String email;
	@NotNull
	private String password;
}
