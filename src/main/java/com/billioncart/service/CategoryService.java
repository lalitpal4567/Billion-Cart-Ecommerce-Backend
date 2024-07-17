package com.billioncart.service;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.billioncart.model.productCatalogue.Category;
import com.billioncart.payload.CategoryRequest;
import com.billioncart.payload.CategoryResponse;

public interface CategoryService {
	CategoryResponse addCategory(CategoryRequest request, MultipartFile imageFile) throws IOException;
	
	void removeCategoryById(Long categoryId);
	
	CategoryResponse updateCategory(Long categoryId, CategoryRequest request, MultipartFile imageFile);
	
	CategoryResponse getCategoryById(Long categoryId);
		
	Page<CategoryResponse> getAllCategories(Integer page, Integer size);
	
}
