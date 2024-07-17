package com.billioncart.payload;

import lombok.Data;

@Data
public class AddressResponse {
	private Long addressId;
	private String fullName;
	private String mobileNo;
	private String city;
	private String state;
	private String locationDetails;
	private String country;
	private String pincode;
	private Boolean isDefaultAddress;
}
