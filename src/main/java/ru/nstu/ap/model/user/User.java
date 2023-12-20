package ru.nstu.ap.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(schema = "auth", name = "users")
public class User implements UserDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false)
	@NotNull
	private String name;

	@Column(nullable = false, unique = true)
	@NotNull
	private String username;

	@Column(nullable = false)
	@NotNull
	private String email;

	@Column(nullable = false)
	@NotNull
	private String password;

	@NotNull
	@Column(nullable = false)
	private Double balance;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(
		schema = "auth",
		name = "user_roles",
		joinColumns = {@JoinColumn(name = "id_user", referencedColumnName = "id")},
		inverseJoinColumns = {@JoinColumn(name = "id_role", referencedColumnName = "id")}
	)
	private List<Role> roles = new ArrayList<>();

	@JsonIgnore
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.emptyList();
	}

	@JsonIgnore
	@Override
	public String getUsername() {
		return username;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isEnabled() {
		return true;
	}
}
