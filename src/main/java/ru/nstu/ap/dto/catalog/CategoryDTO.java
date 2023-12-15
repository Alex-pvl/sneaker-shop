package ru.nstu.ap.dto.catalog;

import lombok.Getter;
import ru.nstu.ap.model.catalog.Category;

@Getter
public class CategoryDTO {
	private final Integer id;
	private final String name;

	public CategoryDTO(Category category) {
		this.id = category.getId();
		this.name = category.getName();
	}
}
