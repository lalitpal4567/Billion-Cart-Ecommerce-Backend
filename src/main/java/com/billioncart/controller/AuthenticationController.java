package com.billioncart.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.billioncart.payload.SigninRequest;
import com.billioncart.payload.SigninResponse;
import com.billioncart.payload.SignupRequest;
import com.billioncart.service.AuthenticationService;

@RestController
@RequestMapping("api/v1/auth")
public class AuthenticationController {
	private AuthenticationService authenticationService;
	
	public AuthenticationController(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}
	
	@PostMapping("/signin")
	public ResponseEntity<Map<String, Object>> signin(@RequestBody SigninRequest request){
		Map<String, Object> res = new LinkedHashMap<>();
		
		try {
			SigninResponse signinResponse = authenticationService.signin(request);
			res.put("message", "Login successfully");
			res.put("Token", signinResponse);
			return ResponseEntity.status(HttpStatus.OK).body(res);
		} catch (Exception e) {
			res.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(res);
		}
	}
	
	@PostMapping("/signup")
	public ResponseEntity<Map<String, String>> signup(@RequestBody SignupRequest request){
		Map<String, String> res = new LinkedHashMap<>();
		
		try {
			authenticationService.signup(request);
			res.put("message", "Account created successfully");
			return ResponseEntity.status(HttpStatus.CREATED).body(res);
		} catch (Exception e) {
			res.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
		}
	}
}
