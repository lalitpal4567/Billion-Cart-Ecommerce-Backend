package com.billioncart.payload;

import lombok.Data;

@Data
public class SpecificationNameResponse {
	private Long nameId;
	private String name;
	private SpecificationSubcategoryResponse subcategory;
}
