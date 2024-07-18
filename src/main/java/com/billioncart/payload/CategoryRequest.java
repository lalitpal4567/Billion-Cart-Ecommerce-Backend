package com.billioncart.payload;

import java.util.List;

import lombok.Data;

@Data
public class CategoryRequest {
	private String name;
	private String description;
	private List<ImageAltText> altTexts;
}
