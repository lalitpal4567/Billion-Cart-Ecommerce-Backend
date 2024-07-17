package com.billioncart.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.billioncart.model.User;
import com.billioncart.payload.UserRequest;

@Mapper
public interface UserRequestMapper {
	UserRequestMapper INSTANCE = Mappers.getMapper(UserRequestMapper.class);
	
	User toEntity(UserRequest request);
}
