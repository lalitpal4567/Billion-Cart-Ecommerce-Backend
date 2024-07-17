package com.billioncart.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.billioncart.payload.SpecificationNameRequest;
import com.billioncart.payload.SpecificationNameResponse;
import com.billioncart.service.SpecificationNameService;

@RestController
@RequestMapping("/api/v1")
public class SpecificationNameController {
	private SpecificationNameService specificationNameService;

	public SpecificationNameController(SpecificationNameService specificationNameService) {
		this.specificationNameService = specificationNameService;
	}

	@PostMapping("/admin/spec-name/add-name/{id}")
	public ResponseEntity<Map<String, Object>> addSpecificationName(@PathVariable(name = "id") Long subcategoryId,
			@RequestBody List<SpecificationNameRequest> request) {
		Map<String, Object> res = new LinkedHashMap<>();

		try {
			List<SpecificationNameResponse> responses = specificationNameService.addSpecificationNames(subcategoryId,request);
			res.put("message", "Specification name added successfully");
			res.put("SpecificationName", responses);
			return ResponseEntity.status(HttpStatus.CREATED).body(res);
		} catch (Exception e) {
			res.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(res);
		}
	}
	
	@GetMapping("/noauth/spec-name/get-names/{id}")
	public ResponseEntity<Map<String, Object>> getSpecificationNamesBySubcategoryId(@PathVariable(name = "id") Long subcategoryId) {
		Map<String, Object> res = new LinkedHashMap<>();

		try {
			List<SpecificationNameResponse> responses = specificationNameService.getSpecificationNamesBySubcategoryId(subcategoryId);
			res.put("SpecificationName", responses);
			return ResponseEntity.status(HttpStatus.OK).body(res);
		} catch (Exception e) {
			res.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(res);
		}
	}
}
