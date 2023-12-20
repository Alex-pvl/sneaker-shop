package ru.nstu.ap.model.cart;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.nstu.ap.model.catalog.Offer;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(schema = "shop", name = "cart_items")
public class CartItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_offer")
	private Offer offer;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_cart")
	private Cart cart;

	@NotNull
	private Integer quantity;

	@NotNull
	private Integer size;

	public void incrementQuantity() {
		quantity++;
	}

	public void decrementQuantity() {
		quantity--;
	}
}
