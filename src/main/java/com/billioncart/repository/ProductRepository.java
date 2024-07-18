package com.billioncart.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.billioncart.model.productCatalogue.Brand;
import com.billioncart.model.productCatalogue.Product;
import com.billioncart.model.productCatalogue.ProductImage;
import com.billioncart.model.productCatalogue.Subcategory;

public interface ProductRepository extends JpaRepository<Product, Long>{
	Page<Product> findAll(Pageable pageable);
	
	Page<Product>  findAllBySubcategory(Pageable pageable, Subcategory subcategory);
	
	List<Product> findAllBySubcategory(Subcategory subcategory);
	
    List<Brand> findDistinctBrandBySubcategory(Subcategory subcategory);

    
    @Query("SELECT p FROM Product p WHERE p.subcategory.id = :subcategoryId " +
    		 "AND p.active = true " +
            "AND (:brandIds IS NULL OR p.brand.id IN :brandIds) " +
            "AND (:colorIds IS NULL OR p.color.id IN :colorIds)")
     Page<Product> findBySubcategoryAndFilters(@Param("subcategoryId") Long subcategoryId,
                                               @Param("brandIds") List<Long> brandIds,
                                               @Param("colorIds") List<Long> colorIds,
                                               Pageable pageable);
	
}
