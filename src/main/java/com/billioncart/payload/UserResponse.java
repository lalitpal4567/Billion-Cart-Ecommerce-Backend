package com.billioncart.payload;

import lombok.Data;

@Data
public class UserResponse {
	private Long userId;
	private String firstName;
	private String lastName;
	private String dob;
	private String gender;
	private String email;
	private byte[] image;
	private String imageName;
	private String imageType;
}
