package com.billioncart.service.serviceImpl;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Component
public class S3Bucket {
	@Autowired
	private  AmazonS3 amazonS3 ;

	public String uploadImageToS3(MultipartFile imageFile, String basePath) throws IOException {
		String bucketName = "billion-cart-bucket";

		String uniqueID = UUID.randomUUID().toString();
		String fileName = basePath + uniqueID + imageFile.getOriginalFilename();
		String cloudFrontDomainName = "https://d3jxj5kmew5ec.cloudfront.net";

		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType(imageFile.getContentType());
		metadata.setContentLength(imageFile.getSize());
		
		System.out.println("woei");

		amazonS3.putObject(new PutObjectRequest(bucketName, fileName, imageFile.getInputStream(), metadata));

		String cloudFrontUrl = cloudFrontDomainName + "/" + fileName;
		return cloudFrontUrl;
	}
}
