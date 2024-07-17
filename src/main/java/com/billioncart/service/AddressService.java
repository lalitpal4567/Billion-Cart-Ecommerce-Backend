package com.billioncart.service;

import java.util.List;

import com.billioncart.payload.AddressRequest;
import com.billioncart.payload.AddressResponse;

public interface AddressService {
	AddressResponse addAddress(AddressRequest request);
	
	void removeAddress(Long addressId);
	
	AddressResponse updateAddress(Long AddressId, AddressRequest request);
	
	List<AddressResponse> getAddressesByUser();
}
