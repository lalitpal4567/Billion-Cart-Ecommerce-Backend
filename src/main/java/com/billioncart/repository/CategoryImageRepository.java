package com.billioncart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.billioncart.model.productCatalogue.Category;
import com.billioncart.model.productCatalogue.CategoryImage;

public interface CategoryImageRepository extends JpaRepository<CategoryImage, Long>{
	List<CategoryImage> findAllByCategory(Category category);
}
