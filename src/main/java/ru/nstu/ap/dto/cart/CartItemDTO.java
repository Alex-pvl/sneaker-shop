package ru.nstu.ap.dto.cart;

import lombok.Getter;
import ru.nstu.ap.model.cart.CartItem;

@Getter
public class CartItemDTO {
	private final Integer id;
	private final Integer offerId;
	private final String name;
	private final String imageUrl;
	private final Integer quantity;
	private final Integer size;

	public CartItemDTO(CartItem item) {
		this.id = item.getId();
		this.offerId = item.getOffer().getId();
		this.name = item.getOffer().getName();
		this.imageUrl = item.getOffer().getImageUrl();
		this.quantity = item.getQuantity();
		this.size = item.getSize();
	}
}
