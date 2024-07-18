package com.billioncart.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "carousel_img_table")
public class CarouselImage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long imageUrlId;
	
	@Column(nullable = false)
	private String imageUrl;
	
	@Column(nullable = false)
	private String altText;
	
	@Column(nullable = false)
	private Boolean active;
}
