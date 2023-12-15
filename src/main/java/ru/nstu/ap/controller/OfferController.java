package ru.nstu.ap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.nstu.ap.controller.request.CreateOfferParams;
import ru.nstu.ap.dto.catalog.OfferDTO;
import ru.nstu.ap.service.catalog.OfferService;

import java.util.List;

@Controller
public class OfferController {
	@Autowired
	private OfferService offerService;

	@GetMapping("/")
	public String catalogView(Model model) {
		model.addAttribute("offers", offerService.getOffers(OfferDTO::new));
		return "catalog";
	}

	@GetMapping("/offers")
	public List<OfferDTO> getAll() {
		return offerService.getOffers(OfferDTO::new);
	}

	@GetMapping("/offers/{id}")
	public String offerDetailsView(@PathVariable Integer id, Model model) {
		var offer = new OfferDTO(offerService.getById(id));
		model.addAttribute("offer", offer);
		return "offer-details";
	}

	@PostMapping("/offers/{id}")
	public OfferDTO getById(@PathVariable Integer id, @RequestBody Integer size) {
		return new OfferDTO(offerService.getById(id));
	}

	@PostMapping("/admin/offers")
	public OfferDTO create(@RequestBody CreateOfferParams params) {
		return new OfferDTO(offerService.create(
			params.name(),
			params.brandId(),
			params.categoryId(),
			params.price(),
			params.quantity(),
			params.imageUrl()
		));
	}

}
