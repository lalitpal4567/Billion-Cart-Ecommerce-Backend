package com.billioncart.service;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.billioncart.model.productCatalogue.Category;
import com.billioncart.model.productCatalogue.CategoryImage;
import com.billioncart.payload.CategoryCreatedResponse;
import com.billioncart.payload.CategoryRequest;
import com.billioncart.payload.CategoryResponse;
import com.billioncart.payload.ImageAltText;

public interface CategoryService {
	CategoryResponse addCategory(CategoryRequest request, List<MultipartFile> imageFiles) throws IOException;
	
	void removeCategoryById(Long categoryId);
	
	CategoryResponse updateCategory(Long categoryId, CategoryRequest request);
	
	CategoryResponse getCategoryById(Long categoryId);
	
	void removeCategoryImageById(Long imageId);
	
//	Page<CategoryResponse> getActiveCategories(Integer page, Integer size);
	List<CategoryResponse> getActiveCategories();
		
	Page<CategoryResponse> getAllCategories(Integer page, Integer size);
	
	void changeCategoryActiveStatus(Long categoryId);
	
	void changeCategoryImageActiveStatus(Long imageId);
	
	List<CategoryImage> getAllCategoryImages(Long categoryId);
	
	 List<CategoryImage> addCategoryImage(Long categoryId, List<ImageAltText> altTextList, List<MultipartFile> imageFiles);
	
}
