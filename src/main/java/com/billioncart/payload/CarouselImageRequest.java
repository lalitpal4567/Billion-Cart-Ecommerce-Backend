package com.billioncart.payload;

import lombok.Data;

@Data
public class CarouselImageRequest {
	private String imageUrl;
	private String title;
	private Boolean active;
}
