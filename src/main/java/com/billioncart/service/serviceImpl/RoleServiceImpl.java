package com.billioncart.service.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.billioncart.exception.ResourceNotFoundException;
import com.billioncart.mapper.AccountRoleMapper;
import com.billioncart.model.Account;
import com.billioncart.model.Role;
import com.billioncart.payload.AccountRoleRequest;
import com.billioncart.payload.AccountRoleResponse;
import com.billioncart.repository.AccountRepository;
import com.billioncart.repository.RoleRepository;
import com.billioncart.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {
	private RoleRepository roleRepository;
	private AccountRepository accountRepository;

	public RoleServiceImpl(RoleRepository roleRepository, AccountRepository accountRepository) {
		this.roleRepository = roleRepository;
		this.accountRepository = accountRepository;
	}

	@Override
	public Role addRole(Role newRole) {
		newRole.setRole(newRole.getRole().toUpperCase());
		
		Optional<Role> existingRole = roleRepository.findByRole(newRole.getRole());
		
		if(!existingRole.isPresent()) {
			return roleRepository.save(newRole);			
		}else {
			throw new ResourceNotFoundException("Role already exists.");
		}
	}

	@Override
	public AccountRoleResponse assignRole(AccountRoleRequest request) {
		Account existingAccount = accountRepository.findById(request.getAccountId())
				.orElseThrow(() -> new ResourceNotFoundException("Account not found"));
		Role existingRole = roleRepository.findById(request.getRoleId())
				.orElseThrow(() -> new ResourceNotFoundException("Role not found"));

		List<Role> list = existingAccount.getRoles();
		list.add(existingRole);
		existingAccount.setRoles(list);
		Account updateAccount = accountRepository.save(existingAccount);
		return AccountRoleMapper.INSTANCE.toPayload(updateAccount);
	}

	@Override
	public AccountRoleResponse removeAssignedRole(AccountRoleRequest request) {
		Account existingAccount = accountRepository.findById(request.getAccountId())
				.orElseThrow(() -> new ResourceNotFoundException("Account not found"));
		Role existingRole = roleRepository.findById(request.getRoleId())
				.orElseThrow(() -> new ResourceNotFoundException("Role not found"));
		
		// user role is not allowed to be deleted
		if(existingRole.getRole().equals("USER")) {
			throw new RuntimeException("User role is not allowed to be removed");
		}

		// get list of all assigned roles to a user
		List<Role> assignedRoles = existingAccount.getRoles();
		
		// check if a user has a assigned role
		Optional<Role> roleFound = assignedRoles.stream()
				.filter(role -> role.getRoleId().equals(existingRole.getRoleId())).findFirst();
		
		// if user has a assigned role then remove it
		if(roleFound.isPresent()) {
			assignedRoles.remove(roleFound.get());
			existingAccount.setRoles(assignedRoles);
			Account updatedAccount = accountRepository.save(existingAccount);
			return AccountRoleMapper.INSTANCE.toPayload(updatedAccount);
		}else {
			throw new ResourceNotFoundException("No such role has been assgined");
		}
	}
	
	@Override
	public List<Role> getAllRoles(){
		return roleRepository.findAll();
	}
}
