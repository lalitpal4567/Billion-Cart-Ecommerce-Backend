package com.billioncart.service;

import java.util.List;

import com.billioncart.model.productCatalogue.Brand;
import com.billioncart.model.productCatalogue.Color;
import com.billioncart.payload.BrandResponse;
import com.billioncart.payload.ColorResponse;

public interface AttributeService {
	List<BrandResponse> getAvailableBrands(Long subcategoryId);
	
	List<ColorResponse> getAvailableColors(Long subcategoryId);
}
