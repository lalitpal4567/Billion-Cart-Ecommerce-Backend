package com.billioncart.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.billioncart.model.productCatalogue.SpecificationValue;
import com.billioncart.payload.SpecificationResponse;

@Mapper
public interface SpecificationNameValueResponseMapper {
	SpecificationNameValueResponseMapper INSTANCE = Mappers.getMapper(SpecificationNameValueResponseMapper.class);
	
	SpecificationResponse toPayload(SpecificationValue specificationValue);
}
