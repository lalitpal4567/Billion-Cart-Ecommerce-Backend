package com.billioncart.service.serviceImpl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.billioncart.exception.BrandNotFoundException;
import com.billioncart.exception.ColorNotFoundException;
import com.billioncart.exception.SubcategoryNotFoundException;
import com.billioncart.model.productCatalogue.Brand;
import com.billioncart.model.productCatalogue.Color;
import com.billioncart.model.productCatalogue.Product;
import com.billioncart.model.productCatalogue.Subcategory;
import com.billioncart.payload.BrandResponse;
import com.billioncart.payload.ColorResponse;
import com.billioncart.repository.BrandRepository;
import com.billioncart.repository.ColorRepository;
import com.billioncart.repository.ProductRepository;
import com.billioncart.repository.SubcategoryRepository;
import com.billioncart.service.AttributeService;

@Service
public class AttributeServiceImpl implements AttributeService{
	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private SubcategoryRepository subcategoryRepository;

	@Autowired
	private BrandRepository brandRepository;

	@Autowired
	private ColorRepository colorRepository;

	@Override
	public List<BrandResponse> getAvailableBrands(Long subcategoryId) {
		System.out.println("green yellow");
		Subcategory existingSubcategory = subcategoryRepository.findById(subcategoryId)
				.orElseThrow(() -> new SubcategoryNotFoundException("Subcategory not found"));
		List<Product> products = productRepository.findAllBySubcategory(existingSubcategory);
		
		Set<Long> brandIds = products.stream().map((product) ->{
			return product.getBrand().getBrandId();
		}).collect(Collectors.toSet());
				
		List<BrandResponse> distinctBrands = brandIds.stream().map((brandId) ->{
			Brand brand = brandRepository.findById(brandId).orElseThrow(() -> new BrandNotFoundException("Brand not found"));
			
			BrandResponse brandResponse = new BrandResponse();
			brandResponse.setId(brand.getBrandId());
			brandResponse.setName(brand.getName());
			
			return brandResponse;
		}).collect(Collectors.toList());
		
	      return distinctBrands;
	}
	
	public List<ColorResponse> getAvailableColors(Long subcategoryId) {
		System.out.println("green yellow");
		Subcategory existingSubcategory = subcategoryRepository.findById(subcategoryId)
				.orElseThrow(() -> new SubcategoryNotFoundException("Subcategory not found"));
		List<Product> products = productRepository.findAllBySubcategory(existingSubcategory);
		
		Set<Long> colorIds = products.stream().map((product) ->{
			return product.getColor().getColorId();
		}).collect(Collectors.toSet());
				
		List<ColorResponse> distinctColors = colorIds.stream().map((colorId) ->{
			Color color = colorRepository.findById(colorId).orElseThrow(() -> new ColorNotFoundException("Color not found"));
			ColorResponse colorResponse = new ColorResponse();
			colorResponse.setId(color.getColorId());
			colorResponse.setName(color.getName());
			return colorResponse;
		}).collect(Collectors.toList());

	      return distinctColors;
	}
}
