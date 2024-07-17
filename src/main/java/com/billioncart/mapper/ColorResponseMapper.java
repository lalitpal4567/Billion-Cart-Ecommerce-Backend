package com.billioncart.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.billioncart.model.productCatalogue.Color;
import com.billioncart.payload.ColorResponse;

@Mapper
public interface ColorResponseMapper {
	ColorResponseMapper INSTANCE = Mappers.getMapper(ColorResponseMapper.class);
	
	ColorResponse toPayload(Color color);
}
