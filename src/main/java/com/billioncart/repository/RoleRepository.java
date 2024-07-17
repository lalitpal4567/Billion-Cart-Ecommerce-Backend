package com.billioncart.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.billioncart.model.Role;


public interface RoleRepository extends JpaRepository<Role, Long>{
	Optional<Role> findByRole(String role);
}
