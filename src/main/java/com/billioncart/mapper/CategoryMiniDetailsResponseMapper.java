package com.billioncart.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.billioncart.model.productCatalogue.Category;
import com.billioncart.payload.CategoryDetailsResponse;
import com.billioncart.payload.CategoryMiniDetailsResponse;

@Mapper
public interface CategoryMiniDetailsResponseMapper {
	CategoryMiniDetailsResponseMapper  INSTANCE = Mappers.getMapper(CategoryMiniDetailsResponseMapper.class);
	
	CategoryMiniDetailsResponse toPayload(Category category);
}
