package com.billioncart.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.billioncart.model.Address;
import com.billioncart.payload.AddressRequest;

@Mapper
public interface AddressRequestMapper {
	AddressRequestMapper INSTANCE = Mappers.getMapper(AddressRequestMapper.class);
	
	Address toEntity(AddressRequest request);
}
