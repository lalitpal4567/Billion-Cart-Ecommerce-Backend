package com.billioncart.service.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.billioncart.exception.SubcategoryNotFoundException;
import com.billioncart.mapper.SpecificationNameRequestMapper;
import com.billioncart.mapper.SpecificationNameResponseMapper;
import com.billioncart.model.productCatalogue.Product;
import com.billioncart.model.productCatalogue.SpecificationName;
import com.billioncart.model.productCatalogue.SpecificationValue;
import com.billioncart.model.productCatalogue.Subcategory;
import com.billioncart.payload.SpecificationNameRequest;
import com.billioncart.payload.SpecificationNameResponse;
import com.billioncart.repository.SpecificationNameRepository;
import com.billioncart.repository.SpecificationValueRepository;
import com.billioncart.repository.SubcategoryRepository;
import com.billioncart.service.SpecificationNameService;
import com.fasterxml.jackson.core.sym.Name;

@Service
public class SpecificationNameServiceImpl implements SpecificationNameService{
	private SpecificationNameRepository specificationNameRepository;
	private SubcategoryRepository subcategoryRepository;
	private SpecificationValueRepository specificationValueRepository;
	
	public SpecificationNameServiceImpl(SpecificationNameRepository specificationNameRepository, SubcategoryRepository subcategoryRepository, SpecificationValueRepository specificationValueRepository) {
		this.specificationNameRepository = specificationNameRepository;
		this.subcategoryRepository = subcategoryRepository;
		this.specificationValueRepository = specificationValueRepository;
	}
	
	@Transactional
	public List<SpecificationNameResponse> addSpecificationNames(Long subcategoryId, List<SpecificationNameRequest> requests){
		Subcategory existingSubcategory = subcategoryRepository.findById(subcategoryId).orElseThrow(() -> new SubcategoryNotFoundException("Subcategory not found"));
		
		List<SpecificationName> specificationNames = requests.stream().map(name ->{
			SpecificationName specificationName = SpecificationNameRequestMapper.INSTANCE.toEntity(name);
			specificationName.setSubcategory(existingSubcategory);
			return specificationName;
		}).collect(Collectors.toList());
		
		List<SpecificationName> savedSpecificationNames = specificationNameRepository.saveAll(specificationNames);
		
		for (Product product : existingSubcategory.getProducts()) {
		    for (SpecificationName name : savedSpecificationNames) {
		        SpecificationValue newSpecificationValue = new SpecificationValue();
		        newSpecificationValue.setProduct(product);
		        newSpecificationValue.setSpecificationName(name);
		        specificationValueRepository.save(newSpecificationValue);
		    }
		}

		
		
		
		List<SpecificationNameResponse> specificationNameResponses = savedSpecificationNames.stream().map(name ->{
			SpecificationNameResponse specificationNameResponse = SpecificationNameResponseMapper.INSTANCE.toPayload(name);
			specificationNameResponse.getSubcategory().setSubcategoryId(existingSubcategory.getSubcategoryId());
			specificationNameResponse.getSubcategory().setName(existingSubcategory.getName());
			return specificationNameResponse;
		}).collect(Collectors.toList());
		
		
		return specificationNameResponses;
	}
	
	public List<SpecificationNameResponse> getSpecificationNamesBySubcategoryId(Long subcategoryId){
		Subcategory existingSubcategory = subcategoryRepository.findById(subcategoryId).orElseThrow(() -> new SubcategoryNotFoundException());
		
		List<SpecificationName> specificationNames = specificationNameRepository.findAllBySubcategory(existingSubcategory);
		
		List<SpecificationNameResponse> specificationNameResponses = specificationNames.stream().map(name ->{
			SpecificationNameResponse specificationNameResponse = SpecificationNameResponseMapper.INSTANCE.toPayload(name);
			specificationNameResponse.getSubcategory().setSubcategoryId(existingSubcategory.getSubcategoryId());
			specificationNameResponse.getSubcategory().setName(existingSubcategory.getName());
			return specificationNameResponse;
		}).collect(Collectors.toList());
		
		return specificationNameResponses;
	}
}

