package ru.nstu.ap.controller.request;

public record CreateOfferParams(
	String name,
	Integer brandId,
	Integer categoryId,
	Double price,
	Integer quantity,
	String imageUrl
) {}
