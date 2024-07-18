package com.billioncart.service.serviceImpl;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.billioncart.exception.CategoryNotFoundException;
import com.billioncart.exception.ImageNotFoundException;
import com.billioncart.exception.ImageUploadException;
import com.billioncart.exception.ResourceNotFoundException;
import com.billioncart.exception.SubcategoryNotFoundException;
import com.billioncart.mapper.CategoryResponseMapper;
import com.billioncart.mapper.SpecificationNameResponseMapper;
import com.billioncart.mapper.SubcategoryRequestMapper;
import com.billioncart.mapper.SubcategoryResponseMapper;
import com.billioncart.model.productCatalogue.Category;
import com.billioncart.model.productCatalogue.Subcategory;
import com.billioncart.model.productCatalogue.SubcategoryImage;
import com.billioncart.payload.CategoryMiniDetailsResponse;
import com.billioncart.payload.CategoryResponse;
import com.billioncart.payload.ImageAltText;
import com.billioncart.payload.ImageFileWithMetadata;
import com.billioncart.payload.SpecificationNameResponse;
import com.billioncart.payload.SubcategoryRequest;
import com.billioncart.payload.SubcategoryResponse;
import com.billioncart.repository.CategoryRepository;
import com.billioncart.repository.SubcategoryImageRepository;
import com.billioncart.repository.SubcategoryRepository;
import com.billioncart.service.SubcategoryService;


@Service
public class SubcategoryServiceImpl implements SubcategoryService {
	private SubcategoryRepository subcategoryRepository;
	private CategoryRepository categoryRepository;
	private SubcategoryImageRepository subcategoryImageRepository;
	private AmazonS3 amazonS3;

	public SubcategoryServiceImpl(SubcategoryRepository subcategoryRepository, CategoryRepository categoryRepository,
			SubcategoryImageRepository subcategoryImageRepository, AmazonS3 amazonS3) {
		this.subcategoryRepository = subcategoryRepository;
		this.subcategoryImageRepository = subcategoryImageRepository;
		this.categoryRepository = categoryRepository;
		this.amazonS3 = amazonS3;
	}

	@Override
	@Transactional
	public SubcategoryResponse addSubcategory(Long categoryId, SubcategoryRequest request,
			List<MultipartFile> imageFiles) {
		
		System.out.println("red");
		System.out.println("name: " + request.getName());
		
		System.out.println("description: "+ request.getDescription());
		
		Category existingCategory = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category does not exist"));

		Optional<Subcategory> existingSubcategory = subcategoryRepository.findByNameIgnoreCase(request.getName());
		if (existingSubcategory.isPresent()) {
			throw new ResourceNotFoundException("Subcategory already exists.");
		}

		System.out.println("white");
		Subcategory newSubcategory = SubcategoryRequestMapper.INSTANCE.toEntity(request);
		newSubcategory.setCategory(existingCategory);
		newSubcategory.setActive(true);

		System.out.println("blue");
		List<ImageAltText> altTextList = request.getAltTexts();
		
		List<ImageFileWithMetadata> imageFileWithMetadatas = IntStream.range(0, imageFiles.size())
			    .mapToObj(i -> {
			        ImageFileWithMetadata imageData = new ImageFileWithMetadata();
			        imageData.setImageFile(imageFiles.get(i));
			        imageData.setAltText(altTextList.get(i).getAltText());
			        return imageData;
			    })
			    .collect(Collectors.toList());
		
		System.out.println("green");
		
		List<SubcategoryImage> subcategoryImages = imageFileWithMetadatas.stream().map(image ->{
			String generatedImageUrl;
			try {
				generatedImageUrl = uploadImageToS3(image.getImageFile());
			} catch (IOException e) {
				throw new ImageUploadException("Failed to upload image.");
			}
			SubcategoryImage subcategoryImage = new SubcategoryImage();
			subcategoryImage.setImageUrl(generatedImageUrl);
			subcategoryImage.setAltText(image.getAltText());
			subcategoryImage.setSubcategory(newSubcategory);
			subcategoryImage.setActive(true);
			return subcategoryImage;
			
		}).collect(Collectors.toList());

	    newSubcategory.setSubcategoryImages(subcategoryImages);
		Subcategory updatedSubcategory = subcategoryRepository.save(newSubcategory);
		SubcategoryResponse response = SubcategoryResponseMapper.INSTANCE.toPayload(updatedSubcategory);
//		CategoryResponse categoryResponse = CategoryResponseMapper.INSTANCE.toPayload(existingCategory);
		
		CategoryMiniDetailsResponse categoryMiniDetailsResponse = new CategoryMiniDetailsResponse();
		categoryMiniDetailsResponse.setCategoryId(existingCategory.getCategoryId());
		categoryMiniDetailsResponse.setName(existingCategory.getName());
		response.setCategory(categoryMiniDetailsResponse);
		return response;
	}
	

