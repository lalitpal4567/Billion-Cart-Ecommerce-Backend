package com.billioncart.service.serviceImpl;

import java.io.IOException;
import java.util.List;
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

import com.billioncart.exception.CarouselImageNotFoundException;
import com.billioncart.exception.ImageUploadException;
import com.billioncart.exception.SubcategoryNotFoundException;
import com.billioncart.mapper.CarouselImageRequestMapper;
import com.billioncart.model.CarouselImage;
import com.billioncart.model.productCatalogue.Subcategory;
import com.billioncart.model.productCatalogue.SubcategoryImage;
import com.billioncart.payload.CarouselImageRequest;
import com.billioncart.payload.ImageAltText;
import com.billioncart.payload.ImageFileWithMetadata;
import com.billioncart.repository.CarouselImageRepository;
import com.billioncart.service.CarouselImageService;

@Service
public class CarouselImageServiceImpl implements CarouselImageService {
	private CarouselImageRepository carouselImageRepository;
	private AmazonS3 amazonS3;

	public CarouselImageServiceImpl(CarouselImageRepository carouselImageRepository, AmazonS3 amazonS3) {
		this.carouselImageRepository = carouselImageRepository;
		this.amazonS3 = amazonS3;
	}

	
	@Override
	@Transactional
	public List<CarouselImage> addCarouselImage(List<ImageAltText> altTextList, List<MultipartFile> imageFiles) {
		
		List<ImageFileWithMetadata> imageFileWithMetadatas = IntStream.range(0, imageFiles.size())
			    .mapToObj(i -> {
			        ImageFileWithMetadata imageData = new ImageFileWithMetadata();
			        imageData.setImageFile(imageFiles.get(i));
			        imageData.setAltText(altTextList.get(i).getAltText());
			        return imageData;
			    })
			    .collect(Collectors.toList());
		
		List<CarouselImage> carouselImages = imageFileWithMetadatas.stream().map(image ->{
			String generatedImageUrl;
			try {
				generatedImageUrl = uploadImageToS3(image.getImageFile());
			} catch (IOException e) {
				throw new ImageUploadException("Failed to upload image.");
			}
			CarouselImage carouselImage = new CarouselImage();
			carouselImage.setImageUrl(generatedImageUrl);
			carouselImage.setAltText(image.getAltText());
			carouselImage.setActive(true);
			return carouselImage;
		}).collect(Collectors.toList());
		
		return carouselImageRepository.saveAll(carouselImages);
	}

	private String uploadImageToS3(MultipartFile imageFile) throws IOException {
		String bucketName = "billion-cart-bucket";

		String uniqueID = UUID.randomUUID().toString();
		String fileName = "carousel-images/" + uniqueID + imageFile.getOriginalFilename();
		String cloudFrontDomainName = "https://d3jxj5kmew5ec.cloudfront.net";

		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType(imageFile.getContentType());
		metadata.setContentLength(imageFile.getSize());

		amazonS3.putObject(new PutObjectRequest(bucketName, fileName, imageFile.getInputStream(), metadata));

		String cloudFrontUrl = cloudFrontDomainName + "/" + fileName;
		return cloudFrontUrl;
	}
	
	@Override
	public CarouselImage updateActiveStatus(Long imageId, Boolean active) {
		CarouselImage carouselImage = carouselImageRepository.findById(imageId)
				.orElseThrow(() -> new CarouselImageNotFoundException("Image not found"));
		carouselImage.setActive(active);
		return carouselImageRepository.save(carouselImage);
	}

	@Override
	public Page<CarouselImage> getActiveImages(Integer page, Integer size) {
		return carouselImageRepository.findByActive(true, PageRequest.of(page, size));
	}

	@Override
	public Page<CarouselImage> getAllImages(Integer page, Integer size) {
		return carouselImageRepository.findAll(PageRequest.of(page, size));
	}

	@Override
	public void removeCarouselImage(Long carouselId) {
		CarouselImage existingCarouselImage = carouselImageRepository.findById(carouselId)
				.orElseThrow(() -> new CarouselImageNotFoundException("Carousel image not found"));

		carouselImageRepository.deleteById(carouselId);
	}
}
