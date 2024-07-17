package com.billioncart.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.billioncart.model.Wishlist;
import com.billioncart.model.WishlistItem;
import com.billioncart.model.productCatalogue.Product;

public interface WishlistItemRepository extends JpaRepository<WishlistItem, Long>{
	Optional<WishlistItem> findWishlistItemByWishlistAndProduct(Wishlist wishlist, Product product);
	
	Optional<WishlistItem> findByWishlistItemIdAndWishlist(Long wishlistItemId, Wishlist wishlist);
	
	Page<WishlistItem> findAllByWishlist(Pageable pageable, Wishlist wishlist);
}
