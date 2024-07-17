package com.billioncart.payload;

import java.util.List;

import com.billioncart.model.Role;

import lombok.Data;

@Data
public class SigninResponse {
	private String token;
	private List<Role> roles;
}
