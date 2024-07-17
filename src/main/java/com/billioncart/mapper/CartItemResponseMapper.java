package com.billioncart.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.billioncart.model.CartItem;
import com.billioncart.model.productCatalogue.Brand;
import com.billioncart.model.productCatalogue.Color;
import com.billioncart.model.productCatalogue.Product;
import com.billioncart.payload.CartItemResponse;

@Mapper
public interface CartItemResponseMapper {
	CartItemResponseMapper INSTANCE = Mappers.getMapper(CartItemResponseMapper.class);
	
	@Mapping(source = "product.brand.name", target = "brand")
    @Mapping(source = "product.color.name", target = "color")
    @Mapping(target = "imageUrl", expression = "java(getFirstImageUrl(product))")
	CartItemResponse toPayload(Product product);
	
	 default String map(Color color) {
	        return color != null ? color.getName() : null;
	    }

	    default String map(Brand brand) {
	        return brand != null ? brand.getName() : null;
	    }
	    
	    default String getFirstImageUrl(Product product) {
	        if (product.getProductImages() != null && !product.getProductImages().isEmpty()) {
	            return product.getProductImages().get(0).getImageUrl();
	        }
	        return null;
	    }
}
