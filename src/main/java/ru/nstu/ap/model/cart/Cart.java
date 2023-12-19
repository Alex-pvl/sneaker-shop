package ru.nstu.ap.model.cart;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "cart")
public class Cart {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@OneToMany(mappedBy = "cart", fetch = FetchType.LAZY)
	private List<CartItem> cartItems = new ArrayList<>();

	@Nullable
	@Column(name = "id_user")
	private Integer userId;

	@NotNull
	private Double cost;

	public void addCost(Double amount) {
		this.cost += amount;
	}

	public void subtractCost(Double amount) {
		this.cost -= amount;
	}
}
