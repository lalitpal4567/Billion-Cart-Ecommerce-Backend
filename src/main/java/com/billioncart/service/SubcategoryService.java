package com.billioncart.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.billioncart.model.productCatalogue.Category;
import com.billioncart.model.productCatalogue.SubcategoryImage;
import com.billioncart.payload.ImageAltText;
import com.billioncart.payload.ImageFileWithMetadata;
import com.billioncart.payload.SubcategoryRequest;
import com.billioncart.payload.SubcategoryResponse;

public interface SubcategoryService {
	SubcategoryResponse addSubcategory(Long categoryId, SubcategoryRequest request, List<MultipartFile> imageFiles);
	
	void removeSubcategoryById(Long subcategoryId);
	
	SubcategoryResponse updateSubcategory(Long subcategoryId, SubcategoryRequest request);
	
	List<SubcategoryImage> addSubcategoryImage(Long subcategoryId, List<ImageAltText> altTextList, List<MultipartFile> imageFiles);
	
	SubcategoryResponse getSubcategoryById(Long subcategoryId);
	
	List<SubcategoryImage> getAllSubcategoryImages(Long subcategoryId);
	
	void removeSubcategoryImageById(Long imageId);
		
	Page<SubcategoryResponse> getAllSubcategories(Integer page, Integer size);
	
	List<SubcategoryResponse> getActiveSubcategoriesByCategoryId(Long categoryId);
	
	void changeSubcategoryActiveStatus(Long subcategoryId);
	
	void changeSubcategoryImageActiveStatus(Long imageId);
	
	Page<SubcategoryResponse> getSubcategoriesByCategoryId(Integer page, Integer size, Long categoryId);
	
	Page<SubcategoryResponse> getsubcategoriesByQuery(Integer page, Integer size, String query);
}
