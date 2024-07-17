package com.billioncart.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SpecificationNameRequest {
	private String name;
	
	public SpecificationNameRequest(String name) {
        this.name = name;
    }
}
