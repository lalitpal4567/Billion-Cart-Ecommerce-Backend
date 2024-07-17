package com.billioncart.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.billioncart.model.User;
import com.billioncart.payload.UserResponse;

@Mapper
public interface UserResponseMapper {
	UserResponseMapper INSTANCE = Mappers.getMapper(UserResponseMapper.class);
	
	UserResponse toPayload(User user);
}
