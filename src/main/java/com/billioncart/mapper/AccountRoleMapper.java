package com.billioncart.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.billioncart.model.Account;
import com.billioncart.payload.AccountRoleResponse;

@Mapper
public interface AccountRoleMapper {
	AccountRoleMapper INSTANCE = Mappers.getMapper(AccountRoleMapper.class);
	
	AccountRoleResponse toPayload(Account account);
}
