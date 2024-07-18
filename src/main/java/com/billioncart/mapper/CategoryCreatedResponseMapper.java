package com.billioncart.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.billioncart.model.productCatalogue.Category;
import com.billioncart.payload.CategoryCreatedResponse;

@Mapper
public interface CategoryCreatedResponseMapper {
	CategoryCreatedResponseMapper INSTANCE = Mappers.getMapper(CategoryCreatedResponseMapper.class);
	
	CategoryCreatedResponse toPayload(Category category);
}
