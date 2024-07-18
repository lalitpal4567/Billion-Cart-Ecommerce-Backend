package com.billioncart.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.billioncart.model.productCatalogue.CategoryImage;
import com.billioncart.model.productCatalogue.SubcategoryImage;
import com.billioncart.payload.CategoryCreatedResponse;
import com.billioncart.payload.CategoryRequest;
import com.billioncart.payload.CategoryResponse;
import com.billioncart.payload.ImageAltText;
import com.billioncart.service.CategoryService;

@RestController
@RequestMapping("/api/v1")
public class CategoryController {
	private CategoryService categoryService;

	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@PostMapping("/admin/category/add-category")
	public ResponseEntity<Map<String, Object>> addCategory(@RequestPart("category") CategoryRequest request,
			@RequestPart("imageFiles") List<MultipartFile> imageFiles) throws IOException {

		Map<String, Object> res = new LinkedHashMap<>();

		try {
			CategoryResponse createdCategory = categoryService.addCategory(request, imageFiles);
			res.put("message", "Category added successfully");
			res.put("Category", createdCategory);
			return ResponseEntity.status(HttpStatus.CREATED).body(res);
		} catch (Exception e) {
			res.put("error", e.getMessage());
			System.out.println(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
		}
	}

	@DeleteMapping("/admin/category/remove-category/{id}")
	public ResponseEntity<Map<String, Object>> removeCategoryById(@PathVariable(name = "id") Long categoryId) {
		Map<String, Object> res = new LinkedHashMap<>();

		try {
			categoryService.removeCategoryById(categoryId);
			res.put("message", "Category removed successfully");
			return ResponseEntity.status(HttpStatus.OK).body(res);
		} catch (Exception e) {
			res.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(res);
		}
	}

	@GetMapping("/noauth/category/get-category/{id}")
	public ResponseEntity<Map<String, Object>> getCategoryById(@PathVariable(name = "id") Long categoryId) {
		Map<String, Object> res = new LinkedHashMap<>();

		try {
			CategoryResponse existingCategory = categoryService.getCategoryById(categoryId);
			res.put("message", "Category found successfully");
			res.put("Category", existingCategory);
			return ResponseEntity.status(HttpStatus.OK).body(res);
		} catch (Exception e) {
			res.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(res);
		}
	}

	
	@PutMapping("/admin/category/update-category/{id}")
	public ResponseEntity<Map<String, Object>> updateCategory(@PathVariable(name = "id") Long categoryId, @RequestBody CategoryRequest request) {
		
		Map<String, Object> res = new LinkedHashMap<>();

		try {
			CategoryResponse updatedCategory = categoryService.updateCategory(categoryId, request);
			res.put("message", "Category upated successfully");
			res.put("Category", updatedCategory);
			return ResponseEntity.status(HttpStatus.OK).body(res);
		} catch (Exception e) {
			res.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
		}
	}

	@GetMapping("/noauth/category/categories-list")
	public Page<CategoryResponse> getCategories(@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "size", defaultValue = "5") Integer size) {
		return categoryService.getAllCategories(page, size);
	}
	
	@GetMapping("/noauth/category/active-categories-list")
	public ResponseEntity<Map<String, Object>> getActiveCategories() {
		Map<String, Object> res = new LinkedHashMap<>();

		try {
			List<CategoryResponse> categoryResponses = categoryService.getActiveCategories();
			res.put("Categories", categoryResponses);
			return ResponseEntity.status(HttpStatus.OK).body(res);
		} catch (Exception e) {
			res.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
		}
	}
	
	@PutMapping("/admin/category/change-category-active-status/{id}")
	public ResponseEntity<Map<String, Object>> changeCategoryActiveStatus(@PathVariable(name = "id") Long categoryId){
	Map<String, Object> res = new LinkedHashMap<>();
		
		try {
			categoryService.changeCategoryActiveStatus(categoryId);
			res.put("message", "Category active status has been changed successfully");
			return ResponseEntity.status(HttpStatus.OK).body(res);
		} catch (Exception e) {
			res.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(res);
		}
	}
	
	@PutMapping("/admin/category/change-category-image-active-status/{id}")
	public ResponseEntity<Map<String, Object>> changeCategoryImageActiveStatus(@PathVariable(name = "id") Long imageId){
	Map<String, Object> res = new LinkedHashMap<>();
		
		try {
			categoryService.changeCategoryImageActiveStatus(imageId);
			res.put("message", "Category image active status has been changed successfully");
			return ResponseEntity.status(HttpStatus.OK).body(res);
		} catch (Exception e) {
			res.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(res);
		}
	}
	
	@PostMapping("/admin/category/add-category-images/{id}")
	public ResponseEntity<Map<String, Object>> addCategoryImages(@PathVariable(name = "id") Long categoryId, @RequestPart("altTexts") List<ImageAltText> altTextList , @RequestPart("imageFiles") List<MultipartFile> imageFiles){
		Map<String, Object> res = new LinkedHashMap<>();
		
		try {
			List<CategoryImage> categoryImages = categoryService.addCategoryImage(categoryId, altTextList , imageFiles);
			res.put("message", "Category images added successfully");
			res.put("Category", categoryImages);
			return ResponseEntity.status(HttpStatus.OK).body(res);
		} catch (Exception e) {
			res.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(res);
		}
	}
	
	@DeleteMapping("/admin/category/remove-category-image/{id}")
	public ResponseEntity<Map<String, Object>> removeCategoryImagesById(@PathVariable(name = "id") Long imageId){
		Map<String, Object> res = new LinkedHashMap<>();
		
		try {
			categoryService.removeCategoryImageById(imageId);
			res.put("message","Category image removed successfully");
			return ResponseEntity.status(HttpStatus.OK).body(res);
		} catch (Exception e) {
			res.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(res);
		}
	}
	
	@GetMapping("/noauth/category/get-category-images/{id}")
	public ResponseEntity<Map<String, Object>> getAllCategoryImages(@PathVariable(name = "id") Long categoryId){
		Map<String, Object> res = new LinkedHashMap<>();
		
		try {
			List<CategoryImage> categoryImages = categoryService.getAllCategoryImages(categoryId);
			res.put("CategoryImages", categoryImages);
			return ResponseEntity.status(HttpStatus.OK).body(res);
		} catch (Exception e) {
			res.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(res);
		}
	}
}
