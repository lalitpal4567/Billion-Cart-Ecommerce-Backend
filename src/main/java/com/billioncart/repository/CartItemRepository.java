package com.billioncart.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.billioncart.model.Cart;
import com.billioncart.model.CartItem;
import com.billioncart.model.productCatalogue.Product;

public interface CartItemRepository extends JpaRepository<CartItem, Long>{
	Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
	
	Optional<CartItem> findByCartItemIdAndCart(Long cartItemId, Cart cart);
}
