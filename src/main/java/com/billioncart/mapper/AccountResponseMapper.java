package com.billioncart.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.billioncart.model.Account;
import com.billioncart.payload.AccountResponse;

@Mapper
public interface AccountResponseMapper {
	AccountResponseMapper INSTANCE = Mappers.getMapper(AccountResponseMapper.class);
	
	AccountResponse toPayload(Account account);
}
	