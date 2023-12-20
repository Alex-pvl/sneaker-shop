package ru.nstu.ap.model.order;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(schema = "shop", name = "orders")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<OrderItem> orderItems = new ArrayList<>();

	@Enumerated(value = EnumType.STRING)
	private OrderStatus status;

	@Column(name = "id_user")
	private Integer userId;

	@Column(name = "created_at")
	private Date createdAt;

	@NotNull
	private Double cost;

	@Column(name = "is_paid")
	private boolean isPaid;
}
