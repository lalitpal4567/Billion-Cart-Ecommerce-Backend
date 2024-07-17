package com.billioncart.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.billioncart.model.productCatalogue.Subcategory;
import com.billioncart.payload.SubcategoryResponse;

@Mapper
public interface SubcategoryResponseMapper {
	SubcategoryResponseMapper INSTANCE = Mappers.getMapper(SubcategoryResponseMapper.class);
	
	SubcategoryResponse toPayload(Subcategory subcategory);
}
