package com.billioncart.service.serviceImpl;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.billioncart.exception.BrandNotFoundException;
import com.billioncart.exception.ColorNotFoundException;
import com.billioncart.exception.ImageNotFoundException;
import com.billioncart.exception.ImageUploadException;
import com.billioncart.exception.ProductNotFoundException;
import com.billioncart.exception.ResourceNotFoundException;
import com.billioncart.exception.SubcategoryNotFoundException;
import com.billioncart.mapper.BrandResponseMapper;
import com.billioncart.mapper.CategoryMiniDetailsResponseMapper;
import com.billioncart.mapper.ColorResponseMapper;
import com.billioncart.mapper.ProductImageRequestMapper;
import com.billioncart.mapper.ProductImageResponseMapper;
import com.billioncart.mapper.ProductRequestMapper;
import com.billioncart.mapper.ProductResponseMapper;
import com.billioncart.mapper.SpecificationNameValueResponseMapper;
import com.billioncart.mapper.SpecificationValueRequestMapper;
import com.billioncart.mapper.SubcategoryMiniDetailsResponseMapper;
import com.billioncart.model.productCatalogue.Brand;
import com.billioncart.model.productCatalogue.Color;
import com.billioncart.model.productCatalogue.Product;
import com.billioncart.model.productCatalogue.ProductImage;
import com.billioncart.model.productCatalogue.SpecificationName;
import com.billioncart.model.productCatalogue.SpecificationValue;
import com.billioncart.model.productCatalogue.Subcategory;
import com.billioncart.model.productCatalogue.SubcategoryImage;
import com.billioncart.payload.BrandResponse;
import com.billioncart.payload.CategoryDetailsResponse;
import com.billioncart.payload.CategoryMiniDetailsResponse;
import com.billioncart.payload.ColorResponse;
import com.billioncart.payload.ImageAltText;
import com.billioncart.payload.ImageFileWithMetadata;
import com.billioncart.payload.ProductImageRequest;
import com.billioncart.payload.ProductImageResponse;
import com.billioncart.payload.ProductRequest;
import com.billioncart.payload.ProductResponse;
import com.billioncart.payload.SpecificationResponse;
import com.billioncart.payload.SpecificationValueRequest;
import com.billioncart.payload.SubcategoryDetailsResponse;
import com.billioncart.payload.SubcategoryMiniDetailsResponse;
import com.billioncart.repository.BrandRepository;
import com.billioncart.repository.ColorRepository;
import com.billioncart.repository.ProductImageRepository;
import com.billioncart.repository.ProductRepository;
import com.billioncart.repository.SpecificationNameRepository;
import com.billioncart.repository.SpecificationValueRepository;
import com.billioncart.repository.SubcategoryRepository;
import com.billioncart.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private SubcategoryRepository subcategoryRepository;

	@Autowired
	private SpecificationNameRepository specificationNameRepository;

	@Autowired
	private ProductImageRepository productImageRepository;

	@Autowired
	private SpecificationValueRepository specificationValueRepository;

	@Autowired
	private ColorRepository colorRepository;

	@Autowired
	private BrandRepository brandRepository;

	@Autowired
	private AmazonS3 amazonS3;

	@Override
	@Transactional
	public ProductResponse addProduct(Long subcategoryId, ProductRequest request, List<MultipartFile> imageFiles) {
		Subcategory existingSubcategory = subcategoryRepository.findById(subcategoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Subcategory not found"));

		Product newProduct = ProductRequestMapper.INSTANCE.toEntity(request);
		newProduct.setSubcategory(existingSubcategory);

		List<ImageAltText> altTextList = request.getAltTexts();
		List<ImageFileWithMetadata> imageFileWithMetadatas = IntStream.range(0, imageFiles.size()).mapToObj(i -> {
			ImageFileWithMetadata imageData = new ImageFileWithMetadata();
			imageData.setImageFile(imageFiles.get(i));
			imageData.setAltText(altTextList.get(i).getAltText());
			return imageData;
		}).collect(Collectors.toList());

		List<ProductImage> productImages = imageFileWithMetadatas.stream().map(image -> {
			String generatedImageUrl;
			try {
				generatedImageUrl = uploadImageToS3(image.getImageFile());
			} catch (IOException e) {
				throw new ImageUploadException("Failed to upload image.");
			}
			ProductImage productImage = new ProductImage();
			productImage.setImageUrl(generatedImageUrl);
			productImage.setAltText(image.getAltText());
			productImage.setProduct(newProduct);
			return productImage;

		}).collect(Collectors.toList());

		newProduct.setProductImages(productImages);
		List<SpecificationValue> specificationValues = getSpecificationValues(request, newProduct);

		Color existingColor = colorRepository.findById(request.getColorId())
				.orElseThrow(() -> new ColorNotFoundException("Product color not found"));
		Brand existingBrand = brandRepository.findById(request.getBrandId())
				.orElseThrow(() -> new BrandNotFoundException("Product brand not found"));

		newProduct.setProductImages(productImages);
		newProduct.setSpecificationValues(specificationValues);
		newProduct.setBrand(existingBrand);
		newProduct.setColor(existingColor);

		Product createdProduct = productRepository.save(newProduct);
		return getProductResponse(createdProduct);
	}

	private String uploadImageToS3(MultipartFile imageFile) throws IOException {
		String bucketName = "billion-cart-bucket";

		String uniqueID = UUID.randomUUID().toString();
		String fileName = "product-images/" + uniqueID + imageFile.getOriginalFilename();
		String cloudFrontDomainName = "https://d3jxj5kmew5ec.cloudfront.net";

		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType(imageFile.getContentType());
		metadata.setContentLength(imageFile.getSize());

		amazonS3.putObject(new PutObjectRequest(bucketName, fileName, imageFile.getInputStream(), metadata));
		String cloudFrontUrl = cloudFrontDomainName + "/" + fileName;
		return cloudFrontUrl;
	}

	public static List<SpecificationResponse> getSpecificationResponses(Product createdProduct) {
		List<SpecificationValue> createdSpecificationValues = createdProduct.getSpecificationValues();
		return createdSpecificationValues.stream().map(spec -> {
			SpecificationResponse specificationResponse = SpecificationNameValueResponseMapper.INSTANCE.toPayload(spec);
			specificationResponse.setName(spec.getSpecificationName().getName());
			specificationResponse.setNameId(spec.getSpecificationName().getNameId());
			return specificationResponse;
		}).collect(Collectors.toList());
	}

	private List<SpecificationValue> getSpecificationValues(ProductRequest request, Product newProduct) {
		List<SpecificationValueRequest> valueList = request.getSpecificationValues();

		return valueList.stream().map(value -> {
			SpecificationValue values = SpecificationValueRequestMapper.INSTANCE.toEntity(value);
			SpecificationName sname = specificationNameRepository.findById(value.getNameId())
					.orElseThrow(() -> new ResourceNotFoundException("Specification name not found"));
			values.setProduct(newProduct);
			values.setSpecificationName(sname);
			return values;
		}).collect(Collectors.toList());
	}

	@Override
	public void removeProduct(Long productId) {
		Product existingProduct = productRepository.findById(productId)
				.orElseThrow(() -> new ProductNotFoundException("Product not found"));

		productRepository.deleteById(productId);
	}

	public ProductResponse getProductResponse(Product product) {
		CategoryMiniDetailsResponse categoryMiniDetailsResponse = CategoryMiniDetailsResponseMapper.INSTANCE
				.toPayload(product.getSubcategory().getCategory());
		SubcategoryMiniDetailsResponse subcategoryMiniDetailsResponse = SubcategoryMiniDetailsResponseMapper.INSTANCE
				.toPayload(product.getSubcategory());

		ProductResponse productResponse = ProductResponseMapper.INSTANCE.toPayload(product);
		productResponse.setProductImages(getImageResponse(product));
		productResponse.setSpecifications(getSpecificationResponses(product));
		productResponse.setCategory(categoryMiniDetailsResponse);
		productResponse.setSubcategory(subcategoryMiniDetailsResponse);

		Color existingColor = colorRepository.findById(product.getColor().getColorId())
				.orElseThrow(() -> new ColorNotFoundException("Product color not found"));
		Brand existingBrand = brandRepository.findById(product.getBrand().getBrandId())
				.orElseThrow(() -> new BrandNotFoundException("Product brand not found"));

		BrandResponse brandResponse = new BrandResponse();
		ColorResponse colorResponse = new ColorResponse();
		
		brandResponse.setId(existingBrand.getBrandId());
		brandResponse.setName(existingBrand.getName());
		
		colorResponse.setId(existingColor.getColorId());
		colorResponse.setName(existingColor.getName());
		
		productResponse.setBrand(brandResponse);
		productResponse.setColor(colorResponse);
		return productResponse;
	}

	public static List<ProductImageResponse> getImageResponse(Product product) {
		List<ProductImage> productImages = product.getProductImages();
		List<ProductImageResponse> productImageResponses = productImages.stream().map(img -> {
			ProductImageResponse productImageResponse = ProductImageResponseMapper.INSTANCE.toPayload(img);
			return productImageResponse;
		}).collect(Collectors.toList());

		return productImageResponses;
	}

	@Override
	public ProductResponse getProductById(Long productId) {
		Product existingProduct = productRepository.findById(productId)
				.orElseThrow(() -> new ProductNotFoundException("Product not found"));

		return getProductResponse(existingProduct);
	}

//	@Override
//	@Transactional
//	public Page<ProductResponse> getProductBySubcategoryId(Long subcategoryId, Integer page, Integer size) {
//		Subcategory subcategory = subcategoryRepository.findById(subcategoryId)
//				.orElseThrow(() -> new SubcategoryNotFoundException("Subcategory not found"));
//
//		Page<Product> productPage = productRepository.findAllBySubcategory(PageRequest.of(page, size), subcategory);
//
//		Page<ProductResponse> productResponsePage = productPage.map(p -> getProductResponse(p));
//		return productResponsePage;
//	}
	
	@Override
	@Transactional
	public Page<ProductResponse> getProductBySubcategoryId(Long subcategoryId, Integer page, Integer size, List<Long> brandIds, List<Long> colorIds) {
        Pageable pageable = PageRequest.of(page, size);
//        Page<Product> productPage = productRepository.findBySubcategoryAndFilters(subcategoryId, brandIds, colorIds, pageable);
        Page<Product> productPage = productRepository.findBySubcategoryAndFilters(subcategoryId, brandIds.isEmpty() ? null : brandIds, colorIds.isEmpty() ? null : colorIds, pageable);

		Page<ProductResponse> productResponsePage = productPage.map(p -> getProductResponse(p));
		return productResponsePage;

    }

	@Override
	@Transactional
	public Page<ProductResponse> getAllProducts(Integer page, Integer size) {
		Page<Product> productsPage = productRepository.findAll(PageRequest.of(page, size));

		Page<ProductResponse> productsResponsePage = productsPage.map(product -> getProductResponse(product));
		return productsResponsePage;
	}

	
	@Override
	@Transactional
	public List<ProductImage> addProductImages(Long productId, List<ImageAltText> altTextList, List<MultipartFile> imageFiles) {
		Product existingProduct= productRepository.findById(productId)
				.orElseThrow(() -> new ProductNotFoundException("Product not found"));
		
		List<ImageFileWithMetadata> imageFileWithMetadatas = IntStream.range(0, imageFiles.size())
			    .mapToObj(i -> {
			        ImageFileWithMetadata imageData = new ImageFileWithMetadata();
			        imageData.setImageFile(imageFiles.get(i));
			        imageData.setAltText(altTextList.get(i).getAltText());
			        return imageData;
			    })
			    .collect(Collectors.toList());
		
		List<ProductImage> productImages = imageFileWithMetadatas.stream().map(image ->{
			String generatedImageUrl;
			try {
				generatedImageUrl = uploadImageToS3(image.getImageFile());
			} catch (IOException e) {
				throw new ImageUploadException("Failed to upload image.");
			}
			ProductImage productImage = new ProductImage();
			productImage.setImageUrl(generatedImageUrl);
			productImage.setAltText(image.getAltText());
			productImage.setProduct(existingProduct);
			return productImage;
		}).collect(Collectors.toList());
		
		return productImageRepository.saveAll(productImages);
	}
	
	@Transactional
	@Override
	public ProductResponse updateProduct(Long productId, ProductRequest request) {
		Product existingProduct = productRepository.findById(productId)
				.orElseThrow(() -> new ProductNotFoundException("Product not found"));

		Product product = ProductRequestMapper.INSTANCE.toEntity(request);
		product.setProductId(existingProduct.getProductId());
		product.setSubcategory(existingProduct.getSubcategory());

		Color existingColor = colorRepository.findById(request.getColorId())
				.orElseThrow(() -> new ColorNotFoundException("Product color not found"));
		Brand existingBrand = brandRepository.findById(request.getBrandId())
				.orElseThrow(() -> new BrandNotFoundException("Product brand not found"));

		product.setBrand(existingBrand);
		product.setColor(existingColor);

		List<ProductImage> productImages = existingProduct.getProductImages();
		product.setProductImages(productImages);
		List<SpecificationValue> specificationValues = getSpecificationValues(request, product);

		product.setSpecificationValues(existingProduct.getSpecificationValues());
		Product createdProduct = productRepository.save(product);

		specificationValues.forEach(spec -> {
			SpecificationValue specificationValue = specificationValueRepository
					.findByProductAndSpecificationName(product, spec.getSpecificationName())
					.orElseThrow(() -> new ResourceNotFoundException("specification value not found"));
			specificationValue.setValue(spec.getValue());
			specificationValueRepository.save(specificationValue);
		});

		List<SpecificationResponse> specificationResponses = getSpecificationResponses(createdProduct);
		List<ProductImageResponse> productImageResponses = getImageResponse(createdProduct);

		ProductResponse productResponse = getProductResponse(createdProduct);
		productResponse.setSpecifications(specificationResponses);
		productResponse.setProductImages(productImageResponses);

		return productResponse;
	}

	
	@Override
	public void removeProductImageById(Long imageId) {
		ProductImage productImage = productImageRepository.findById(imageId).orElseThrow(() -> new ImageNotFoundException("Product image not found"));
		productImageRepository.deleteById(imageId);
	}
	@Override
	@Transactional
	public List<ProductImage> getAllProductImages(Long productId) {
		Product existingProduct = productRepository.findById(productId)
				.orElseThrow(() -> new ProductNotFoundException("Product not found"));

		return productImageRepository.findAllByProduct(existingProduct);
	}
}
