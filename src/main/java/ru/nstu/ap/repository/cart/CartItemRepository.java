package ru.nstu.ap.repository.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nstu.ap.model.cart.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
}
