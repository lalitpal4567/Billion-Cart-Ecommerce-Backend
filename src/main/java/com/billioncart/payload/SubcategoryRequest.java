package com.billioncart.payload;

import java.util.List;

import lombok.Data;

@Data
public class SubcategoryRequest {
	private String name;
	private String description;
	private List<ImageAltText> altTexts;
}
