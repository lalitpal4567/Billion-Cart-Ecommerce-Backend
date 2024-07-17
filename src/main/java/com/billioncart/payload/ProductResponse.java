package com.billioncart.payload;


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
	private float currentPrice;
	private float previousPrice;
	private ColorResponse color;
	private BrandResponse brand;
	private SubcategoryMiniDetailsResponse subcategory;
	private CategoryMiniDetailsResponse category;
	private List<ProductImageResponse> productImages;
	private List<SpecificationResponse> specifications;
}


