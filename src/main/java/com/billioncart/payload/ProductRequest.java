package com.billioncart.payload;

import java.util.List;

import com.billioncart.model.productCatalogue.Color;
import com.billioncart.model.productCatalogue.ProductImage;

import lombok.Data;

@Data
public class ProductRequest {
	private String name;
	private String description;
	private String details;
	private Long brandId;
	private Long colorId;
	private float currentPrice;
	private float previousPrice;
	private String model;
	private int quantity;
	private List<SpecificationValueRequest> specificationValues;
	private List<ImageAltText> altTexts;
}
