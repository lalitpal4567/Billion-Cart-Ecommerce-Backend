package com.billioncart.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.billioncart.model.productCatalogue.SubcategoryImage;
import com.billioncart.payload.SubcategoryImageRequest;

@Mapper
public interface SubcategoryImageRequestMapper {
	SubcategoryImageRequestMapper INSTANCE = Mappers.getMapper(SubcategoryImageRequestMapper.class);
	
	SubcategoryImage toEntity(SubcategoryImageRequest request);
}
