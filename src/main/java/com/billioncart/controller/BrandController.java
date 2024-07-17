package com.billioncart.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.billioncart.model.productCatalogue.Brand;
import com.billioncart.payload.BrandRequest;
import com.billioncart.payload.BrandResponse;
import com.billioncart.service.BrandService;

@RestController
@RequestMapping("/api/v1")
public class BrandController {
	private BrandService brandService;
	
	public BrandController(BrandService brandService) {
		this.brandService = brandService;
	}
	
	@PostMapping("/admin/brand/add-brand")
	public ResponseEntity<Map<String, Object>> addBrands(@RequestBody List<BrandRequest> request) {
		Map<String, Object> res = new LinkedHashMap<>();

		try {
			List<BrandResponse> responses = brandService.addBrands(request);
			res.put("message", "Brand added successfully");
			res.put("Brand", responses);
			return ResponseEntity.status(HttpStatus.CREATED).body(res);
		} catch (Exception e) {
			res.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(res);
		}
	}
	
	@GetMapping("/noauth/brand/brands-list")
	public Page<BrandResponse> getBrands(@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "size", defaultValue = "5") Integer size) {
		return brandService.getAllBrands(page, size);
	}
}
