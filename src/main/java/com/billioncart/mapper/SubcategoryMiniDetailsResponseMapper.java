package com.billioncart.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.billioncart.model.productCatalogue.Subcategory;
import com.billioncart.payload.SubcategoryDetailsResponse;
import com.billioncart.payload.SubcategoryMiniDetailsResponse;

@Mapper
public interface SubcategoryMiniDetailsResponseMapper {
	SubcategoryMiniDetailsResponseMapper INSTANCE	= Mappers.getMapper(SubcategoryMiniDetailsResponseMapper.class);
	
	SubcategoryMiniDetailsResponse toPayload(Subcategory subcategory);
}
