package com.billioncart.payload;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import com.billioncart.model.Role;

import lombok.Data;

@Data
public class AccountResponse {
	private Long accountId;
	private String username;
	private String firstName;
	private String lastName;
	private String dob;
	private String email;
	private byte[] image;
	private String imageName;
	private Long cartId;
	private Long wishlistId;
	private List<Role> roles;
	private boolean isAccountNonExpired;
	private boolean isAccountNonLocked;
	private boolean isCredentialNonExpired;
	private boolean isUserNonEnabled;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
