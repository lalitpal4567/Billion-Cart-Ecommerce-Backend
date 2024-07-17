package com.billioncart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.billioncart.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{

}
