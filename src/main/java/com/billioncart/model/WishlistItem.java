package com.billioncart.model;

import com.billioncart.audit.UserDateAudit;
import com.billioncart.model.productCatalogue.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Data
@Table(name = "wishlistItem_table", 
				uniqueConstraints = @UniqueConstraint(columnNames = {"wishlist_id", "product_id"}))
public class WishlistItem extends UserDateAudit{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long wishlistItemId;
	
	@ManyToOne
	@JoinColumn(name = "wishlist_id")
	@JsonIgnore
	private Wishlist wishlist;
	
	@ManyToOne
	@JoinColumn(name = "product_id")
	@JsonIgnore
	private Product product;
}