package com.billioncart.service.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.billioncart.exception.AddressNotFoundException;
import com.billioncart.mapper.AddressRequestMapper;
import com.billioncart.mapper.AddressResponseMapper;
import com.billioncart.model.Account;
import com.billioncart.model.Address;
import com.billioncart.payload.AddressRequest;
import com.billioncart.payload.AddressResponse;
import com.billioncart.repository.AccountRepository;
import com.billioncart.repository.AddressRepository;
import com.billioncart.service.AddressService;

@Service
public class AddressServiceImpl implements AddressService{
	private AddressRepository addressRepository;
	private AccountRepository accountRepository;
	
	public AddressServiceImpl(AddressRepository addressRepository, AccountRepository accountRepository) {
		this.addressRepository = addressRepository;
		this.accountRepository = accountRepository;
	}
	
	public AddressResponse addAddress(AddressRequest request) {
		String username = UserDetailsUtils.getAuthenticatedUsername();
		
		   // find the account by username
        Account existingAccount = accountRepository.findByUsername(username)
        		.orElseThrow(() -> new UsernameNotFoundException("Account not found"));        
        
        Address newAddress = AddressRequestMapper.INSTANCE.toEntity(request);
        newAddress.setUser(existingAccount.getUser());
        newAddress.setIsDefaultAddress(false);
        Address createdAddress = addressRepository.save(newAddress);
        
        return AddressResponseMapper.INSTANCE.toPayload(createdAddress);
	}
	
	@Transactional
	public void removeAddress(Long addressId) {
		String username = UserDetailsUtils.getAuthenticatedUsername();
		
		 // find the account by username
        Account existingAccount = accountRepository.findByUsername(username)
        		.orElseThrow(() -> new UsernameNotFoundException("Account not found"));
        
        Address existingAddress = addressRepository.findById(addressId).orElseThrow(() -> new AddressNotFoundException("Address not found"));
        
        if(existingAddress.getUser().getUserId() == existingAccount.getUser().getUserId()) {
        	addressRepository.removeByUserAndAddressId(existingAccount.getUser(), addressId);        	
        }else {
        	throw new AddressNotFoundException("Address not found");
        }
	}
	
	@Override
	public AddressResponse updateAddress(Long AddressId, AddressRequest request) {
		Address existingAddress = addressRepository.findById(AddressId).orElseThrow(() -> new AddressNotFoundException("Address not found"));
		
		Address address = AddressRequestMapper.INSTANCE.toEntity(request);
		address.setAddressId(existingAddress.getAddressId());
		address.setUser(existingAddress.getUser());
		
		Address updatedAddress = addressRepository.save(address);
		return AddressResponseMapper.INSTANCE.toPayload(updatedAddress);
	}
	
	@Transactional
	@Override
	public List<AddressResponse> getAddressesByUser(){
		String username = UserDetailsUtils.getAuthenticatedUsername();
		
		Account existingAccount = accountRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Account not found"));
		
		List<Address> addresses = addressRepository.findAllByUser(existingAccount.getUser());
		
		return addresses.stream().map(addr ->{
			AddressResponse response = AddressResponseMapper.INSTANCE.toPayload(addr);
			return response;
		}).collect(Collectors.toList());
	}
}
