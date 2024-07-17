package com.billioncart.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.billioncart.model.productCatalogue.Brand;
import com.billioncart.payload.BrandRequest;
import com.billioncart.payload.BrandResponse;

public interface BrandService {
	List<BrandResponse> addBrands(List<BrandRequest> requests);
	
	Page<BrandResponse> getAllBrands(Integer page, Integer size);
}
