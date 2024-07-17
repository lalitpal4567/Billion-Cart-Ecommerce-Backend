package com.billioncart.service;

import org.springframework.data.domain.Page;

import com.billioncart.payload.AccountResponse;
import com.billioncart.payload.UserRequest;
import com.billioncart.payload.UserResponse;

public interface AccountService{
	Page<AccountResponse> getAllAccounts(Integer page, Integer size);
	
	UserResponse updateProfile(UserRequest request);
		
	AccountResponse getAccountById(Long accountId);

}
