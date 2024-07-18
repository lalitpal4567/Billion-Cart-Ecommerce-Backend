package com.billioncart.controller;

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

import com.billioncart.model.productCatalogue.ProductImage;
import com.billioncart.payload.ImageAltText;
import com.billioncart.payload.ProductRequest;
import com.billioncart.payload.ProductResponse;
import com.billioncart.service.ProductService;

@RestController
@RequestMapping("/api/v1")
public class ProductController {
	private ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@PostMapping("/admin/product/add-product/{id}")
	public ResponseEntity<Map<String, Object>> addProduct(@PathVariable(name = "id") Long subcategoryId,
			@RequestPart("product") ProductRequest request, @RequestPart("imageFiles") List<MultipartFile> imageFiles) {
		Map<String, Object> res = new LinkedHashMap<>();

		try {
			ProductResponse createdProduct = productService.addProduct(subcategoryId, request, imageFiles);
			res.put("message", "Product added successfully");
			res.put("Product", createdProduct);
			return ResponseEntity.status(HttpStatus.CREATED).body(res);
		} catch (Exception e) {
			res.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(res);
		}
	}

	@PutMapping("/admin/product/update-product/{id}")
	public ResponseEntity<Map<String, Object>> updateProduct(@PathVariable(name = "id") Long productId,
			@RequestBody ProductRequest request) {
		Map<String, Object> res = new LinkedHashMap<>();

		try {
			ProductResponse updatedProduct = productService.updateProduct(productId, request);
			res.put("message", "Product update successfully");
			res.put("Product", updatedProduct);
			return ResponseEntity.status(HttpStatus.OK).body(res);
		} catch (Exception e) {
			res.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(res);
		}
	}
	
	@DeleteMapping("/admin/product/remove-product/{id}")
	public ResponseEntity<Map<String, Object>> removeProduct(@PathVariable(name = "id") Long productId) {
		Map<String, Object> res = new LinkedHashMap<>();

		try {
			productService.removeProduct(productId);
			res.put("message", "Product removed successfully");
			return ResponseEntity.status(HttpStatus.OK).body(res);
		} catch (Exception e) {
			res.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(res);
		}
	}

	@GetMapping("/noauth/product/fetch-product/{id}")
	public ResponseEntity<Map<String, Object>> getProductById(@PathVariable(name = "id") Long productId) {
		Map<String, Object> res = new LinkedHashMap<>();

		try {
			ProductResponse existingProduct = productService.getProductById(productId);
			res.put("message", "Product found successfully");
			res.put("Product", existingProduct);
			return ResponseEntity.status(HttpStatus.OK).body(res);
		} catch (Exception e) {
			res.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(res);
		}
	}

//	@GetMapping("/noauth/product/product-subcategory-wise/{id}")
//	public Page<ProductResponse> getAllProductsBySubcategoryId(@RequestParam(name = "page", defaultValue = "0") Integer page,
//			@RequestParam(name = "size", defaultValue = "2") Integer size, @PathVariable(name = "id") Long subcategoryId) {
//
//		return productService.getProductBySubcategoryId(subcategoryId, page, size);
//	}
	
	 @GetMapping("/noauth/product/product-subcategory-wise/{id}")
	    public Page<ProductResponse> getAllProductsBySubcategoryId(
	            @RequestParam(name = "page", defaultValue = "0") Integer page,
	            @RequestParam(name = "size", defaultValue = "10") Integer size,
	            @PathVariable(name = "id") Long subcategoryId,
	            @RequestParam(name = "brands", required = false) List<Long> brandIds,
	            @RequestParam(name = "colors", required = false) List<Long> colorIds) {
		 
	        return productService.getProductBySubcategoryId(subcategoryId, page, size, brandIds, colorIds);
	    }
	
	@GetMapping("/noauth/product/product-list")
	public Page<ProductResponse> getAllProducts(@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "size", defaultValue = "5") Integer size) {

		return productService.getAllProducts(page, size);
	}
	
	@PostMapping("/admin/product/add-product-images/{id}")
	public ResponseEntity<Map<String, Object>> addProductImages(@PathVariable(name = "id") Long productId, @RequestPart("altTexts") List<ImageAltText> altTextList , @RequestPart("imageFiles") List<MultipartFile> imageFiles){
		Map<String, Object> res = new LinkedHashMap<>();
		
		try {
			List<ProductImage> productImages = productService.addProductImages(productId, altTextList , imageFiles);
			res.put("message", "Product images added successfully");
			res.put("ProductImages", productImages);
			return ResponseEntity.status(HttpStatus.OK).body(res);
		} catch (Exception e) {
			res.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(res);
		}
	}
	
	@DeleteMapping("/admin/product/remove-product-image/{id}")
	public ResponseEntity<Map<String, Object>> removeProductImagesById(@PathVariable(name = "id") Long imageId){
		Map<String, Object> res = new LinkedHashMap<>();
		
		try {
			productService.removeProductImageById(imageId);
			res.put("message", "Product image removed successfully");
			return ResponseEntity.status(HttpStatus.OK).body(res);
		} catch (Exception e) {
			res.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(res);
		}
	}
	
	@PutMapping("/admin/product/change-product-active-status/{id}")
	public ResponseEntity<Map<String, Object>> changeProductActiveStatus(@PathVariable(name = "id") Long productId){
		Map<String, Object> res = new LinkedHashMap<>();
		
		try {
			productService.changeProductActiveStatus(productId);
			res.put("message", "Product active status has been changed successfully");
			return ResponseEntity.status(HttpStatus.OK).body(res);
		} catch (Exception e) {
			res.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(res);
		}
	}
	
	@PutMapping("/admin/product/change-product-image-active-status/{id}")
	public ResponseEntity<Map<String, Object>> changeProductImageActiveStatus(@PathVariable(name = "id") Long productId){
		Map<String, Object> res = new LinkedHashMap<>();
		
		try {
			productService.changeProductImageActiveStatus(productId);
			res.put("message", "Product image active status has been changed successfully");
			return ResponseEntity.status(HttpStatus.OK).body(res);
		} catch (Exception e) {
			res.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(res);
		}
	}
	
	@GetMapping("/noauth/product/get-product-images/{id}")
	public ResponseEntity<Map<String, Object>> getAllProductImages(@PathVariable(name = "id") Long productId){
		Map<String, Object> res = new LinkedHashMap<>();
		
		try {
			List<ProductImage> productImages = productService.getAllProductImages(productId);
			res.put("ProductImages", productImages);
			return ResponseEntity.status(HttpStatus.OK).body(res);
		} catch (Exception e) {
			res.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(res);
		}
	}
}
