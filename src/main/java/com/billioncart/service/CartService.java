package com.billioncart.service;

import java.util.List;

import com.billioncart.payload.CartItemResponse;

public interface CartService {
	void addToCart(Long productId);
	
	void changeCartItemQuantity(Long quantity, Long cartItemId);
	
	List<CartItemResponse> getCartItemResponse();
	
	void toggleCartItemForOrder(Long cartItemId);
	
	void removeCartItem(Long cartItem);
}
