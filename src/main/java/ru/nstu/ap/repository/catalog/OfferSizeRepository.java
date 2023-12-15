package ru.nstu.ap.repository.catalog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nstu.ap.model.catalog.OfferSize;

@Repository
public interface OfferSizeRepository extends JpaRepository<OfferSize, Integer> {
}
