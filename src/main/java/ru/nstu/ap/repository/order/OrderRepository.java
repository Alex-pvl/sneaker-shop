package ru.nstu.ap.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nstu.ap.model.order.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
}
