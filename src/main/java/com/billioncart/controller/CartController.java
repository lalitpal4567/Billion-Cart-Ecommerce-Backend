package com.billioncart.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.billioncart.payload.CartItemResponse;
import com.billioncart.service.CartService;

@RestController
@RequestMapping("/api/v1/user/cart")
public class CartController {
	private CartService cartService;
	
	public CartController(CartService cartService) {
		this.cartService = cartService;
	}
	
	@PostMapping("/add-product-to-cart/{id}")
	public ResponseEntity<Map<String, Object>> addToCart(@PathVariable(name = "id") Long productId) {
		System.out.println("adding product to cart");
		Map<String, Object> res = new LinkedHashMap<>();

		try {
			cartService.addToCart(productId);
			res.put("message", "CartItem added successfully");
			return ResponseEntity.status(HttpStatus.CREATED).body(res);
		} catch (Exception e) {
			res.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(res);
		}
	}
	
	@PutMapping("/cart-item-quantity/{id}")
	public ResponseEntity<Map<String, Object>> changeCartItemQuantity(@PathVariable(name = "id") Long cartItemId, @RequestParam(name = "quantity") Long quantity) {
		Map<String, Object> res = new LinkedHashMap<>();

		try {
			cartService.changeCartItemQuantity(quantity , cartItemId);
			res.put("message", "CartItem quantity increased successfully");
			return ResponseEntity.status(HttpStatus.OK).body(res);
		} catch (Exception e) {
			res.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(res);
		}
	}
	
	@GetMapping("/cartItems")
	public ResponseEntity<Map<String, Object>> getCartItems() {
		Map<String, Object> res = new LinkedHashMap<>();

		try {
			List<CartItemResponse> cartItemResponses = cartService.getCartItemResponse();
			res.put("CartItems", cartItemResponses);
			return ResponseEntity.status(HttpStatus.OK).body(res);
		} catch (Exception e) {
			res.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(res);
		}
	}
	
	@PutMapping("/toggle-cartItem-for-order/{id}")
	public ResponseEntity<Map<String, Object>> toggleCartItemForOrder(@PathVariable(name = "id") Long cartItemId) {
		Map<String, Object> res = new LinkedHashMap<>();

		try { 
			cartService.toggleCartItemForOrder(cartItemId);
			res.put("message", "CartItem marked or unmarked for order successfully");
			return ResponseEntity.status(HttpStatus.OK).body(res);
		} catch (Exception e) {
			res.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(res);
		}
	}
	
	@DeleteMapping("/remove-cart-item/{id}")
	public ResponseEntity<Map<String, Object>> removeCartItem(@PathVariable(name = "id") Long cartItemId) {
		Map<String, Object> res = new LinkedHashMap<>();

		try { 
			cartService.removeCartItem(cartItemId);
			res.put("message", "CartItem removed successfully");
			return ResponseEntity.status(HttpStatus.OK).body(res);
		} catch (Exception e) {
			res.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(res);
		}
	}
}
