package com.billioncart.payload;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class CategoryResponse {
	private Long categoryId;
	private String name;
	private String description;
	private Boolean active;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private String createdBy;
	private String updatedBy;
	private List<ImageResponse> images;
	private List<SubcategoryMiniDetailsResponse> subcategories;
}