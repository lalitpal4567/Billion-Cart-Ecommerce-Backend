package com.billioncart.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.billioncart.model.CarouselImage;
import com.billioncart.payload.CarouselImageRequest;
import com.billioncart.payload.ImageAltText;
import com.billioncart.service.CarouselImageService;

@RestController
@RequestMapping("/api/v1")
public class CarouselImageController { 
	private CarouselImageService carouselImageService;
	
	public CarouselImageController(CarouselImageService carouselImageService) {
		this.carouselImageService = carouselImageService;
	}

	@PostMapping("/admin/carousel/add-carousel-image")
	public ResponseEntity<Map<String, Object>> addCarouseImage(@RequestPart("altTexts") List<ImageAltText> imageAltTexts, @RequestPart ("imageFiles") List<MultipartFile> imageFiles) {
		Map<String, Object> res = new LinkedHashMap<>();

		try {
			List<CarouselImage> carouselImages = carouselImageService.addCarouselImage(imageAltTexts, imageFiles);
			res.put("message", "Carousel image added successfully");
			res.put("CarouselImage", carouselImages);
			return ResponseEntity.status(HttpStatus.CREATED).body(res);
		} catch (Exception e) {
			res.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(res);
		}
	}
	
	@DeleteMapping("/admin/carousel/remove-carousel-image/{id}")
	public ResponseEntity<Map<String, Object>> removeCarouselImage(@PathVariable(name = "id") Long imageId) {
		Map<String, Object> res = new LinkedHashMap<>();

		try {
			carouselImageService.removeCarouselImage(imageId);
			res.put("message", "Carousel image removed successfully");
			return ResponseEntity.status(HttpStatus.OK).body(res);
		} catch (Exception e) {
			res.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(res);
		}
	}
	
	@PatchMapping("/admin/carousel/update-carousel-active/{id}")
	public ResponseEntity<Map<String, Object>> updateCarouselActiveStatus(@PathVariable(name = "id") Long imageId, @RequestParam(name = "status") Boolean status) {
		Map<String, Object> res = new LinkedHashMap<>();

		try {
			carouselImageService.updateActiveStatus(imageId, status);
			res.put("message", "Carousel image status updated successfully");
			return ResponseEntity.status(HttpStatus.OK).body(res);
		} catch (Exception e) {
			res.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(res);
		}
	}
	
	@GetMapping("/noauth/active-carousel-images-list")
	public Page<CarouselImage> getActiveCarouselImages(@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "size", defaultValue = "5") Integer size) {
		return carouselImageService.getActiveImages(page, size);
	}
	
	@GetMapping("/noauth/carousel/carousel-images-list")
	public Page<CarouselImage> getAllCarouselImages(@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "size", defaultValue = "5") Integer size) {
		return carouselImageService.getAllImages(page, size);
	}
}
