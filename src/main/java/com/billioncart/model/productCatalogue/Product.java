package com.billioncart.model.productCatalogue;

import java.util.List;

import com.billioncart.audit.UserDateAudit;
import com.billioncart.model.CartItem;
import com.billioncart.model.Review;
import com.billioncart.model.WishlistItem;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Data
@Table(name = "product_table")
public class Product extends UserDateAudit {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long productId;

	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private int quantity;
	
	@Column(nullable = false)
	private float currentPrice;

	@Column(nullable = false)
	private float previousPrice;
	
	@Column(nullable = false)
	private String description;
	
	@Column(nullable = false)
	private String details;
	
	private String model;
	
	@Column(nullable = false)
	private Boolean active;
	
	@ManyToOne
	@JoinColumn(name = "subcategory_id")
	private Subcategory subcategory;
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<CartItem> cartItems;
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<WishlistItem> wishlistItems;

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Review> reviews;

	@ManyToOne
	@JoinColumn(name = "brand_id")
	 private Brand brand;
	
	@ManyToOne
	@JoinColumn(name = "color_id")
	private Color color;
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<SpecificationValue> specificationValues;
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProductImage> productImages;
}
