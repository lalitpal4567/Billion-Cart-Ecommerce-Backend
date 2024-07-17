package com.billioncart.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.billioncart.repository.AccountRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	private AccountRepository accountRepository;
	
	public UserDetailsServiceImpl(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return accountRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("user not found"));
	}
}
