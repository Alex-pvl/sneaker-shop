package ru.nstu.ap.model.catalog;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(schema = "catalog", name = "offers")
public class Offer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotNull
	@NotBlank
	private String name;

	@Nullable
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_brand")
	private Brand brand;

	@Nullable
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_category")
	private Category category;

	@NotNull
	private Double price;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
		schema = "catalog",
		name = "offer_size",
		joinColumns = @JoinColumn(name = "id_offer"),
		inverseJoinColumns = @JoinColumn(name = "id_size")
	)
	private List<OfferSize> sizes = new ArrayList<>();

	@NotNull
	private Integer quantity;

	@NotNull
	@NotBlank
	@Column(name = "image_url", nullable = false)
	private String imageUrl;

	@NotNull
	@Column(name = "is_available")
	private boolean available;

	public void incrementQuantity(int i) {
		quantity+=i;
	}

	public void decrementQuantity(int i) {
		quantity-=i;
		this.available = this.quantity > 0;
	}
}
