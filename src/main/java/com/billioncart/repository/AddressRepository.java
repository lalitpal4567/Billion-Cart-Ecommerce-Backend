package com.billioncart.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.billioncart.model.Address;
import com.billioncart.model.User;

public interface AddressRepository extends JpaRepository<Address, Long>{
	
	void removeByUserAndAddressId(User user, Long addressId);
	
	Optional<Address> findByUserAndAddressId(User user, Long addressId);
	
	List<Address> findByUser(User user);
	
	List<Address> findAllByUser(User user);
}
