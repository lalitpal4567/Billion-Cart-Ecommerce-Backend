package com.billioncart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.billioncart.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>{

}
