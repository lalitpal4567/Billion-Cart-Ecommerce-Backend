package com.billioncart.payload;

import lombok.Data;

@Data
public class ImageResponse {
	private Long imageUrlId;
	private String imageUrl;
	private String altText;
}
