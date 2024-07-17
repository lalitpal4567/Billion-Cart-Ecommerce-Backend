package com.billioncart.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.billioncart.model.productCatalogue.Category;
import com.billioncart.payload.CategoryResponse;

@Mapper
public interface CategoryResponseMapper {
	CategoryResponseMapper INSTANCE = Mappers.getMapper(CategoryResponseMapper.class);
	
	CategoryResponse toPayload(Category category);
}
