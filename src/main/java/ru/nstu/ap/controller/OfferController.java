package ru.nstu.ap.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.nstu.ap.dto.catalog.BrandDTO;
import ru.nstu.ap.dto.catalog.CategoryDTO;
import ru.nstu.ap.dto.catalog.OfferDTO;
import ru.nstu.ap.model.catalog.Offer;
import ru.nstu.ap.service.catalog.BrandService;
import ru.nstu.ap.service.catalog.CategoryService;
import ru.nstu.ap.service.catalog.OfferService;
import ru.nstu.ap.utils.SecurityUtil;

import java.util.List;
import java.util.function.Predicate;

@Controller
@AllArgsConstructor
public class OfferController {
	private OfferService offerService;
	private BrandService brandService;
	private CategoryService categoryService;

	@GetMapping("/")
	public String catalogView(Model model) {
		boolean isAdmin = SecurityUtil.isAdmin();
		var offers = offerService.getOffers(OfferDTO::new);

		model.addAttribute("isAdmin", isAdmin);
		model.addAttribute("offers", offers);
		return "catalog";
	}

	@GetMapping("/catalog")
	public String catalogCategoryView(
		@RequestParam("categoryId") int categoryId,
		Model model
	) {
		boolean isAdmin = SecurityUtil.isAdmin();
		Predicate<Offer> isCategoryMatch = offer -> offer.getCategory() != null &&
				offer.getCategory().getId() == categoryId;
		var filteredOffers = offerService.getOffers(isCategoryMatch, OfferDTO::new);

		model.addAttribute("isAdmin", isAdmin);
		model.addAttribute("offers", filteredOffers);
		return "catalog";
	}

	@GetMapping("/offers")
	public List<OfferDTO> getAll() {
		return offerService.getOffers(OfferDTO::new);
	}

	@GetMapping("/offers/{id}")
	public String offerDetailsView(@PathVariable Integer id, Model model) {
		boolean isAdmin = SecurityUtil.isAdmin();
		var offer = offerService.getById(id);

		model.addAttribute("isAdmin", isAdmin);
		model.addAttribute("offer", new OfferDTO(offer));
		return "offer_details";
	}

	@PostMapping("/offers/{id}")
	public OfferDTO getById(@PathVariable Integer id) {
		return new OfferDTO(offerService.getById(id));
	}

	@PostMapping("/search")
	public String search(@RequestParam("searchText") String searchText, Model model) {
		boolean isAdmin = SecurityUtil.isAdmin();
		Predicate<Offer> isNameEquals = offer ->
				offer.getName().toLowerCase().contains(searchText.toLowerCase());
		var offers = offerService.getOffers(isNameEquals, OfferDTO::new);

		model.addAttribute("isAdmin", isAdmin);
		model.addAttribute("offers", offers);
		return "catalog";
	}

	/** -------------- Admin pages -------------- */

	@GetMapping("/admin/offers")
	public String adminOffers(Model model) {
		if (!SecurityUtil.isAdmin()) return "redirect:/";
		adminViewOffers(1, model);
		return "admin/offers";
	}

	@GetMapping("/offers/page/{pageNo}")
	public String adminViewOffers(@PathVariable int pageNo, Model model) {
		if (!SecurityUtil.isAdmin()) return "redirect:/";
		Page<OfferDTO> page = offerService.findPaginated(pageNo, 5).map(OfferDTO::new);
		List<OfferDTO> list = page.getContent();

		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("offers", list);
		return "admin/offers";
	}

	@GetMapping("/admin/addOffer")
	public String addOfferView(Model model) {
		var offer = new Offer();
		var brands = brandService.getBrands(BrandDTO::new);
		var categories = categoryService.getCategories(CategoryDTO::new);

		model.addAttribute("offer", offer);
		model.addAttribute("brands", brands);
		model.addAttribute("categories", categories);
		return "admin/offers_new";
	}

	@PostMapping("/admin/addOffer")
	public String addOffer(@ModelAttribute("offer") Offer offer) {
		offerService.create(offer);
		return "redirect:/admin/offers";
	}

	@GetMapping("/admin/updateOffer/{id}")
	public String updateOfferView(@PathVariable int id, Model model) {
		var offer = offerService.getById(id);
		var brands = brandService.getBrands(BrandDTO::new);
		var categories = categoryService.getCategories(CategoryDTO::new);

		model.addAttribute("offer", offer);
		model.addAttribute("brands", brands);
		model.addAttribute("categories", categories);
		return "admin/offers_update";
	}

	@GetMapping("/admin/deleteOffer/{id}")
	public String deleteOffer(@PathVariable int id) {
		offerService.deleteById(id);
		return "redirect:/admin/offers";
	}
}
