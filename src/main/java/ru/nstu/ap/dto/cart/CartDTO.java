package ru.nstu.ap.dto.cart;

import lombok.Getter;
import ru.nstu.ap.dto.catalog.OfferDTO;
import ru.nstu.ap.model.cart.Cart;
import ru.nstu.ap.model.cart.CartItem;

import java.util.List;

@Getter
public class CartDTO {
	private final Integer id;
	private final Integer userId;
	private final List<CartItemDTO> cartItems;
	private final List<OfferDTO> offers;
	private final Double cost;

	public CartDTO(Cart cart) {
		this.id = cart.getId();
		this.userId = cart.getUserId();
		this.cartItems = cart.getCartItems().stream().map(CartItemDTO::new).toList();
		this.offers = cart.getCartItems().stream().map(CartItem::getOffer).map(OfferDTO::new).toList();
		this.cost = cart.getCartItems().stream()
			.map(i -> i.getOffer().getPrice() * i.getQuantity())
			.mapToDouble(Double::doubleValue)
			.sum();
	}
}
