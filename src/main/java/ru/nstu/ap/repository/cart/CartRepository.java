package ru.nstu.ap.repository.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nstu.ap.model.cart.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
	void deleteAllByUserId(Integer userId);
}
