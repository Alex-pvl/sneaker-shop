package ru.nstu.ap.model.order;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
	private List<OrderItem> orderItems;

	@Enumerated(value = EnumType.STRING)
	private OrderStatus status;

	@Column(name = "id_user")
	private Integer userId;

	@NotNull
	private Double cost;

	@Transient
	private boolean isPaid;
}
