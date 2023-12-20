package ru.nstu.ap.repository.catalog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.nstu.ap.model.catalog.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
	Category findCategoryById(Integer id);
	Category findCategoryByName(String name);
}
