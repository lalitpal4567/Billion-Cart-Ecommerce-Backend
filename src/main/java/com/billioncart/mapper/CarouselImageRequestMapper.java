package com.billioncart.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.billioncart.model.CarouselImage;
import com.billioncart.payload.CarouselImageRequest;

@Mapper
public interface CarouselImageRequestMapper {
	CarouselImageRequestMapper INSTANCE = Mappers.getMapper(CarouselImageRequestMapper.class);
	
	CarouselImage toEntity(CarouselImageRequest request);
}

