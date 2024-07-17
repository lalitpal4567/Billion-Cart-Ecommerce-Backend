package com.billioncart.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.billioncart.model.productCatalogue.ProductImage;
import com.billioncart.payload.ProductImageRequest;

@Mapper
public interface ProductImageRequestMapper {
	ProductImageRequestMapper INSTANCE = Mappers.getMapper(ProductImageRequestMapper.class);
	
	ProductImage toEntity(ProductImageRequest request);
}
