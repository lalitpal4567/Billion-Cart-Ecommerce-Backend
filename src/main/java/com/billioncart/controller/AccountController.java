package com.billioncart.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.billioncart.payload.AccountResponse;
import com.billioncart.payload.UserRequest;
import com.billioncart.payload.UserResponse;
import com.billioncart.service.AccountService;

@RestController
@RequestMapping("/api/v1")
public class AccountController {
	private AccountService accountService;
	
	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}
	
	@GetMapping("/admin/users-list")
	public Page<AccountResponse> getAllUsers(@RequestParam(name = "page", defaultValue = "0") Integer page,
			@RequestParam(name = "size", defaultValue = "5") Integer size) {
		return accountService.getAllAccounts(page, size);
	}
	
	@PostMapping("/user/update-profile")
	public ResponseEntity<Map<String, Object>> updateProfile(@RequestBody  UserRequest request) {
		Map<String, Object> res = new LinkedHashMap<>();

		try {
			UserResponse response = accountService.updateProfile(request);
			res.put("message", "User profile updated successfully");
			res.put("User", response);
			return ResponseEntity.status(HttpStatus.OK).body(res);
		} catch (Exception e) {
			res.put("error", e.getMessage());
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.OK).body(res);
		}
	}
	
	@GetMapping("/admin/get-account/{id}")
	public ResponseEntity<Map<String, Object>> getUserById(@PathVariable(name = "id") Long accountId) {
		Map<String, Object> res = new LinkedHashMap<>();

		try {
			AccountResponse accountResponse = accountService.getAccountById(accountId);
			res.put("message", "User found successfully");
			res.put("Account", accountResponse);
			return ResponseEntity.status(HttpStatus.OK).body(res);
		} catch (Exception e) {
			res.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(res);
		}
	}
}
