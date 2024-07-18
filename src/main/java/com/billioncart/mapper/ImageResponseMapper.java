package com.billioncart.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.billioncart.model.productCatalogue.CategoryImage;
import com.billioncart.model.productCatalogue.ProductImage;
import com.billioncart.model.productCatalogue.SubcategoryImage;
import com.billioncart.payload.ImageResponse;

@Mapper
public interface ImageResponseMapper {
	ImageResponseMapper INSTANCE = Mappers.getMapper(ImageResponseMapper.class);

	ImageResponse toPayload(CategoryImage image);

	ImageResponse toPayload(SubcategoryImage image);

	ImageResponse toPayload(ProductImage image);
}
