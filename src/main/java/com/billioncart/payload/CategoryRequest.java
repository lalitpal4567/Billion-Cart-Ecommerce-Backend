package com.billioncart.payload;

import lombok.Data;

@Data
public class CategoryRequest {
	private String name;
	private String description;
	private String altText;
}
