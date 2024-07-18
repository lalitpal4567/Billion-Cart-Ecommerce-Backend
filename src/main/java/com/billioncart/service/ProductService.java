package com.billioncart.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.billioncart.model.productCatalogue.ProductImage;
import com.billioncart.payload.ImageAltText;
import com.billioncart.payload.ProductRequest;
import com.billioncart.payload.ProductResponse;

public interface ProductService {
	ProductResponse addProduct(Long subcategoryId, ProductRequest request, List<MultipartFile> imageFiles);
	
	ProductResponse getProductById(Long productId);
	
	void removeProduct(Long productId);
	
	ProductResponse updateProduct(Long productId, ProductRequest request);
		
	Page<ProductResponse> getAllProducts(Integer page, Integer size);
	
	List<ProductImage> addProductImages(Long productId, List<ImageAltText> altTextList, List<MultipartFile> imageFiles);
	
	void removeProductImageById(Long imageId);
	
	List<ProductImage> getAllProductImages(Long productId);
	
	void changeProductImageActiveStatus(Long imageId);
	
	void changeProductActiveStatus(Long productId);
	
	Page<ProductResponse> getProductBySubcategoryId(Long subcategoryId, Integer page, Integer size, List<Long> brandIds, List<Long> colorIds);

}
