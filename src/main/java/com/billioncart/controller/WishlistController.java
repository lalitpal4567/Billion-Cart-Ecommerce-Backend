package com.billioncart.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.billioncart.payload.ProductResponse;
import com.billioncart.payload.WishlistItemResponse;
import com.billioncart.service.WishlistService;

@RestController
@RequestMapping("/api/v1/user/wishlist")
public class WishlistController {
	private WishlistService wishlistService;
	
	public WishlistController(WishlistService wishlistService) {
		this.wishlistService = wishlistService;
	}
	
	@PostMapping("/add-wishlist-item/{id}")
	public ResponseEntity<Map<String, Object>> addToCart(@PathVariable(name = "id") Long productId) {
		Map<String, Object> res = new LinkedHashMap<>();

		try {
			wishlistService.addToWishlist(productId);
			res.put("message", "Wishlist item added successfully");
			return ResponseEntity.status(HttpStatus.CREATED).body(res);
		} catch (Exception e) {
			res.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(res);
		}
	}
	
	@DeleteMapping("/remove-wishlist-item/{id}")
	public ResponseEntity<Map<String, Object>> removeFromWishlist(@PathVariable(name = "id") Long wishlistItemId) {
		Map<String, Object> res = new LinkedHashMap<>();

		try {
			wishlistService.removeFromWishlist(wishlistItemId);
			res.put("message", "Wishlist item removed successfully");
			return ResponseEntity.status(HttpStatus.OK).body(res);
		} catch (Exception e) {
			res.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(res);
		}
	}
	
	@GetMapping("/wishlist-items")
	public Page<WishlistItemResponse> getAllWishlistItems(@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "size", defaultValue = "10") Integer size) {

		return wishlistService.getAllWishlistItems(page, size);
	}
}
