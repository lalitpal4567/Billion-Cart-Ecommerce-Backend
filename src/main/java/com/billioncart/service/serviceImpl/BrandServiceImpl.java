package com.billioncart.service.serviceImpl;

import java.util.List;import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.billioncart.model.productCatalogue.Brand;
import com.billioncart.payload.BrandRequest;
import com.billioncart.payload.BrandResponse;
import com.billioncart.repository.BrandRepository;
import com.billioncart.service.BrandService;

@Service
public class BrandServiceImpl implements BrandService{
	private BrandRepository brandRepository;
	
	public BrandServiceImpl(BrandRepository brandRepository) {
		this.brandRepository = brandRepository;
	}

	@Transactional
	public List<BrandResponse> addBrands(List<BrandRequest> requests){
		List<Brand> brands = requests.stream().map(brand ->{
			Brand newBrand = new Brand();
			newBrand.setName(brand.getName());
			return newBrand;
		}).collect(Collectors.toList());
		
		List<Brand> createdBrands = brandRepository.saveAll(brands);
		
		List<BrandResponse> brandResponses = createdBrands.stream().map(brand ->{
			return getBrandResponse(brand);
		}).collect(Collectors.toList());
		
		return brandResponses;
	}
	
	@Override
	 public Page<BrandResponse> getAllBrands(Integer page, Integer size) {
	        Page<Brand> brandPage = brandRepository.findAll(PageRequest.of(page, size));
	        
	        Page<BrandResponse> brandResponses = brandPage.map(brand ->{
	        	return getBrandResponse(brand);
	        });
	        return brandResponses;
	    }
	
	private BrandResponse getBrandResponse(Brand brand) {
		BrandResponse brandResponse = new BrandResponse();
		
		brandResponse.setId(brand.getBrandId());
		brandResponse.setName(brand.getName());
		return brandResponse;
	}
}
