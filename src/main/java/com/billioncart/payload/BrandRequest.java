package com.billioncart.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BrandRequest {
	private String name;
	
	public BrandRequest(String name) {
		this.name = name;
	}
}
