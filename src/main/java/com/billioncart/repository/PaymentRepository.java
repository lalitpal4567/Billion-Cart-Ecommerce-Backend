package com.billioncart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.billioncart.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long>{

}
