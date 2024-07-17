package com.billioncart.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.billioncart.model.productCatalogue.SpecificationValue;
import com.billioncart.payload.SpecificationValueRequest;

@Mapper
public interface SpecificationValueRequestMapper {
	SpecificationValueRequestMapper INSTANCE = Mappers.getMapper(SpecificationValueRequestMapper.class);
	
	SpecificationValue toEntity(SpecificationValueRequest request);
}
