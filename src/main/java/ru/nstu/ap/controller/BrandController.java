package ru.nstu.ap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.nstu.ap.dto.catalog.BrandDTO;
import ru.nstu.ap.model.catalog.Brand;
import ru.nstu.ap.service.catalog.BrandService;
import ru.nstu.ap.utils.SecurityUtil;

import java.util.List;

@Controller
public class BrandController {
	@Autowired
	private BrandService brandService;

	@GetMapping("/brands")
	public List<BrandDTO> getAll() {
		return brandService.getBrands(BrandDTO::new);
	}

	@GetMapping("/brands/{id}")
	public BrandDTO getById(@PathVariable Integer id) {
		return new BrandDTO(brandService.getById(id));
	}

	@GetMapping("/admin/brands")
	public String adminBrands(Model model) {
		String username = SecurityUtil.getSessionUser();
		if (username == null || !username.equals("admin")) return "redirect:/";
		adminViewBrands(1, model);
		return "admin-brands-index";
	}

	@GetMapping("/brands/page/{pageNo}")
	public String adminViewBrands(@PathVariable int pageNo, Model model) {
		String username = SecurityUtil.getSessionUser();
		if (username == null || !username.equals("admin")) {
			return "redirect:/";
		}
		int pageSize = 5;
		Page<BrandDTO> page = brandService.findPaginated(pageNo, pageSize).map(BrandDTO::new);
		List<BrandDTO> list = page.getContent();
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("brands", list);
		return "admin-brands-index";
	}

	@GetMapping("/admin/addBrand")
	public String addBrandView(Model model) {
		Brand brand = new Brand();
		model.addAttribute("brand", brand);
		return "admin-brands-new";
	}

	@PostMapping("/admin/addBrand")
	public String addBrand(@ModelAttribute("brand") Brand brand) {
		brandService.create(brand);
		return "redirect:/admin/brands";
	}

	@GetMapping("/admin/updateBrand/{id}")
	public String updateBrandView(@PathVariable int id, Model model) {
		Brand brand = brandService.getById(id);
		model.addAttribute("brand", brand);
		return "admin-brands-update";
	}

	@GetMapping("/admin/deleteBrand/{id}")
	public String deleteBrand(@PathVariable int id) {
		brandService.deleteById(id);
		return "redirect:/admin/brands";
	}
}
