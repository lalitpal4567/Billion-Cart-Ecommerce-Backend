package com.billioncart.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.billioncart.payload.CategoryRequest;
import com.billioncart.payload.CategoryResponse;
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
			@RequestPart("imageFile") MultipartFile imageFile) throws IOException {

		Map<String, Object> res = new LinkedHashMap<>();

		try {
			CategoryResponse createdCategory = categoryService.addCategory(request, imageFile);
			res.put("message", "Category added successfully");
			res.put("Category", createdCategory);
			return ResponseEntity.status(HttpStatus.CREATED).body(res);
		} catch (Exception e) {
			res.put("error", e.getMessage());
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
	public ResponseEntity<Map<String, Object>> updateCategory(@PathVariable(name = "id") Long categoryId, @RequestPart("category") CategoryRequest request,
			@RequestPart("imageFile") MultipartFile imageFile) throws IOException {
		
		Map<String, Object> res = new LinkedHashMap<>();

		try {
			CategoryResponse updatedCategory = categoryService.updateCategory(categoryId, request, imageFile);
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
}
