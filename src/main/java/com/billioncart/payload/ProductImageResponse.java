package com.billioncart.payload;

import lombok.Data;

@Data
public class ProductImageResponse {
	private Long imageUrlId;
	private String imageUrl;
	private String altText;
}
