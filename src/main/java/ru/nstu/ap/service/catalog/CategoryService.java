package ru.nstu.ap.service.catalog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
		return categoryRepository.findCategoryById(id);
	}

	@Transactional(readOnly = true)
	public Category getByName(String name) {
		return categoryRepository.findCategoryByName(name);
	}

	@Transactional
	public void create(Category category) {
		categoryRepository.save(category);
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

	@Transactional(readOnly = true)
	public Page<Category> findPaginated(int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		return categoryRepository.findAll(pageable);
	}
}
