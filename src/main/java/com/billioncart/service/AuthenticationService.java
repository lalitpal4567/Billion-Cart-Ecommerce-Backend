package com.billioncart.service;

import com.billioncart.payload.SigninRequest;
import com.billioncart.payload.SigninResponse;
import com.billioncart.payload.SignupRequest;

public interface AuthenticationService {
	SigninResponse signin(SigninRequest request);
	void signup(SignupRequest request);
}
