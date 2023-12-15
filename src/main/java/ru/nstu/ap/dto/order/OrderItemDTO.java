package ru.nstu.ap.dto.order;

import lombok.Getter;
import ru.nstu.ap.model.order.OrderItem;

@Getter
public class OrderItemDTO {
	private final Integer id;
	private final Integer offerId;
	private final String name;
	private final String imageUrl;
	private final Integer quantity;
	private final Integer size;
	private final Double cost;

	public OrderItemDTO(OrderItem item) {
		this.id = item.getId();
		this.offerId = item.getOffer().getId();
		this.name = item.getOffer().getName();
		this.imageUrl = item.getOffer().getImageUrl();
		this.quantity = item.getQuantity();
		this.size = item.getSize();
		this.cost = item.getOffer().getPrice() * this.quantity;
	}
}
