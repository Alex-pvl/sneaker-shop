package ru.nstu.ap.dto.order;

import lombok.Getter;
import ru.nstu.ap.dto.catalog.OfferDTO;
import ru.nstu.ap.model.order.Order;
import ru.nstu.ap.model.order.OrderItem;
import ru.nstu.ap.model.order.OrderStatus;

import java.util.Date;
import java.util.List;

@Getter
public class OrderDTO {
	private final Integer id;
	private final Integer userId;
	private final List<OrderItemDTO> orderItems;
	private final List<OfferDTO> offers;
	private final OrderStatus status;
	private final boolean isPaid;
	private final Date createdAt;
	private final Double cost;

	public OrderDTO(Order order) {
		this.id = order.getId();
		this.userId = order.getUserId();
		this.orderItems = order.getOrderItems().stream().map(OrderItemDTO::new).toList();
		this.offers = order.getOrderItems().stream().map(OrderItem::getOffer).map(OfferDTO::new).toList();
		this.status = order.getStatus();
		this.isPaid = order.isPaid();
		this.createdAt = new Date();
		this.cost = order.getOrderItems().stream()
			.map(i -> i.getOffer().getPrice() * i.getQuantity())
			.mapToDouble(Double::doubleValue)
			.sum();
	}
}
