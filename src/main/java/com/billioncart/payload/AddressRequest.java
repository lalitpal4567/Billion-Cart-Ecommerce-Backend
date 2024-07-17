package com.billioncart.payload;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class AddressRequest {
	private String fullName;
	private String mobileNo;
	private int street;
	private String city;
	private String state;
	private String country;
	private String pincode;
	private Boolean isDefaultAddress;
}
