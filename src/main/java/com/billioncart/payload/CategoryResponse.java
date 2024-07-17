package com.billioncart.payload;

import java.util.List;

import lombok.Data;

@Data
public class CategoryResponse {
	private Long categoryId;
	private String name;
	private String description;
	private String imageUrl;
	private String altText;
	private List<SubcategoryMiniDetailsResponse> subcategories;
}