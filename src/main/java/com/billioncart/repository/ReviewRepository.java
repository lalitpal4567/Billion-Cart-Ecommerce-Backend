package com.billioncart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.billioncart.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Long>{

}
