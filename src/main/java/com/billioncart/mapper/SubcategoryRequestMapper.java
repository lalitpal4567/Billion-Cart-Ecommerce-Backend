package com.billioncart.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.billioncart.model.productCatalogue.Subcategory;
import com.billioncart.payload.SubcategoryRequest;

@Mapper
public interface SubcategoryRequestMapper {
	SubcategoryRequestMapper INSTANCE = Mappers.getMapper(SubcategoryRequestMapper.class);
	
	Subcategory toEntity(SubcategoryRequest request);
}
