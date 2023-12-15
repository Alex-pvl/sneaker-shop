package ru.nstu.ap.dto.catalog;

import lombok.Getter;
import ru.nstu.ap.model.catalog.Offer;
import ru.nstu.ap.model.catalog.OfferSize;

import java.util.List;

@Getter
public class OfferDTO {
	private final Integer id;
	private final String name;
	private final String brand;
	private final String category;
	private final Double price;
	private final List<Integer> availableSizes;
	private final Integer quantity;
	private final String imageUrl;

	public OfferDTO(Offer offer) {
		this.id = offer.getId();
		this.name = offer.getName();
		this.brand = offer.getBrand() != null ? offer.getBrand().getName() : "noname";
		this.category = offer.getCategory() != null ? offer.getCategory().getName() : "кроссовки";
		this.price = offer.getPrice();
		this.availableSizes = offer.getSizes().stream().map(OfferSize::getSize).toList();
		this.quantity = offer.getQuantity();
		this.imageUrl = offer.getImageUrl();
	}
}
