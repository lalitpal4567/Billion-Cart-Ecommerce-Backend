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
public class AddressServiceImpl implements AddressService {
	private AddressRepository addressRepository;
	private AccountRepository accountRepository;

	public AddressServiceImpl(AddressRepository addressRepository, AccountRepository accountRepository) {
		this.addressRepository = addressRepository;
		this.accountRepository = accountRepository;
	}

	@Override
	public AddressResponse addAddress(AddressRequest request) {
		String username = UserDetailsUtils.getAuthenticatedUsername();

		Account existingAccount = accountRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Account not found"));

		Address newAddress = AddressRequestMapper.INSTANCE.toEntity(request);
		newAddress.setUser(existingAccount.getUser());
		newAddress.setIsDefaultAddress(false);
		Address createdAddress = addressRepository.save(newAddress);

		return AddressResponseMapper.INSTANCE.toPayload(createdAddress);
	}

	@Override
	@Transactional
	public void removeAddress(Long addressId) {
		String username = UserDetailsUtils.getAuthenticatedUsername();

		Account existingAccount = accountRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Account not found"));

		Address existingAddress = addressRepository.findByUserAndAddressId(existingAccount.getUser(), addressId)
				.orElseThrow(() -> new AddressNotFoundException("Address not found"));

		addressRepository.removeByUserAndAddressId(existingAccount.getUser(), addressId);

	}

	@Override
	public AddressResponse updateAddress(Long addressId, AddressRequest request) {

		String username = UserDetailsUtils.getAuthenticatedUsername();

		Account existingAccount = accountRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Account not found"));

		Address existingAddress = addressRepository.findByUserAndAddressId(existingAccount.getUser(), addressId)
				.orElseThrow(() -> new AddressNotFoundException("Address not found"));

		Address address = AddressRequestMapper.INSTANCE.toEntity(request);
		address.setAddressId(existingAddress.getAddressId());
		address.setUser(existingAddress.getUser());
		address.setIsDefaultAddress(false);

		Address updatedAddress = addressRepository.save(address);
		return AddressResponseMapper.INSTANCE.toPayload(updatedAddress);
	}
	
	@Override
	public AddressResponse getUserAddressById(Long addressId) {
		String username = UserDetailsUtils.getAuthenticatedUsername();

		Account existingAccount = accountRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Account not found"));

		Address existingAddress = addressRepository.findByUserAndAddressId(existingAccount.getUser(), addressId)
				.orElseThrow(() -> new AddressNotFoundException("Address not found"));
		
		AddressResponse addressResponse = AddressResponseMapper.INSTANCE.toPayload(existingAddress);
		return addressResponse;
	}

	@Override
	public void changeToDefaultAddress(Long addressId) {
		String username = UserDetailsUtils.getAuthenticatedUsername();

		Account existingAccount = accountRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Account not found"));

        List<Address> addresses = addressRepository.findByUser(existingAccount.getUser());
        for (Address address : addresses) {
            if (address.getIsDefaultAddress() != null && address.getIsDefaultAddress()) {
                address.setIsDefaultAddress(false);
                addressRepository.save(address);
            }
        }
        
        Address existingAddress = addressRepository.findByUserAndAddressId(existingAccount.getUser(), addressId)
        		.orElseThrow(() -> new AddressNotFoundException("Address not found"));
        
        existingAddress.setIsDefaultAddress(true);
        addressRepository.save(existingAddress);
	}
	
	
	@Override
	@Transactional
	public List<AddressResponse> getUserAddress() {
		String username = UserDetailsUtils.getAuthenticatedUsername();

		Account existingAccount = accountRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Account not found"));

		List<Address> addresses = addressRepository.findAllByUser(existingAccount.getUser());

		return addresses.stream().map(addr -> {
			AddressResponse response = AddressResponseMapper.INSTANCE.toPayload(addr);
			return response;
		}).collect(Collectors.toList());
	}
}
