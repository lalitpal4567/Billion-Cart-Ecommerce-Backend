package com.billioncart.payload;

import java.time.LocalDateTime;
import java.util.List;

import com.billioncart.model.productCatalogue.Category;
import com.billioncart.model.productCatalogue.SubcategoryImage;

import lombok.Data;

@Data
public class SubcategoryResponse {
	private Long subcategoryId;
	private String name;
	private String description;
	private Boolean active;
	private CategoryMiniDetailsResponse category;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private String createdBy;
	private String updatedBy;
	private List<SubcategoryImage> subcategoryImages;
	private List<SpecificationNameResponse> specificationName;
}
