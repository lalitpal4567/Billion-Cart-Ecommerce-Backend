package com.billioncart.payload;

import lombok.Data;

@Data
public class SigninRequest {
	private String username;
	private String password;
}
