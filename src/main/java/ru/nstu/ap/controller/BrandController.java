package ru.nstu.ap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.nstu.ap.dto.catalog.BrandDTO;
import ru.nstu.ap.service.catalog.BrandService;

import java.util.List;

@RestController
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

	@PostMapping("/admin/brands")
	public BrandDTO create(@RequestBody String name) {
		return new BrandDTO(brandService.create(name));
	}
}
