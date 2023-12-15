package ru.nstu.ap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.nstu.ap.dto.catalog.CategoryDTO;
import ru.nstu.ap.service.catalog.CategoryService;

import java.util.List;

@RestController
public class CategoryController {
	@Autowired
	private CategoryService categoryService;

	@GetMapping("/categories")
	public List<CategoryDTO> getAll() {
		return categoryService.getCategories(CategoryDTO::new);
	}

	@GetMapping("/categories/{id}")
	public CategoryDTO getById(@PathVariable Integer id) {
		return new CategoryDTO(categoryService.getById(id));
	}

	@PostMapping("/admin/categories")
	public CategoryDTO create(@RequestBody String name) {
		return new CategoryDTO(categoryService.create(name));
	}
}
