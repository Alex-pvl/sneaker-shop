package ru.nstu.ap.service.user;

import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.nstu.ap.repository.user.UserRepository;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		var user = userRepository.findUserByUsername(username);
		if (user != null) {
			return new User(
				user.getUsername(),
				user.getPassword(),
				user.getRoles().stream()
					.map(role -> new SimpleGrantedAuthority(role.getName()))
					.toList()
			);
		} else {
			throw new UsernameNotFoundException("Invalid username or password");
		}
	}
}
