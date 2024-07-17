package com.billioncart.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.billioncart.payload.UserResponse;
import com.billioncart.service.UserService;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
	private UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping("/user-profile-info")
	public ResponseEntity<Map<String, Object>> getUserProfileInformation() {
		Map<String, Object> res = new LinkedHashMap<>();

		try {
			UserResponse userResponse = userService.getUserProfileInformation();
			res.put("message", "User profile found successfully");
			res.put("Profile", userResponse);
			return ResponseEntity.status(HttpStatus.OK).body(res);
		} catch (Exception e) {
			res.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(res);
		}
	}
	
	@PostMapping("/update-profile-picture")
	public ResponseEntity<Map<String, Object>> updateProfilePicture(@RequestPart("imageFile") MultipartFile imageFile){
		Map<String, Object> res = new LinkedHashMap<>();
		System.out.println("lalit");
		
		try {
			userService.updateProfilePicture(imageFile);
			res.put("message", "User profile picture updated successfully");
			return ResponseEntity.status(HttpStatus.OK).body(res);
		} catch (Exception e) {
			res.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(res);
		}
	}
}
