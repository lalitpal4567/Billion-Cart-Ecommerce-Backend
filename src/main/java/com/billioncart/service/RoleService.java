package com.billioncart.service;

import java.util.List;

import com.billioncart.model.Role;
import com.billioncart.payload.AccountRoleRequest;
import com.billioncart.payload.AccountRoleResponse;

public interface RoleService {
	Role addRole(Role newRole);
	AccountRoleResponse assignRole(AccountRoleRequest request);
	AccountRoleResponse removeAssignedRole(AccountRoleRequest request);
	public List<Role> getAllRoles();
}
