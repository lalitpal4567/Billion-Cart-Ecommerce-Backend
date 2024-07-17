package com.billioncart.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ColorRequest {
	private String name;
	
	public ColorRequest(String name) {
		this.name = name;
	}
}
