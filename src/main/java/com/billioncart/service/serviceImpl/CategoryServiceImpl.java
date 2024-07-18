package com.billioncart.service.serviceImpl;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.billioncart.exception.CategoryAlreadyExistsException;
import com.billioncart.exception.CategoryNotFoundException;
import com.billioncart.exception.ImageNotFoundException;
import com.billioncart.exception.ImageUploadException;
import com.billioncart.exception.ResourceNotFoundException;
import com.billioncart.exception.SubcategoryNotFoundException;
import com.billioncart.mapper.CategoryRequestMapper;
import com.billioncart.mapper.CategoryResponseMapper;
import com.billioncart.mapper.ImageResponseMapper;
import com.billioncart.mapper.SubcategoryMiniDetailsResponseMapper;
import com.billioncart.mapper.SubcategoryRequestMapper;
import com.billioncart.mapper.SubcategoryResponseMapper;
import com.billioncart.model.productCatalogue.Category;
import com.billioncart.model.productCatalogue.CategoryImage;
import com.billioncart.model.productCatalogue.Subcategory;
import com.billioncart.model.productCatalogue.SubcategoryImage;
import com.billioncart.payload.CategoryRequest;
import com.billioncart.payload.CategoryResponse;
import com.billioncart.payload.ImageAltText;
import com.billioncart.payload.ImageFileWithMetadata;
import com.billioncart.payload.ImageResponse;
import com.billioncart.payload.SubcategoryMiniDetailsResponse;
import com.billioncart.payload.SubcategoryRequest;
import com.billioncart.payload.SubcategoryResponse;
import com.billioncart.repository.CategoryImageRepository;
import com.billioncart.repository.CategoryRepository;
import com.billioncart.repository.SubcategoryRepository;
import com.billioncart.service.CategoryService;

import ch.qos.logback.core.status.StatusUtil;

@Service
public class CategoryServiceImpl implements CategoryService {
	private CategoryRepository categoryRepository;
	private SubcategoryRepository subcategoryRepository;
	private CategoryImageRepository categoryImageRepository;
	private S3Bucket s3Bucket;

	public CategoryServiceImpl(CategoryRepository categoryRepository, AmazonS3 amazonS3, SubcategoryRepository subcategoryRepository, CategoryImageRepository categoryImageRepository, S3Bucket s3Bucket) {
		this.categoryRepository = categoryRepository;
		this.subcategoryRepository = subcategoryRepository;
		this.categoryImageRepository = categoryImageRepository;
		this.s3Bucket = s3Bucket;
	}

	@Override
	public CategoryResponse addCategory(CategoryRequest request, List<MultipartFile> imageFiles) throws IOException {	
		Optional<Category> existingCategory = categoryRepository.findByNameIgnoreCase(request.getName());
		if (existingCategory.isPresent()) {
		    throw new CategoryAlreadyExistsException("Category with name '" + request.getName() + "' already exists.");
		}
		
		Category newCategory = CategoryRequestMapper.INSTANCE.toEntity(request);
		newCategory.setActive(true);

		List<ImageAltText> altTextList = request.getAltTexts();
		List<ImageFileWithMetadata> imageFileWithMetadatas = IntStream.range(0, imageFiles.size()).mapToObj(i -> {
			ImageFileWithMetadata imageData = new ImageFileWithMetadata();
			imageData.setImageFile(imageFiles.get(i));
			imageData.setAltText(altTextList.get(i).getAltText());
			return imageData;
		}).collect(Collectors.toList());
		
		List<CategoryImage> categoryImages = imageFileWithMetadatas.stream().map(image -> {
			String generatedImageUrl;
			try {
				generatedImageUrl = s3Bucket.uploadImageToS3(image.getImageFile(), "/category-images");
			} catch (IOException e) {
				throw new ImageUploadException("Failed to upload image.");
			}
			
			CategoryImage categoryImage = new CategoryImage();
			categoryImage.setImageUrl(generatedImageUrl);
			categoryImage.setAltText(image.getAltText());
			categoryImage.setCategory(newCategory);
			categoryImage.setActive(true);
			return categoryImage;
		}).collect(Collectors.toList());
		
		newCategory.setCategoryImages(categoryImages);
		Category createdCategory = categoryRepository.save(newCategory);
		return getCategoryResponse(createdCategory);
	}

	@Transactional
	@Override
	public void removeCategoryById(Long categoryId) {
		Category existingCatgory = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category does not exist."));
		categoryRepository.deleteById(categoryId);
	}

	
	@Override
	@Transactional
	public CategoryResponse updateCategory(Long categoryId, CategoryRequest request) {
		Category existingCategory = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new CategoryNotFoundException("Category not found"));
		
		existingCategory.setName(request.getName());
		existingCategory.setDescription(request.getDescription());

