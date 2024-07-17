package com.billioncart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.billioncart.model.productCatalogue.Subcategory;
import com.billioncart.model.productCatalogue.SubcategoryImage;

public interface SubcategoryImageRepository extends JpaRepository<SubcategoryImage, Long>{
	List<SubcategoryImage> findAllBySubcategory(Subcategory subcategory);
}
