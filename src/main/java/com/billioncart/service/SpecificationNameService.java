package com.billioncart.service;

import java.util.List;

import com.billioncart.payload.SpecificationNameRequest;
import com.billioncart.payload.SpecificationNameResponse;

public interface SpecificationNameService {
	List<SpecificationNameResponse> addSpecificationNames(Long subcategoryId, List<SpecificationNameRequest> requests);
	
	List<SpecificationNameResponse> getSpecificationNamesBySubcategoryId(Long subcategoryId);
}
