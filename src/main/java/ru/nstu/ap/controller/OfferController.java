package ru.nstu.ap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.nstu.ap.dto.catalog.BrandDTO;
import ru.nstu.ap.dto.catalog.CategoryDTO;
import ru.nstu.ap.dto.catalog.OfferDTO;
import ru.nstu.ap.model.catalog.Offer;
import ru.nstu.ap.model.catalog.OfferSize;
import ru.nstu.ap.repository.catalog.OfferSizeRepository;
import ru.nstu.ap.service.catalog.BrandService;
import ru.nstu.ap.service.catalog.CategoryService;
import ru.nstu.ap.service.catalog.OfferService;
import ru.nstu.ap.utils.SecurityUtil;

import java.util.List;
import java.util.stream.Stream;

@Controller
public class OfferController {
	@Autowired
	private OfferService offerService;
	@Autowired
	private BrandService brandService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private OfferSizeRepository offerSizeRepository;

	@GetMapping("/")
	public String catalogView(Model model) {
		String username = SecurityUtil.getSessionUser();
		boolean isAdmin = username != null && username.equals("admin");
		model.addAttribute("isAdmin", isAdmin);
		model.addAttribute("offers", offerService.getOffers(OfferDTO::new));
		return "catalog";
	}

	@GetMapping("/catalog")
	public String catalogCategoryView(
		@RequestParam("categoryId") int categoryId,
		Model model
	) {
		String username = SecurityUtil.getSessionUser();
		boolean isAdmin = username != null && username.equals("admin");
		var filteredOffers = offerService.getAllByCategory(categoryId);
		model.addAttribute("isAdmin", isAdmin);
		model.addAttribute("offers", filteredOffers.stream().map(OfferDTO::new));
		return "catalog";
	}

	@GetMapping("/offers")
	public List<OfferDTO> getAll() {
		return offerService.getOffers(OfferDTO::new);
	}

	@GetMapping("/offers/{id}")
	public String offerDetailsView(@PathVariable Integer id, Model model) {
		String username = SecurityUtil.getSessionUser();
		boolean isAdmin = username != null && username.equals("admin");
		var offer = new OfferDTO(offerService.getById(id));
		model.addAttribute("isAdmin", isAdmin);
		model.addAttribute("offer", offer);
		return "offer-details";
	}

	@PostMapping("/offers/{id}")
	public OfferDTO getById(@PathVariable Integer id, @RequestBody Integer size) {
		return new OfferDTO(offerService.getById(id));
	}

	@PostMapping("/search")
	public String search(@RequestParam("searchText") String searchText, Model model) {
		String username = SecurityUtil.getSessionUser();
		boolean isAdmin = username != null && username.equals("admin");
		var offers = offerService.searchByName(searchText).stream().map(OfferDTO::new).toList();
		model.addAttribute("isAdmin", isAdmin);
		model.addAttribute("offers", offers);
		return "catalog";
	}

	@GetMapping("/admin/offers")
	public String adminOffers(Model model) {
		String username = SecurityUtil.getSessionUser();
		if (username == null || !username.equals("admin")) return "redirect:/";
		adminViewOffers(1, model);
		return "admin-offers-index";
	}

	@GetMapping("/offers/page/{pageNo}")
	public String adminViewOffers(@PathVariable int pageNo, Model model) {
		String username = SecurityUtil.getSessionUser();
		if (username == null || !username.equals("admin")) {
			return "redirect:/";
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

	@GetMapping("/admin/addOffer")
	public String addOfferView(Model model) {
		Offer offer = new Offer();
		var brands = brandService.getBrands(BrandDTO::new);
		var categories = categoryService.getCategories(CategoryDTO::new);
		model.addAttribute("offer", offer);
		model.addAttribute("brands", brands);
		model.addAttribute("categories", categories);
		return "admin-offers-new";
	}

	@PostMapping("/admin/addOffer")
	public String addOffer(@ModelAttribute("offer") Offer offer) {
		offerService.create(offer);
		return "redirect:/admin/offers";
	}

	@GetMapping("/admin/updateOffer/{id}")
	public String updateOfferView(@PathVariable int id, Model model) {
		Offer offer = offerService.getById(id);
		var brands = brandService.getBrands(BrandDTO::new);
		var categories = categoryService.getCategories(CategoryDTO::new);
		model.addAttribute("offer", offer);
		model.addAttribute("brands", brands);
		model.addAttribute("categories", categories);
		return "admin-offers-update";
	}

	@GetMapping("/admin/deleteOffer/{id}")
	public String deleteOffer(@PathVariable int id) {
		offerService.deleteById(id);
		return "redirect:/admin/offers";
	}
}
