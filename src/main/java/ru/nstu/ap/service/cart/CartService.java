package ru.nstu.ap.service.cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nstu.ap.model.cart.Cart;
import ru.nstu.ap.model.cart.CartItem;
import ru.nstu.ap.repository.cart.CartRepository;

import java.util.Collections;
import java.util.Objects;
import java.util.function.Function;

@Service
public class CartService {
	@Autowired
	private CartRepository cartRepository;
	@Autowired
	private CartItemService cartItemService;

	@Transactional(readOnly = true)
	public <T> T getCart(Integer userId, Function<Cart, T> mapper) {
		return mapper.apply(getByUserId(userId));
	}

	public Cart getByUserId(Integer userId) {
		return cartRepository.findAll().stream()
			.filter(c -> Objects.equals(c.getUserId(), userId)).findFirst()
			.orElseThrow(IllegalArgumentException::new);
	}

	@Transactional
	public CartItem addOffer(Integer userId, Integer offerId, Integer size) {
		Cart cart = getByUserId(userId);
		var cartItem = cartItemService.mapFromOffer(cart, offerId, size);
		cartRepository.save(cart);
		return cartItem;
	}

	@Transactional
	public void updateItem(Integer userId, Integer cartItemId, boolean increment) {
		var cart = getByUserId(userId);
		if (increment) {
			cartItemService.incrementQuantity(cartItemId, cart.getId());
		} else {
			cartItemService.decrementQuantity(cartItemId, cart.getId());
		}
		cartRepository.save(cart);
	}

	@Transactional
	public void deleteItem(Integer userId, Integer cartItemId) {
		var cart = getByUserId(userId);
		var item = cartItemService.getById(cartItemId);
		cartItemService.delete(cartItemId);
		cart.getCartItems().remove(item);
		cartRepository.save(cart);
	}

	@Transactional
	public void createEmptyCart(Integer userId) {
		var cart = new Cart();
		cart.setCartItems(Collections.emptyList());
		cart.setUserId(userId);
		cart.setCost(0.0);
		cartRepository.save(cart);
	}
}
