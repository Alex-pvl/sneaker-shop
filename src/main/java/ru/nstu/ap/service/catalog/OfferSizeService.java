package ru.nstu.ap.service.catalog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nstu.ap.repository.catalog.OfferSizeRepository;

@Service
public class OfferSizeService {
	@Autowired
	private OfferSizeRepository offerSizeRepository;


}
