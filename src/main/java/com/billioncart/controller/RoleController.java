package com.billioncart.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.billioncart.model.Role;
import com.billioncart.payload.AccountRoleRequest;
import com.billioncart.payload.AccountRoleResponse;
import com.billioncart.service.RoleService;

@RestController
@RequestMapping("/api/v1/admin")
public class RoleController {
	private RoleService roleService;

	public RoleController(RoleService roleService) {
		this.roleService = roleService;
	}

	@PostMapping("/add-role")
	public ResponseEntity<Map<String, Object>> addRole(@RequestBody Role newRole) {
		Map<String, Object> res = new HashMap<>();

		try {
			Role createdRole = roleService.addRole(newRole);
			res.put("message", "Role added successfully");
			res.put("Role", createdRole);
			return ResponseEntity.status(HttpStatus.CREATED).body(res);
		} catch (Exception e) {
			res.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
		}
	}

	@PostMapping("/assign-role")
	public ResponseEntity<Map<String, Object>> addRole(@RequestBody AccountRoleRequest request) {
		Map<String, Object> res = new HashMap<>();

		try {
			AccountRoleResponse response = roleService.assignRole(request);
			res.put("message", "A new role has been assigned");
			res.put("AccountRole", response);
			return ResponseEntity.status(HttpStatus.CREATED).body(res);
		} catch (Exception e) {
			res.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
		}
	}

	@PostMapping("/remove-assigned-role")
	public ResponseEntity<Map<String, Object>> removeAssignedRole(@RequestBody AccountRoleRequest request) {
		Map<String, Object> res = new HashMap<>();

		try {
			AccountRoleResponse response = roleService.removeAssignedRole(request);
			res.put("message", "Assigned role has been removed successfully");
			res.put("AccountRole", response);
			return ResponseEntity.status(HttpStatus.OK).body(res);
		} catch (Exception e) {
			res.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(res);
		}
	}

	@GetMapping("/role-list")
	public ResponseEntity<Map<String, Object>> getAllRoles() {
		Map<String, Object> res = new HashMap<>();

		List<Role> list = roleService.getAllRoles();
		res.put("Roles", list);
		return ResponseEntity.status(HttpStatus.CREATED).body(res);
	}
	
	
}
