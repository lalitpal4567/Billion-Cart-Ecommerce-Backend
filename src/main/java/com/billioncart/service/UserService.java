package com.billioncart.service;

import org.springframework.web.multipart.MultipartFile;

import com.billioncart.payload.UserRequest;
import com.billioncart.payload.UserResponse;

public interface UserService {
	UserResponse updateProfile(UserRequest request);
	
	UserResponse getUserProfileInformation();
	
	void updateProfilePicture(MultipartFile imageFile);
}
