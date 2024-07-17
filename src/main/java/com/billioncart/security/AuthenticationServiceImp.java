package com.billioncart.security;

import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.billioncart.exception.ResourceNotFoundException;
import com.billioncart.model.Account;
import com.billioncart.model.Cart;
import com.billioncart.model.Role;
import com.billioncart.model.User;
import com.billioncart.model.Wishlist;
import com.billioncart.payload.SigninRequest;
import com.billioncart.payload.SigninResponse;
import com.billioncart.payload.SignupRequest;
import com.billioncart.repository.AccountRepository;
import com.billioncart.repository.RoleRepository;
import com.billioncart.repository.UserRepository;
import com.billioncart.service.AuthenticationService;

@Service
public class AuthenticationServiceImp implements AuthenticationService {
	private AccountRepository accountRepository;
	private PasswordEncoder passwordEncoder;
	private RoleRepository roleRepository;
	private AuthenticationManager authenticationManager;
	private JwtService jwtService;
	private UserRepository userRepository;

	public AuthenticationServiceImp(AccountRepository accountRepository, PasswordEncoder passwordEncoder,
			RoleRepository roleRepository, AuthenticationManager authenticationManager, JwtService jwtService, UserRepository userRepository) {
		this.accountRepository = accountRepository;
		this.passwordEncoder = passwordEncoder;
		this.roleRepository = roleRepository;
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
		this.userRepository = userRepository;

	}

	public SigninResponse signin(SigninRequest request) {
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
		Account accountFound = accountRepository.findByUsername(request.getUsername())
				.orElseThrow(() -> new IllegalArgumentException("invalid username or password"));
		String token = jwtService.generateToken(accountFound);
		
		SigninResponse signinResponse = new SigninResponse();
		signinResponse.setToken(token);
		signinResponse.setRoles(accountFound.getRoles());
		
		return signinResponse;
	}

	@Override
	public void signup(SignupRequest request) {
		Account newAccount = new Account();
		User userProfile = new User();
		Cart newCart = new Cart();
		Wishlist newWishlist = new Wishlist();
		
		userProfile.setCart(newCart);
		userProfile.setCart(newCart);
		userProfile.setWishlist(newWishlist);
		newAccount.setUsername(request.getUsername());
		newAccount.setPassword(passwordEncoder.encode(request.getPassword()));
		newAccount.setUser(userProfile);

		// Fetch the role from the repository
		Role userRole = roleRepository.findByRole("USER")
				.orElseThrow(() -> new ResourceNotFoundException("Role not found"));

		// Assign roles to the new account
		newAccount.setRoles(List.of(userRole));

		// Save the new account to the repository
		accountRepository.save(newAccount);

	}

}
