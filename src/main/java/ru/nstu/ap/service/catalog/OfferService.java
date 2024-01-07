package ru.nstu.ap.service.catalog;

import lombok.AllArgsConstructor;
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
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class OfferService {
	private OfferRepository offerRepository;
	private OfferSizeRepository offerSizeRepository;

	@Transactional(readOnly = true)
	public <T> List<T> getOffers(Predicate<Offer> predicate, Function<Offer, T> mapper) {
		return this.getAll().stream()
				.filter(predicate)
				.map(mapper)
				.toList();
	}

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
	public Page<Offer> findPaginated(int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		return offerRepository.findAll(pageable);
	}

	@Transactional
	public void deleteById(Integer id) {
		offerRepository.deleteById(id);
	}
}
