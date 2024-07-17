package com.billioncart.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.billioncart.model.productCatalogue.Product;
import com.billioncart.payload.ProductResponse;

@Mapper
public interface ProductResponseMapper {
	ProductResponseMapper INSTANCE = Mappers.getMapper(ProductResponseMapper.class);
	
	ProductResponse toPayload(Product product);
}
