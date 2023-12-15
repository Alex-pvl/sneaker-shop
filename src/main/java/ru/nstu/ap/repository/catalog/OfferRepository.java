package ru.nstu.ap.repository.catalog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.nstu.ap.model.catalog.Offer;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Integer> {
	@Query(value = """
		SELECT COUNT(*) FROM offer_size os
		LEFT JOIN sizes s ON s.id = os.id_size
		WHERE s.size = :size
	""", nativeQuery = true)
	int hasSize(int size);
}
