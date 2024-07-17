package com.billioncart.model.productCatalogue;

import java.util.List;

import com.billioncart.audit.UserDateAudit;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Data
@Table(name = "category_table")
public class Category extends UserDateAudit{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long categoryId;
	
	@Column(nullable = false, unique = true)
	private String name;
	
	@Column(nullable = false)
	private String description;
	
	@Column(name = "img_url")
	private String imageUrl;
	
	@Column(nullable = false)
	private String altText;
	
	@Column(nullable = false)
	private Boolean active;
	
	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Subcategory> subcategories;
	
	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
	private List<CategoryImage> categoryImages;
}
