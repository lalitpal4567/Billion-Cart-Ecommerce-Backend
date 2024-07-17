package com.billioncart.payload;

import java.util.List;

import com.billioncart.model.Role;

import lombok.Data;

@Data
public class AccountRoleResponse {
	private Long accountId;
	private List<Role> roles;
}
