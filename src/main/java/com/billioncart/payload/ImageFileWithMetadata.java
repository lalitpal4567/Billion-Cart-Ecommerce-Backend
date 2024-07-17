package com.billioncart.payload;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class ImageFileWithMetadata {
	private MultipartFile imageFile;
    private String altText;
}
