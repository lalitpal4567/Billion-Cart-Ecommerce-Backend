package com.billioncart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.billioncart.model.Account;
import java.util.Optional;


public interface AccountRepository extends JpaRepository<Account, Long>{
	Optional<Account> findByUsername(String mobileNo);
}
