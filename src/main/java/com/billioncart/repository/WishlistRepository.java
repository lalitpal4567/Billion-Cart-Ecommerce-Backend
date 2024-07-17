package com.billioncart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.billioncart.model.Wishlist;

public interface WishlistRepository  extends JpaRepository<Wishlist, Long>{

}
