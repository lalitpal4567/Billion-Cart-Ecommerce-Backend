package com.billioncart.model;

import java.util.List;

import com.billioncart.audit.UserDateAudit;
import com.billioncart.model.productCatalogue.Product;
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
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Data
@Table(name = "review_table", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"user_id", "product_id"})
})
public class Review extends UserDateAudit{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long reviewId;
	
	private String title;
	
	@Column(nullable = false)
	private String comment;
	
	@Column(nullable = false)
	private Integer rating;
	
	@Column(nullable = false)
	private Integer helpfulCount;

	@ManyToOne
	@JoinColumn(name = "product_id")
	@JsonIgnore
	private Product product;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	@JsonIgnore
	private User user;
	
	@Column(nullable = false)
	private Boolean verifiedPurchase = false;
	
	@OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<ReviewImage> reviewImages;
}
