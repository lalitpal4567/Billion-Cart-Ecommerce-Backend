package com.billioncart.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.billioncart.model.CarouselImage;
import com.billioncart.payload.CarouselImageRequest;
import com.billioncart.payload.ImageAltText;

public interface CarouselImageService {	
	List<CarouselImage> addCarouselImage(List<ImageAltText> altTextList, List<MultipartFile> imageFiles);
		
	void changeCarouselImageActiveStatus(Long imageId);
	
	void removeCarouselImage(Long carouselId);
	
	Page<CarouselImage> getActiveImages(Integer page, Integer size);
	
	Page<CarouselImage> getAllImages(Integer page, Integer size);
}
