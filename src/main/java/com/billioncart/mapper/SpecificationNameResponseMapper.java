package com.billioncart.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.billioncart.model.productCatalogue.SpecificationName;
import com.billioncart.payload.SpecificationNameResponse;

@Mapper
public interface SpecificationNameResponseMapper {
	SpecificationNameResponseMapper INSTANCE = Mappers.getMapper(SpecificationNameResponseMapper.class);
	
	SpecificationNameResponse toPayload(SpecificationName specificationName);
	
}
