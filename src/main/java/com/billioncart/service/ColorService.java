package com.billioncart.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.billioncart.model.productCatalogue.Color;
import com.billioncart.payload.ColorRequest;
import com.billioncart.payload.ColorResponse;

public interface ColorService {
	List<ColorResponse> addColors(List<ColorRequest> requests);
	
	Page<ColorResponse> getAllColors(Integer page, Integer size);
}
