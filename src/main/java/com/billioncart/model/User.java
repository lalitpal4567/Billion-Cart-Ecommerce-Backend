package com.billioncart.model;

import java.util.ArrayList;
import java.util.List;

import com.billioncart.audit.DateAudit;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Data
@Table(name = "user_table")
public class User extends DateAudit{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long UserId;
	
	private String firstName;
	private String lastName;
	private String dob;
	private String gender;
	private String email;
	
	@Lob
	@Column(name = "profile_picture", columnDefinition = "LONGBLOB")
    private byte[] image;
	
	private String imageName;
	private String imageType;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Review> reviews = new ArrayList<>();
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "cart_id")
	@JsonIgnore
	private Cart cart;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "wishlist_id")
	@JsonIgnore
	private Wishlist wishlist;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Address> addresses = new ArrayList<>();
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<Order> orders = new ArrayList<>();
	
}
