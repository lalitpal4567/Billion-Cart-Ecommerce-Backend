package com.billioncart.service.serviceImpl;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.billioncart.exception.CategoryNotFoundException;
import com.billioncart.exception.ImageUploadException;
import com.billioncart.exception.ResourceNotFoundException;
import com.billioncart.mapper.CategoryRequestMapper;
import com.billioncart.mapper.CategoryResponseMapper;
import com.billioncart.model.productCatalogue.Category;
import com.billioncart.model.productCatalogue.Subcategory;
import com.billioncart.payload.CategoryRequest;
import com.billioncart.payload.CategoryResponse;
import com.billioncart.payload.SubcategoryMiniDetailsResponse;
import com.billioncart.repository.CategoryRepository;
import com.billioncart.repository.SubcategoryRepository;
import com.billioncart.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {
	private CategoryRepository categoryRepository;
	private SubcategoryRepository subcategoryRepository;
	private AmazonS3 amazonS3;

	public CategoryServiceImpl(CategoryRepository categoryRepository, AmazonS3 amazonS3, SubcategoryRepository subcategoryRepository) {
		this.categoryRepository = categoryRepository;
		this.subcategoryRepository = subcategoryRepository;
		this.amazonS3 = amazonS3;
	}

	@Override
	public CategoryResponse addCategory(CategoryRequest request, MultipartFile imageFile) throws IOException {

		Optional<Category> existingCategory = categoryRepository.findByName(request.getName());
		String generatedImageUrl = uploadImageToS3(imageFile);

		if (!existingCategory.isPresent()) {
			Category newCategory = CategoryRequestMapper.INSTANCE.toEntity(request);
			newCategory.setImageUrl(generatedImageUrl);
			Category createdCategory = categoryRepository.save(newCategory);
			return CategoryResponseMapper.INSTANCE.toPayload(createdCategory);
		} else {
			throw new RuntimeException("Category already exists.");
		}
	}

	private String uploadImageToS3(MultipartFile imageFile) throws IOException {
		String bucketName = "billion-cart-bucket";

		String uniqueID = UUID.randomUUID().toString();
		String fileName = "category-images/" + uniqueID + imageFile.getOriginalFilename();
		String cloudFrontDomainName = "https://d3jxj5kmew5ec.cloudfront.net";

		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType(imageFile.getContentType());
		metadata.setContentLength(imageFile.getSize());

		amazonS3.putObject(new PutObjectRequest(bucketName, fileName, imageFile.getInputStream(), metadata));

		String cloudFrontUrl = cloudFrontDomainName + "/" + fileName;
		return cloudFrontUrl;
	}

	@Transactional
	@Override
	public void removeCategoryById(Long categoryId) {
		Category existingCatgory = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category does not exist."));
		categoryRepository.deleteById(categoryId);
	}

	@Override
	public CategoryResponse updateCategory(Long categoryId, CategoryRequest request, MultipartFile imageFile) {
		Category existingCategory = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new CategoryNotFoundException("Category not found"));

		String generatedImageUrl = "";
		try {
			generatedImageUrl = uploadImageToS3(imageFile);
		} catch (IOException e) {
			throw new ImageUploadException("Failed to upload image to S3");
		}

		existingCategory.setName(request.getName());
		existingCategory.setDescription(request.getDescription());
		existingCategory.setAltText(request.getAltText());
		existingCategory.setImageUrl(generatedImageUrl);

		Category updatedCategory = categoryRepository.save(existingCategory);
		return CategoryResponseMapper.INSTANCE.toPayload(updatedCategory);
	}

	@Override
	public CategoryResponse getCategoryById(Long categoryId) {
		Category existingCategory = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new CategoryNotFoundException("Category not found"));

		return CategoryResponseMapper.INSTANCE.toPayload(existingCategory);
	}

	@Override
	public Page<CategoryResponse> getAllCategories(Integer page, Integer size) {
		Page<Category> categories = categoryRepository.findAll(PageRequest.of(page, size));

		Page<CategoryResponse> categoryResponsePage = categories.map(cat -> {
			CategoryResponse response = CategoryResponseMapper.INSTANCE.toPayload(cat);
			List<Subcategory> subcategories = subcategoryRepository.findAllByCategory(cat);
			
			return getCategoryResponse(response, subcategories);
		});
		return categoryResponsePage;
	}
	
	private CategoryResponse getCategoryResponse(CategoryResponse response, List<Subcategory> subcategories) {
		List<SubcategoryMiniDetailsResponse> subcatList = subcategories.stream().map(subcat ->{
			SubcategoryMiniDetailsResponse subcategoryMiniDetailsResponse = new SubcategoryMiniDetailsResponse();
			subcategoryMiniDetailsResponse.setSubcategoryId(subcat.getSubcategoryId());
			subcategoryMiniDetailsResponse.setName(subcat.getName());
			return subcategoryMiniDetailsResponse;
		}).collect(Collectors.toList());
		
		response.setSubcategories(subcatList);
		return response;
	}
}
