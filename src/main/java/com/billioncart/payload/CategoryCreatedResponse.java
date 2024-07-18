package com.billioncart.payload;

import java.util.List;

import lombok.Data;

@Data
public class CategoryCreatedResponse {
	private Long categoryId;
	private String name;
	private String description;
	private List<ImageResponse> images;
}
