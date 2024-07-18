package com.billioncart.payload;


import java.time.LocalDateTime;
import java.util.List;

import com.billioncart.model.productCatalogue.Brand;
import com.billioncart.model.productCatalogue.Color;

import lombok.Data;

@Data
public class ProductResponse {
	private Long productId;
	private String name;
	private String description;
	private String details;
	private int quantity;
	private String model;
	private Boolean active;
	private float price;
	private Float offer;
	private LocalDateTime offerEndDate;
	private ColorResponse color;
	private BrandResponse brand;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private String createdBy;
	private String updatedBy;
	private SubcategoryMiniDetailsResponse subcategory;
	private CategoryMiniDetailsResponse category;
	private List<ProductImageResponse> productImages;
	private List<SpecificationResponse> specifications;
}


