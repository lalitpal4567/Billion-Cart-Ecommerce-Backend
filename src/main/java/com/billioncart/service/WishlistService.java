package com.billioncart.service;

import org.springframework.data.domain.Page;

import com.billioncart.payload.ProductResponse;
import com.billioncart.payload.WishlistItemResponse;

public interface WishlistService {
	void addToWishlist(Long productId);
	
	void removeFromWishlist(Long wishlistItemId);
		
	Page<WishlistItemResponse> getAllWishlistItems(Integer page, Integer size);
}
