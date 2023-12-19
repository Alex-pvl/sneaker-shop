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
	public void addOffer(Integer userId, Integer offerId, Integer size) {
		Cart cart = getByUserId(userId);
		cart.setEmpty(false);
		var cartItem = cartItemService.mapFromOffer(cart, offerId, size);
		cart.addCost(cartItem.getOffer().getPrice());
	}

	@Transactional
	public void updateItem(Integer userId, Integer cartItemId, boolean increment) {
		Cart cart = getByUserId(userId);
		CartItem item;
		if (increment) {
			item = cartItemService.incrementQuantity(cartItemId, cart.getId());
			cart.addCost(item.getOffer().getPrice());
		} else {
			item = cartItemService.decrementQuantity(cartItemId, cart.getId());
			cart.subtractCost(item.getOffer().getPrice());
		}
		cartRepository.save(cart);
	}

	@Transactional
	public void deleteItem(Integer userId, Integer cartItemId) {
		var cart = getByUserId(userId);
		cartItemService.delete(cartItemId);
		cartRepository.save(cart);
	}

	@Transactional
	public void clear(Integer userId) {
		var cart = getByUserId(userId);
		cart.setEmpty(true);
		cartRepository.deleteAllByUserId(userId);
	}

	@Transactional
	public Cart createEmptyCart(Integer userId) {
		var cart = new Cart();
		cart.setCartItems(Collections.emptyList());
		cart.setUserId(userId);
		cart.setEmpty(true);
		cart.setCost(0.0);
		cartRepository.save(cart);
		return cart;
	}
}
