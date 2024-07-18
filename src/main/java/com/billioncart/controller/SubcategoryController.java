package com.billioncart.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.aop.target.LazyInitTargetSource;
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

import com.billioncart.model.productCatalogue.SubcategoryImage;
import com.billioncart.payload.ImageAltText;
import com.billioncart.payload.ImageFileWithMetadata;
import com.billioncart.payload.SubcategoryRequest;
import com.billioncart.payload.SubcategoryResponse;
import com.billioncart.service.SubcategoryService;

@RestController
@RequestMapping("/api/v1")
public class SubcategoryController {
	private SubcategoryService subcategoryService;

	public SubcategoryController(SubcategoryService subcategoryService) {
		this.subcategoryService = subcategoryService;
	}

	@PostMapping("/admin/subcategory/add-subcategory/{id}")
	public ResponseEntity<Map<String, Object>> addSubcategory(@PathVariable(name = "id") Long categoryid,
			@RequestPart("subcategory") SubcategoryRequest request,
			@RequestPart("imageFiles") List<MultipartFile> imageFiles) {
		System.out.println(request);
		Map<String, Object> res = new LinkedHashMap<>();

		try {
			SubcategoryResponse createdSubcateogry = subcategoryService.addSubcategory(categoryid, request, imageFiles);
			res.put("message", "Subcategory added successfully");
			res.put("Subcategory", createdSubcateogry);
			return ResponseEntity.status(HttpStatus.CREATED).body(res);
		} catch (Exception e) {
			res.put("error", e.getMessage());
			System.out.println(e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(res);
		}
	}

	
	
	
	@DeleteMapping("/admin/subcategory/remove-subcategory/{id}")
	public ResponseEntity<Map<String, Object>> removeSubcategoryById(@PathVariable(name = "id") Long subcategoryId) {
		Map<String, Object> res = new LinkedHashMap<>();

		try {
			subcategoryService.removeSubcategoryById(subcategoryId);
			res.put("message", "Subcategory removed successfully");
			return ResponseEntity.status(HttpStatus.OK).body(res);
		} catch (Exception e) {
			res.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(res);
		}
	}

	
	
	
	@PutMapping("/admin/subcategory/update-subcategory/{id}")
	public ResponseEntity<Map<String, Object>> updateSubcategory(@PathVariable(name = "id") Long subcategoryId,
			@RequestBody SubcategoryRequest request) {
		Map<String, Object> res = new LinkedHashMap<>();

		try {
			SubcategoryResponse updatedSubcategory = subcategoryService.updateSubcategory(subcategoryId, request);
			res.put("message", "Subcategory updated successfully");
			res.put("Subcategory", updatedSubcategory);
			return ResponseEntity.status(HttpStatus.OK).body(res);
		} catch (Exception e) {
			res.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(res);
		}
	}

	
	
	
	@PostMapping("/admin/subcategory/add-subcategory-images/{id}")
	public ResponseEntity<Map<String, Object>> addSubcategoryImages(@PathVariable(name = "id") Long subcategoryId,
			@RequestPart("altTexts") List<ImageAltText> altTextList,
			@RequestPart("imageFiles") List<MultipartFile> imageFiles) {

		Map<String, Object> res = new LinkedHashMap<>();

		try {
			List<SubcategoryImage> subcategoryImages = subcategoryService.addSubcategoryImage(subcategoryId,
					altTextList, imageFiles);
			res.put("message", "Subcategory images added successfully");
			res.put("Subcategory", subcategoryImages);
			return ResponseEntity.status(HttpStatus.OK).body(res);
		} catch (Exception e) {
			res.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(res);
		}
	}

	
	
	
	@DeleteMapping("/admin/subcategory/remove-subcategory-image/{id}")
	public ResponseEntity<Map<String, Object>> removeSubcategoryImagesById(@PathVariable(name = "id") Long imageId) {
		Map<String, Object> res = new LinkedHashMap<>();

		try {
			subcategoryService.removeSubcategoryImageById(imageId);
			res.put("message", "Subcategory image removed successfully");
			return ResponseEntity.status(HttpStatus.OK).body(res);
		} catch (Exception e) {
			res.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(res);
		}
	}

	
	
	
	@GetMapping("/noauth/subcategory/get-subcategory/{id}")
	public ResponseEntity<Map<String, Object>> getSubcategoryById(@PathVariable(name = "id") Long subcategoryId) {
		Map<String, Object> res = new LinkedHashMap<>();

		try {
			SubcategoryResponse existingSubcategory = subcategoryService.getSubcategoryById(subcategoryId);
			res.put("message", "Subcategory found successfully");
			res.put("Subcategory", existingSubcategory);
			return ResponseEntity.status(HttpStatus.OK).body(res);
		} catch (Exception e) {
			res.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(res);
		}
	}

	
	
	
	@GetMapping("/noauth/subcategory/subcategories-list")
	public Page<SubcategoryResponse> getAllSubcategories(@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "size", defaultValue = "5") Integer size) {

		return subcategoryService.getAllSubcategories(page, size);
	}

	
	
	
	@GetMapping("/noauth/active-subcategory/subcategories-list-category/{id}")
	public ResponseEntity<Map<String, Object>> getActiveSubcategoriesByCategoryId(@PathVariable(name = "id") Long categoryId) {
		Map<String, Object> res = new LinkedHashMap<>();

		try {
			List<SubcategoryResponse> subcategoryResponses = subcategoryService.getActiveSubcategoriesByCategoryId(categoryId);
			res.put("Subcategories", subcategoryResponses);
			return ResponseEntity.status(HttpStatus.OK).body(res);
		} catch (Exception e) {
			res.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(res);
		}
	}

	
	
	
	@GetMapping("/noauth/subcategory/subcategories-category/{id}")
	public Page<SubcategoryResponse> getSubcategoriesByCategoryId(
			@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "size", defaultValue = "5") Integer size, @PathVariable(name = "id") Long categoryId) {

		return subcategoryService.getSubcategoriesByCategoryId(page, size, categoryId);
	}

	
	
	
	@GetMapping("/noauth/subcategory/subcategories-query")
	public Page<SubcategoryResponse> getAllSubcategoriesByQuery(
			@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "size", defaultValue = "5") Integer size,
			@RequestParam(name = "query", defaultValue = "") String query) {

		return subcategoryService.getsubcategoriesByQuery(page, size, query);
	}

	
	
	
	@PutMapping("/admin/subcategory/change-subcategory-active-status/{id}")
	public ResponseEntity<Map<String, Object>> ChangeSubcategoryActiveStatus(
			@PathVariable(name = "id") Long subcategoryId) {
		Map<String, Object> res = new LinkedHashMap<>();

		try {
			subcategoryService.changeSubcategoryActiveStatus(subcategoryId);
			res.put("message", "Subcategory active status has been changed successfully");
			return ResponseEntity.status(HttpStatus.OK).body(res);
		} catch (Exception e) {
			res.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(res);
		}
	}

	
	
	
	@PutMapping("/admin/subcategory/change-subcategory-image-active-status/{id}")
	public ResponseEntity<Map<String, Object>> ChangeSubcategoryImageActiveStatus(
			@PathVariable(name = "id") Long subcategoryId) {
		Map<String, Object> res = new LinkedHashMap<>();

		try {
			subcategoryService.changeSubcategoryImageActiveStatus(subcategoryId);
			res.put("message", "Subcategory image active status has been changed successfully");
			return ResponseEntity.status(HttpStatus.OK).body(res);
		} catch (Exception e) {
			res.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(res);
		}
	}

	
	
	
	@GetMapping("/noauth/subcategory/get-subcategory-images/{id}")
	public ResponseEntity<Map<String, Object>> getAllSubcategoryImages(@PathVariable(name = "id") Long subcategoryId) {
		Map<String, Object> res = new LinkedHashMap<>();

		try {
			List<SubcategoryImage> subcategoryImages = subcategoryService.getAllSubcategoryImages(subcategoryId);
			res.put("SubcategoryImages", subcategoryImages);
			return ResponseEntity.status(HttpStatus.OK).body(res);
		} catch (Exception e) {
			res.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(res);
		}
	}
}
