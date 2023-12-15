package ru.nstu.ap.service.catalog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nstu.ap.model.catalog.Category;
import ru.nstu.ap.repository.catalog.CategoryRepository;

import java.util.List;
import java.util.function.Function;

@Service
public class CategoryService {
	@Autowired
	private CategoryRepository categoryRepository;

	@Transactional(readOnly = true)
	public <T> List<T> getCategories(Function<Category, T> mapper) {
		return this.getAll().stream().map(mapper).toList();
	}

	public List<Category> getAll() {
		return categoryRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Category getById(Integer id) {
		return categoryRepository.findById(id)
			.orElseThrow(IllegalArgumentException::new);
	}

	@Transactional
	public Category create(String name) {
		var category = new Category();
		category.setName(name);
		return categoryRepository.save(category);
	}

	@Transactional
	public Category update(Integer id, String name) {
		var brand = getById(id);
		brand.setName(name);
		return categoryRepository.save(brand);
	}

	@Transactional
	public void deleteById(Integer id) {
		categoryRepository.deleteById(id);
	}
}
