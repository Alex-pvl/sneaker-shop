package ru.nstu.ap.service.catalog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nstu.ap.model.catalog.Offer;
import ru.nstu.ap.model.catalog.OfferSize;
import ru.nstu.ap.repository.catalog.OfferRepository;
import ru.nstu.ap.repository.catalog.OfferSizeRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

@Service
public class OfferService {
	@Autowired
	private OfferRepository offerRepository;
	@Autowired
	private OfferSizeRepository offerSizeRepository;
	@Autowired
	private BrandService brandService;
	@Autowired
	private CategoryService categoryService;

	@Transactional(readOnly = true)
	public <T> List<T> getOffers(Function<Offer, T> mapper) {
		return this.getAll().stream().map(mapper).toList();
	}

	public List<Offer> getAll() {
		return offerRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Offer getById(Integer id) {
		return offerRepository.findById(id)
			.orElseThrow(NoSuchElementException::new);
	}

	@Transactional(readOnly = true)
	public List<Offer> searchByName(String searchText) {
		return getAll().stream()
			.filter(o ->
				o.getName().toLowerCase().contains(searchText.toLowerCase())
			)
			.toList();
	}

	public void create(Offer offer) {
		offer.setSizes(Stream.of(39, 40, 41, 42, 43, 44, 45, 46).map(s -> {
			var os = new OfferSize();
			os.setSize(s);
			offerSizeRepository.save(os);
			return os;
		}).toList());
		offerRepository.save(offer);
	}

	@Transactional(readOnly = true)
	public List<Offer> getAllByCategory(Integer categoryId) {
		return getAll().stream()
			.filter(o ->
				Objects.requireNonNull(o.getCategory()).getId().equals(categoryId)
			)
			.toList();
	}

	@Transactional(readOnly = true)
	public Page<Offer> findPaginated(int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		return offerRepository.findAll(pageable);
	}

	@Transactional
	public void deleteById(Integer id) {
		offerRepository.deleteById(id);
	}
}
