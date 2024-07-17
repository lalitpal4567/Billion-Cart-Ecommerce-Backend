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

import com.billioncart.model.productCatalogue.Color;
import com.billioncart.payload.ColorRequest;
import com.billioncart.payload.ColorResponse;
import com.billioncart.service.ColorService;

@RestController
@RequestMapping("/api/v1")
public class ColorController {
	private ColorService colorService;
	
	public ColorController(ColorService colorService) {
		this.colorService = colorService;
	}
	
	@PostMapping("/admin/color/add-color")
	public ResponseEntity<Map<String, Object>> addColors(@RequestBody List<ColorRequest> request) {
		Map<String, Object> res = new LinkedHashMap<>();

		try {
			List<ColorResponse> responses = colorService.addColors(request);
			res.put("message", "Color added successfully");
			res.put("Color", responses);
			return ResponseEntity.status(HttpStatus.CREATED).body(res);
		} catch (Exception e) {
			res.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(res);
		}
	}
	
	@GetMapping("/noauth/color/colors-list")
	public Page<ColorResponse> getColors(@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "size", defaultValue = "5") Integer size) {
		return colorService.getAllColors(page, size);
	}
}
