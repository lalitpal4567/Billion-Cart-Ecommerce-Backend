package com.billioncart.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.billioncart.model.Address;
import com.billioncart.payload.AddressResponse;

@Mapper
public interface AddressResponseMapper {
	AddressResponseMapper INSTANCE = Mappers.getMapper(AddressResponseMapper.class);
	
	AddressResponse toPayload(Address address);
}
