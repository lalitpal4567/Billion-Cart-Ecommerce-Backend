package com.billioncart.service.serviceImpl;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.billioncart.exception.AccountNotFoundException;
import com.billioncart.mapper.AccountResponseMapper;
import com.billioncart.mapper.UserRequestMapper;
import com.billioncart.mapper.UserResponseMapper;
import com.billioncart.model.Account;
import com.billioncart.model.User;
import com.billioncart.payload.AccountResponse;
import com.billioncart.payload.UserRequest;
import com.billioncart.payload.UserResponse;
import com.billioncart.repository.AccountRepository;
import com.billioncart.repository.UserRepository;
import com.billioncart.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService {
	private AccountRepository accountRepository;
	private UserRepository userRepository;

	public AccountServiceImpl(AccountRepository accountRepository, UserRepository userRepository) {
		this.accountRepository = accountRepository;
		this.userRepository = userRepository;
	}

	@Override
	public Page<AccountResponse> getAllAccounts(Integer page, Integer size) {
		Page<Account> accounts = accountRepository.findAll(PageRequest.of(page, size));

		Page<AccountResponse> accountResponsePage = accounts.map(account -> {
			AccountResponse response = getAccountResponse(account);
			return response;
		});
		return accountResponsePage;
	}

	private AccountResponse getAccountResponse(Account account) {
		AccountResponse response = AccountResponseMapper.INSTANCE.toPayload(account);
		User user = account.getUser();
		response.setDob(user.getDob());
		response.setEmail(user.getEmail());
		response.setFirstName(user.getFirstName());
		response.setLastName(user.getLastName());
		response.setImage(user.getImage());
		response.setImageName(user.getImageName());
		response.setCartId(user.getCart().getCartId());
		response.setWishlistId(user.getWishlist().getWishlistId());
		response.setRoles(account.getRoles());

		return response;
	}

	@Override
	public UserResponse updateProfile(UserRequest request) {
		// Get the authenticated user's username
		String username = UserDetailsUtils.getAuthenticatedUsername();
		// find the account by username
		Account existingAccount = accountRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Account not found"));

		// find user by user id
		User existingUser = userRepository.findById(existingAccount.getUser().getUserId())
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		// convert request payload to User entity
		User user = UserRequestMapper.INSTANCE.toEntity(request);

		// Preserve important fields
		user.setUserId(existingUser.getUserId());
		user.setCart(existingUser.getCart());
		user.setWishlist(existingUser.getWishlist());

		// save user to user repository
		User updatedUser = userRepository.save(user);

		// convert saved user entity back to userResponse payload
		return UserResponseMapper.INSTANCE.toPayload(user);
	}

	@Override
	public AccountResponse getAccountById(Long accountId) {
		Account existingAccount = accountRepository.findById(accountId).orElseThrow(() -> new AccountNotFoundException("Account not found"));
		return getAccountResponse(existingAccount);
	}
}
