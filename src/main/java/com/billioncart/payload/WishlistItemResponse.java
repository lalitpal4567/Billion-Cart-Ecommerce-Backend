package com.billioncart.payload;

import lombok.Data;

@Data
public class WishlistItemResponse {
	private Long wishlistItemId;
	private Long wishlistId;
	private Long productId;
	private String name;
	private String description;
	private Long quantity;
	private String imageUrl;
	private float currentPrice;
	private float previousPrice;
	SubcategoryMiniDetailsResponse subcategory;
}
