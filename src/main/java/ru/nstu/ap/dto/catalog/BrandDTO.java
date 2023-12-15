package ru.nstu.ap.dto.catalog;

import lombok.Getter;
import ru.nstu.ap.model.catalog.Brand;

@Getter
public class BrandDTO {
	private final Integer id;
	private final String name;

	public BrandDTO(Brand brand) {
		this.id = brand.getId();
		this.name = brand.getName();
	}
}
