package com.billioncart.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.billioncart.model.productCatalogue.Product;
import com.billioncart.payload.WishlistItemResponse;

@Mapper
public interface WishlistItemResponseMapper {
	WishlistItemResponseMapper iNSTANCE = Mappers.getMapper(WishlistItemResponseMapper.class);
	
    @Mapping(target = "imageUrl", expression = "java(getFirstImageUrl(product))")
	WishlistItemResponse toPayload(Product product);
	
	  default String getFirstImageUrl(Product product) {
	        if (product.getProductImages() != null && !product.getProductImages().isEmpty()) {
	            return product.getProductImages().get(0).getImageUrl();
	        }
	        return null;
	    }
}
