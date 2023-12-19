package ru.nstu.ap.model.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {
	SAVED(1),
	PROCESSING(2),
	DONE(3),
	CANCELED(4);

	private final Integer id;
}
