package com.billioncart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.billioncart.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Long>{

}
