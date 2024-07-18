package com.billioncart.model.productCatalogue;

import java.util.ArrayList;
import java.util.List;

import com.billioncart.audit.UserDateAudit;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Data
@Table(name = "subcategory_table")
public class Subcategory extends UserDateAudit{
	@Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	private Long subcategoryId;
	
	@Column(nullable = false, unique = true)
	private String name;
	
	@Column(nullable = false)
	private String description;
	
	@Column(nullable = false)
	private Boolean active;
	
	@ManyToOne
	@JoinColumn(name = "category_id")
	@JsonIgnore
	private Category category;
		
	@OneToMany(mappedBy = "subcategory", cascade = CascadeType.ALL)
	private List<SubcategoryImage> subcategoryImages = new ArrayList<>();
	
	@OneToMany(mappedBy = "subcategory", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Product> products = new ArrayList<>();
	
	@OneToMany(mappedBy = "subcategory", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<SpecificationName> specificationNames = new ArrayList<>();
}
