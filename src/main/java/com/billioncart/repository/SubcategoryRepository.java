package com.billioncart.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.billioncart.model.productCatalogue.Category;
import com.billioncart.model.productCatalogue.Subcategory;

public interface SubcategoryRepository extends JpaRepository<Subcategory, Long>{
	Optional<Subcategory> findByNameIgnoreCase(String query);
	
	Page<Subcategory> findAllByNameContaining(String name, Pageable pageable);
	
	Page<Subcategory> findAll(Pageable pageable);
	
	Page<Subcategory> findAllByCategory(Pageable pageable, Category category);
	
	List<Subcategory> findAllByCategory(Category category);
	
	List<Subcategory> findByCategoryAndActiveTrue(Category category);

}
