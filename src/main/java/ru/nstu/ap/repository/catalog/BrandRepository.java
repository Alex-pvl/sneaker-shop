package ru.nstu.ap.repository.catalog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nstu.ap.model.catalog.Brand;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer> {
	Brand findBrandById(Integer id);
}
