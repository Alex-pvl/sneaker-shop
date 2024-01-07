package ru.nstu.ap.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.nstu.ap.dto.catalog.CategoryDTO;
import ru.nstu.ap.model.catalog.Category;
import ru.nstu.ap.service.catalog.CategoryService;
import ru.nstu.ap.utils.SecurityUtil;

import java.util.List;

@Controller
@AllArgsConstructor
public class CategoryController {
	private CategoryService categoryService;

	@GetMapping("/categories")
	public List<CategoryDTO> getAll() {
		return categoryService.getCategories(CategoryDTO::new);
	}

	@GetMapping("/categories/{id}")
	public CategoryDTO getById(@PathVariable Integer id) {
		return new CategoryDTO(categoryService.getById(id));
	}

	/** -------------- Admin pages -------------- */

	@GetMapping("/admin/categories")
	public String adminCategories(Model model) {
		if (!SecurityUtil.isAdmin()) return "redirect:/";
		adminViewCategories(1, model);
		return "admin/categories";
	}

	@GetMapping("/categories/page/{pageNo}")
	public String adminViewCategories(@PathVariable int pageNo, Model model) {
		if (!SecurityUtil.isAdmin()) return "redirect:/";
		Page<CategoryDTO> page = categoryService.findPaginated(pageNo, 5).map(CategoryDTO::new);
		List<CategoryDTO> list = page.getContent();

		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("categories", list);
		return "admin/categories";
	}

	@GetMapping("/admin/addCategory")
	public String addCategoryView(Model model) {
		var category = new Category();
		model.addAttribute("category", category);
		return "admin/categories_new";
	}

	@PostMapping("/admin/addCategory")
	public String addCategory(@ModelAttribute("category") Category category) {
		categoryService.create(category);
		return "redirect:/admin/categories";
	}

	@GetMapping("/admin/updateCategory/{id}")
	public String updateCategoryView(@PathVariable int id, Model model) {
		var category = categoryService.getById(id);
		model.addAttribute("category", category);
		return "admin/categories_update";
	}

	@GetMapping("/admin/deleteCategory/{id}")
	public String deleteCategory(@PathVariable int id) {
		categoryService.deleteById(id);
		return "redirect:/admin/categories";
	}
}
