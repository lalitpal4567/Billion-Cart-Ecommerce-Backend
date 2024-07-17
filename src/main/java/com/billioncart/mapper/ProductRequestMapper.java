package com.billioncart.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.billioncart.model.productCatalogue.Product;
import com.billioncart.payload.ProductRequest;

@Mapper
public interface ProductRequestMapper {
	ProductRequestMapper INSTANCE = Mappers.getMapper(ProductRequestMapper.class);
	
	Product toEntity(ProductRequest request);
}
