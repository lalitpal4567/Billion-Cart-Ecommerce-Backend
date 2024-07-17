package com.billioncart.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.billioncart.model.Account;
import com.billioncart.payload.SignupRequest;

@Mapper
public interface SignupRequestMapper {
	SignupRequestMapper INSTANCE = Mappers.getMapper(SignupRequestMapper.class);
	
	Account toEntity(SignupRequest request);
}
