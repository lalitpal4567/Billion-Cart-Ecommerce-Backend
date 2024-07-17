package com.billioncart.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.billioncart.exception.SubcategoryNotFoundException;
import com.billioncart.model.productCatalogue.Brand;
import com.billioncart.model.productCatalogue.Color;
import com.billioncart.model.productCatalogue.Product;
import com.billioncart.model.productCatalogue.Subcategory;
import com.billioncart.payload.BrandResponse;
import com.billioncart.payload.ColorResponse;
import com.billioncart.repository.BrandRepository;
import com.billioncart.repository.ColorRepository;
import com.billioncart.repository.ProductRepository;
import com.billioncart.repository.SubcategoryRepository;
import com.billioncart.service.AttributeService;

@RestController
@RequestMapping("/api/v1/noauth")
public class AttributeFilterController {
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private SubcategoryRepository subcategoryRepository;
	
	@Autowired
	private BrandRepository brandRepository;
	
	@Autowired
	private ColorRepository colorRepository;
	
	@Autowired
	private AttributeService attributeService;
	
	@GetMapping("/brands/{id}")
    public ResponseEntity<Map<String, Object>> getAvailableBrands( @PathVariable(name = "id") Long subcategoryId) {
		System.out.println("hi welcome");
		Map<String, Object> res = new LinkedHashMap<>();
		
		try {
			List<BrandResponse> brands = attributeService.getAvailableBrands(subcategoryId);
			res.put("Brands", brands);
			return ResponseEntity.status(HttpStatus.OK).body(res);
		} catch (Exception e) {
			res.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
		}
    }
	
	@GetMapping("/colors/{id}")
    public ResponseEntity<Map<String, Object>> getAvailableColor( @PathVariable(name = "id") Long subcategoryId) {
		System.out.println("hi welcome");
		Map<String, Object> res = new LinkedHashMap<>();
		
		try {
			List<ColorResponse> brands = attributeService.getAvailableColors(subcategoryId);
			res.put("Colors", brands);
			return ResponseEntity.status(HttpStatus.OK).body(res);
		} catch (Exception e) {
			res.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
		}
    }
}
