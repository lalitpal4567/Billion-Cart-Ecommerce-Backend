package com.billioncart.payload;

import lombok.Data;

@Data
public class CartItemResponse {
	private Long cartItemId;
	private Long productId;
	private Long cartId;
	private Boolean isSelectedForOrder;
	private String name;
	private String description;
	private String brand;
	private String color;
	private Long quantity;
	private String imageUrl;
	private float currentPrice;
	private float previousPrice;
}
