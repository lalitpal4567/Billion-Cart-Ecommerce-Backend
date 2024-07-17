package com.billioncart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.billioncart.model.Address;
import com.billioncart.model.User;

public interface AddressRepository extends JpaRepository<Address, Long>{
	void removeByUserAndAddressId(User user, Long addressId);
	List<Address> findAllByUser(User user);
}
