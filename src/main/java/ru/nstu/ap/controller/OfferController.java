package ru.nstu.ap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.nstu.ap.controller.request.CreateOfferParams;
import ru.nstu.ap.dto.catalog.OfferDTO;
import ru.nstu.ap.model.catalog.Offer;
import ru.nstu.ap.service.catalog.OfferService;
import ru.nstu.ap.utils.SecurityUtil;

import java.util.List;

@Controller
public class OfferController {
	@Autowired
	private OfferService offerService;

	@GetMapping("/")
	public String catalogView(Model model) {
		String username = SecurityUtil.getSessionUser();
		model.addAttribute("offers", offerService.getOffers(OfferDTO::new));
		model.addAttribute("isAdmin", username.equals("admin"));
		return "catalog";
	}

	@GetMapping("/admin/offers")
	public String adminOffers(Model model) {
		String username = SecurityUtil.getSessionUser();
		model.addAttribute("isAdmin", username.equals("admin"));
		adminViewOffers(1, model);
		return "admin-offers-index";
	}

	@GetMapping("/catalog")
	public String catalogCategoryView(
		@RequestParam("categoryId") int categoryId,
		Model model
	) {
		var filteredOffers = offerService.getAllByCategory(categoryId);
		model.addAttribute("offers", filteredOffers.stream().map(OfferDTO::new));
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

	@PostMapping("/search")
	public String search(@RequestParam("searchText") String searchText, Model model) {
		var offers = offerService.searchByName(searchText).stream().map(OfferDTO::new).toList();
		model.addAttribute("offers", offers);
		return "catalog";
	}

	@GetMapping("/page/{pageNo}")
	public String adminViewOffers(@PathVariable int pageNo, Model model) {
		String username = SecurityUtil.getSessionUser();
		if (username == null || !username.equals("admin")) {
			return "redirect:/login";
		}
		int pageSize = 5;
		Page<OfferDTO> page = offerService.findPaginated(pageNo, pageSize).map(OfferDTO::new);
		List<OfferDTO> list = page.getContent();
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("offers", list);
		return "admin-offers-index";
	}

}