		Category updateSubcategory = categoryRepository.save(existingCategory);
		CategoryResponse response = getCategoryResponse(updateSubcategory);
		return response;
	}

	@Override
	public CategoryResponse getCategoryById(Long categoryId) {
		Category existingCategory = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new CategoryNotFoundException("Category not found"));

		return getCategoryResponse(existingCategory);
	}

	@Override
	public Page<CategoryResponse> getAllCategories(Integer page, Integer size) {
		Page<Category> categories = categoryRepository.findAll(PageRequest.of(page, size));

		Page<CategoryResponse> categoryResponsePage = categories.map(category -> {
			return getCategoryResponse(category);
		});
		return categoryResponsePage;
	}
	
	@Override
	public List<CategoryResponse> getActiveCategories() {
		List<Category> categories = categoryRepository.findByActiveTrue();

		List<CategoryResponse> categoryResponsePage = categories.stream().map(category -> {
			return getCategoryResponse(category);
		}).collect(Collectors.toList());
		return categoryResponsePage;
	}

	public void changeCategoryActiveStatus(Long categoryId) {
		Category existingCategory = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new CategoryNotFoundException("Category not found"));
		
		existingCategory.setActive(!existingCategory.getActive());
		categoryRepository.save(existingCategory);
	}
	
	public void changeCategoryImageActiveStatus(Long imageId) {
		CategoryImage existingCategoryImage = categoryImageRepository.findById(imageId)
				.orElseThrow(() -> new ImageNotFoundException("Category image not found"));
		
		existingCategoryImage.setActive(!existingCategoryImage.getActive());
		categoryImageRepository.save(existingCategoryImage);
	}

	private List<ImageResponse> getImageResponse(List<CategoryImage> images) {
		List<ImageResponse> imageResponses = images.stream().map(image ->{
			ImageResponse imageResponse = ImageResponseMapper.INSTANCE.toPayload(image);
			return imageResponse;
		}).collect(Collectors.toList());
		
		return imageResponses;
	}
	

	private CategoryResponse getCategoryResponse(Category category) {
		CategoryResponse response = CategoryResponseMapper.INSTANCE.toPayload(category);			

		List<Subcategory> subcategories = category.getSubcategories();
		List<CategoryImage> categoryImages = category.getCategoryImages();
		
		List<SubcategoryMiniDetailsResponse> subcatList = subcategories.stream().map(subcat ->{
			SubcategoryMiniDetailsResponse subcategoryMiniDetailsResponse = SubcategoryMiniDetailsResponseMapper.INSTANCE.toPayload(subcat);
			return subcategoryMiniDetailsResponse;
		}).collect(Collectors.toList());
		
		List<ImageResponse> imageResponses = getImageResponse(categoryImages);
		
		response.setSubcategories(subcatList);
		response.setImages(imageResponses);
		return response;
	}
	
	@Override
	public void removeCategoryImageById(Long imageId) {
		CategoryImage existingCategoryImage = categoryImageRepository.findById(imageId).orElseThrow(() -> new ImageNotFoundException("Category image not found"));
		categoryImageRepository.deleteById(imageId);
	}
	
	@Override
	@Transactional
	public List<CategoryImage> addCategoryImage(Long categoryId, List<ImageAltText> altTextList, List<MultipartFile> imageFiles) {
		Category existingCategory = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new CategoryNotFoundException("Category not found"));
	
		List<ImageFileWithMetadata> imageFileWithMetadatas = IntStream.range(0, imageFiles.size())
			    .mapToObj(i -> {
			        ImageFileWithMetadata imageData = new ImageFileWithMetadata();
			        imageData.setImageFile(imageFiles.get(i));
			        imageData.setAltText(altTextList.get(i).getAltText());
			        return imageData;
			    })
			    .collect(Collectors.toList());
		
		List<CategoryImage> categoryImages = imageFileWithMetadatas.stream().map(image ->{
			String generatedImageUrl;
			try {
				generatedImageUrl = s3Bucket.uploadImageToS3(image.getImageFile(), "/category-images");
				System.out.println("generatedUrl" + generatedImageUrl);
			} catch (IOException e) {
				throw new ImageUploadException("Failed to upload image.");
			}
			CategoryImage categoryImage = new CategoryImage();
			categoryImage.setImageUrl(generatedImageUrl);
			categoryImage.setAltText(image.getAltText());
			categoryImage.setCategory(existingCategory);
			categoryImage.setActive(true);
			return categoryImage;
		}).collect(Collectors.toList());
		
		return categoryImageRepository.saveAll(categoryImages);
	}
	
	
	@Override
	@Transactional
	public List<CategoryImage> getAllCategoryImages(Long categoryId){
		Category existingCategory = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new CategoryNotFoundException("Category not found"));
		
		return categoryImageRepository.findAllByCategory(existingCategory);
	}
}
