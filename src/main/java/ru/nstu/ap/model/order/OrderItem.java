package ru.nstu.ap.model.order;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.nstu.ap.model.catalog.Offer;
import ru.nstu.ap.model.order.Order;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(schema = "shop", name = "order_items")
public class OrderItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_order")
	private Order order;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_offer")
	private Offer offer;

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