	private String uploadImageToS3(MultipartFile imageFile) throws IOException {
		String bucketName = "billion-cart-bucket";

		String uniqueID = UUID.randomUUID().toString();
		String fileName = "subcategory-images/" + uniqueID + imageFile.getOriginalFilename();
		String cloudFrontDomainName = "https://d3jxj5kmew5ec.cloudfront.net";

		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType(imageFile.getContentType());
		metadata.setContentLength(imageFile.getSize());

		amazonS3.putObject(new PutObjectRequest(bucketName, fileName, imageFile.getInputStream(), metadata));

		String cloudFrontUrl = cloudFrontDomainName + "/" + fileName;
		return cloudFrontUrl;
	}

	@Override
	public void removeSubcategoryById(Long subcategoryId) {
		Subcategory existingSubcategory = subcategoryRepository.findById(subcategoryId)
				.orElseThrow(() -> new SubcategoryNotFoundException("Subcategory not found"));
		subcategoryRepository.deleteById(subcategoryId);
	}
	

	@Override
	@Transactional
	public SubcategoryResponse updateSubcategory(Long subcategoryId, SubcategoryRequest request) {
		Subcategory existingSubcategory = subcategoryRepository.findById(subcategoryId)
				.orElseThrow(() -> new SubcategoryNotFoundException("Subcategory not found"));

		Subcategory subcategory = SubcategoryRequestMapper.INSTANCE.toEntity(request);
		subcategory.setSubcategoryId(existingSubcategory.getSubcategoryId());
		subcategory.setCategory(existingSubcategory.getCategory());

		subcategory.setProducts(existingSubcategory.getProducts());
		subcategory.setSubcategoryImages(existingSubcategory.getSubcategoryImages());

		Subcategory updateSubcategory = subcategoryRepository.save(subcategory);
		SubcategoryResponse response = SubcategoryResponseMapper.INSTANCE.toPayload(updateSubcategory);
		return response;
	}

	
	@Override
	@Transactional
	public List<SubcategoryImage> addSubcategoryImage(Long subcategoryId, List<ImageAltText> altTextList, List<MultipartFile> imageFiles) {
		Subcategory existingSubcategory = subcategoryRepository.findById(subcategoryId)
				.orElseThrow(() -> new SubcategoryNotFoundException("Subcategory not found"));
		
		List<ImageFileWithMetadata> imageFileWithMetadatas = IntStream.range(0, imageFiles.size())
			    .mapToObj(i -> {
			        ImageFileWithMetadata imageData = new ImageFileWithMetadata();
			        imageData.setImageFile(imageFiles.get(i));
			        imageData.setAltText(altTextList.get(i).getAltText());
			        return imageData;
			    })
			    .collect(Collectors.toList());
		
		List<SubcategoryImage> subcategoryImages = imageFileWithMetadatas.stream().map(image ->{
			String generatedImageUrl;
			try {
				generatedImageUrl = uploadImageToS3(image.getImageFile());
			} catch (IOException e) {
				throw new ImageUploadException("Failed to upload image.");
			}
			SubcategoryImage subcategoryImage = new SubcategoryImage();
			subcategoryImage.setImageUrl(generatedImageUrl);
			subcategoryImage.setAltText(image.getAltText());
			subcategoryImage.setSubcategory(existingSubcategory);
			subcategoryImage.setActive(true);
			return subcategoryImage;
		}).collect(Collectors.toList());
		
		return subcategoryImageRepository.saveAll(subcategoryImages);
	}
	
	@Override
	public void removeSubcategoryImageById(Long imageId) {
		SubcategoryImage existingSubcategoryImage = subcategoryImageRepository.findById(imageId).orElseThrow(() -> new ImageNotFoundException("Subcategory image not found"));
		subcategoryImageRepository.deleteById(imageId);
	}
	
