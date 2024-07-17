package com.billioncart.model.productCatalogue;

import java.util.List;

import com.billioncart.audit.UserDateAudit;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Data
@Table(name = "spec_name_table")
public class SpecificationName extends UserDateAudit{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long nameId;
	
	private String name;
	
	@ManyToOne
	@JoinColumn(name = "subcategory_id")
	@JsonIgnore
	private Subcategory subcategory;
	
	@OneToMany(mappedBy = "specificationName", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<SpecificationValue> specificationValues;
}
