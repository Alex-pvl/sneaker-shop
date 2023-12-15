package ru.nstu.ap.model.catalog;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;

@Getter
@RequiredArgsConstructor
public class OfferFilterParams {
	@Nullable
	private Double fromPrice;
	@Nullable
	private Double toPrice;
	@Nullable
	private Integer brandId;
	@Nullable
	private Integer size;
}
