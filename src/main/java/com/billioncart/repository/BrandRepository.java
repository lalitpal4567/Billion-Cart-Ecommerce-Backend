package com.billioncart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.billioncart.model.productCatalogue.Brand;

public interface BrandRepository extends JpaRepository<Brand, Long>{

}
