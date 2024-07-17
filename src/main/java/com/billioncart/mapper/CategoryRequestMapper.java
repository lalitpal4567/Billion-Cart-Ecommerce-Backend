package com.billioncart.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.billioncart.model.productCatalogue.Category;
import com.billioncart.payload.CategoryRequest;

@Mapper
public interface CategoryRequestMapper {
	CategoryRequestMapper INSTANCE = Mappers.getMapper(CategoryRequestMapper.class);
	
	Category toEntity(CategoryRequest request);
}
