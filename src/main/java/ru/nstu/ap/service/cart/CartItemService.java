package ru.nstu.ap.service.cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nstu.ap.model.cart.Cart;
import ru.nstu.ap.model.cart.CartItem;
import ru.nstu.ap.model.catalog.Offer;
import ru.nstu.ap.repository.cart.CartItemRepository;
import ru.nstu.ap.service.catalog.OfferService;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Service
public class CartItemService {
	@Autowired
	private CartItemRepository cartItemRepository;
	@Autowired
	private OfferService offerService;

	@Transactional(readOnly = true)
	public <T> List<T> getCartItemsByCartId(Integer cartId, Function<CartItem, T> mapper) {
		return this.getAll(cartId).stream().map(mapper).toList();
	}

	public List<CartItem> getAll(Integer cartId) {
		return cartItemRepository.findAll().stream()
			.filter(i -> i.getCart().getId().equals(cartId))
			.toList();
	}

	@Transactional(readOnly = true)
	public CartItem getById(Integer id) {
		return cartItemRepository.findById(id)
			.orElseThrow(IllegalArgumentException::new);
	}

	@Transactional
	public CartItem mapFromOffer(Cart cart, Integer offerId, Integer size) {
		var cartItem = new CartItem();
		var offer = offerService.getById(offerId);
		cartItem.setOffer(offer);
		cartItem.setCart(cart);
		cartItem.setQuantity(1);
		cartItem.setSize(size);
		cartItemRepository.save(cartItem);
		return cartItem;
	}

	@Transactional
	public CartItem incrementQuantity(Integer id, Integer cartId) {
		var item = getById(id);
		var cart = item.getCart();
		if (!Objects.equals(cartId, cart.getId())) {
			throw new IllegalArgumentException("Unknown cart");
		}
		item.incrementQuantity();
		cartItemRepository.save(item);
		return item;
	}

	@Transactional
	public CartItem decrementQuantity(Integer id, Integer cartId) {
		var item = getById(id);
		var cart = item.getCart();
		if (!Objects.equals(cartId, cart.getId())) {
			throw new IllegalArgumentException("Unknown cart");
		}
		if (item.getQuantity() > 1) {
			item.decrementQuantity();
		} else {
			throw new IllegalArgumentException("");
		}
		cartItemRepository.save(item);
		return item;
	}

	@Transactional
	public void delete(Integer id) {
		cartItemRepository.deleteById(id);
	}
}
