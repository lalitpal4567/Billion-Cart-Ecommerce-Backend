package com.billioncart.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.billioncart.model.CarouselImage;

public interface CarouselImageRepository extends JpaRepository<CarouselImage, Long>{
	Page<CarouselImage> findByActive(Boolean active, Pageable pageable);
}
