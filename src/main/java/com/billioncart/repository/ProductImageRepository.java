package com.billioncart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.billioncart.model.productCatalogue.Product;
import com.billioncart.model.productCatalogue.ProductImage;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long>{
	List<ProductImage> findAllByProduct(Product product);
}