	@Override
	public SubcategoryResponse getSubcategoryById(Long subcategoryId) {
		Subcategory existingSubcategory = subcategoryRepository.findById(subcategoryId)
				.orElseThrow(() -> new SubcategoryNotFoundException("Subcategory not found"));

		SubcategoryResponse subcategoryResponse = SubcategoryResponseMapper.INSTANCE.toPayload(existingSubcategory);
		subcategoryResponse.setSpecificationName(getAllSpecificationNames(existingSubcategory));
		System.out.println("one");
		return subcategoryResponse;
	}

	
	@Override
	@Transactional
	public Page<SubcategoryResponse> getAllSubcategories(Integer page, Integer size) {
		Page<Subcategory> subcategories = subcategoryRepository.findAll(PageRequest.of(page, size));

		Page<SubcategoryResponse> subcategoryRespPage = subcategories.map(subcat -> {
			SubcategoryResponse response = SubcategoryResponseMapper.INSTANCE.toPayload(subcat);
			return response;
		});
		return subcategoryRespPage;
	}
	
	@Override
	@Transactional
	public List<SubcategoryResponse> getActiveSubcategoriesByCategoryId(Long categoryId) {
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new CategoryNotFoundException("Category not found"));
		List<Subcategory> subcategories = subcategoryRepository.findByCategoryAndActiveTrue(category);

		List<SubcategoryResponse> subcategoryResponsees = subcategories.stream().map(subcat -> {
			SubcategoryResponse response = SubcategoryResponseMapper.INSTANCE.toPayload(subcat);
			return response;
		}).collect(Collectors.toList());
		
		return subcategoryResponsees;
	}
	
	@Override
	public void changeSubcategoryActiveStatus(Long subcategoryId) {
		Subcategory existingSubcategory = subcategoryRepository.findById(subcategoryId)
				.orElseThrow(() -> new SubcategoryNotFoundException("Subcategory not found"));
		
		existingSubcategory.setActive(!existingSubcategory.getActive());
		subcategoryRepository.save(existingSubcategory);
	}
	
	@Override
	public void changeSubcategoryImageActiveStatus(Long imageId) {
		SubcategoryImage existingSubcategoryImage = subcategoryImageRepository.findById(imageId).orElseThrow(() -> new ImageNotFoundException("Subcategory image not found"));
		
		existingSubcategoryImage.setActive(!existingSubcategoryImage.getActive());
		subcategoryImageRepository.save(existingSubcategoryImage);
	}
	
	@Override
	@Transactional
	public List<SubcategoryImage> getAllSubcategoryImages(Long subcategoryId){
		Subcategory existingSubcategory = subcategoryRepository.findById(subcategoryId)
				.orElseThrow(() -> new SubcategoryNotFoundException("Subcategory not found"));
		
		return subcategoryImageRepository.findAllBySubcategory(existingSubcategory);
	}
	
	private List<SpecificationNameResponse> getAllSpecificationNames(Subcategory subcat) {
		System.out.println("two");
		List<SpecificationNameResponse> specificationNames = subcat.getSpecificationNames().stream().map(name -> {
			SpecificationNameResponse specificationNameResponse = SpecificationNameResponseMapper.INSTANCE
					.toPayload(name);
			return specificationNameResponse;
		}).collect(Collectors.toList());
		System.out.println(specificationNames);
		return specificationNames;
	}

	@Override
	@Transactional
	public Page<SubcategoryResponse> getSubcategoriesByCategoryId(Integer page, Integer size, Long categoryId) {
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new CategoryNotFoundException("Category not found"));
		Page<Subcategory> subcatePage = subcategoryRepository.findAllByCategory(PageRequest.of(page, size), category);

		Page<SubcategoryResponse> subcategoryRespPage = subcatePage.map(subcat -> {
			SubcategoryResponse response = SubcategoryResponseMapper.INSTANCE.toPayload(subcat);
			CategoryMiniDetailsResponse categoryMiniDetailsResponse = new CategoryMiniDetailsResponse();
			categoryMiniDetailsResponse.setCategoryId(categoryId);
			categoryMiniDetailsResponse.setName(category.getName());
			return response;
		});
		return subcategoryRespPage;
	}

	@Override
	@Transactional
	public Page<SubcategoryResponse> getsubcategoriesByQuery(Integer page, Integer size, String query) {
		Page<Subcategory> subcategoryPage = subcategoryRepository.findAllByNameContaining(query,
				PageRequest.of(page, size));

		Page<SubcategoryResponse> subcategoryRespPage = subcategoryPage.map(subcat -> {
			SubcategoryResponse response = SubcategoryResponseMapper.INSTANCE.toPayload(subcat);
			return response;
		});
		return subcategoryRespPage;
	}
}
