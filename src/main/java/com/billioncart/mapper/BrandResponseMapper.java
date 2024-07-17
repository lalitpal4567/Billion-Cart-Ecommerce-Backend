package com.billioncart.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.billioncart.model.productCatalogue.Brand;
import com.billioncart.payload.BrandResponse;

@Mapper
public interface BrandResponseMapper {
	BrandResponseMapper INSTANCE = Mappers.getMapper(BrandResponseMapper.class);
	
	BrandResponse toPayload(Brand brand);
}
