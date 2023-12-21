package ru.nstu.ap.service.catalog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nstu.ap.model.catalog.Brand;
import ru.nstu.ap.repository.catalog.BrandRepository;

import java.util.List;
import java.util.function.Function;

@Service
public class BrandService {
	@Autowired
	private BrandRepository brandRepository;

	@Transactional(readOnly = true)
	public <T> List<T> getBrands(Function<Brand, T> mapper) {
		return this.getAll().stream().map(mapper).toList();
	}

	public List<Brand> getAll() {
		return brandRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Brand getById(Integer id) {
		return brandRepository.findBrandById(id);
	}

	@Transactional
	public void create(Brand brand) {
		brandRepository.save(brand);
	}

	@Transactional
	public Brand update(Integer id, String name) {
		var brand = getById(id);
		brand.setName(name);
		return brandRepository.save(brand);
	}

	@Transactional
	public void deleteById(Integer id) {
		brandRepository.deleteById(id);
	}

	@Transactional(readOnly = true)
	public Page<Brand> findPaginated(int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		return brandRepository.findAll(pageable);
	}
}
