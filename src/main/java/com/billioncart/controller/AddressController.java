package com.billioncart.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.billioncart.payload.AddressRequest;
import com.billioncart.payload.AddressResponse;
import com.billioncart.service.AddressService;

@RestController
@RequestMapping("/api/v1/user")
public class AddressController {
	private AddressService addressService;
	
	public AddressController(AddressService addressService) {
		this.addressService = addressService;
	}
	
	@PostMapping("/add-address")
	public ResponseEntity<Map<String, Object>> addAddress(@RequestBody  AddressRequest request) {
		Map<String, Object> res = new LinkedHashMap<>();

		try {
			AddressResponse createdAddress = addressService.addAddress(request);
			res.put("message", "Address added successfully");
			res.put("Address", createdAddress);
			return ResponseEntity.status(HttpStatus.CREATED).body(res);
		} catch (Exception e) {
			res.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(res);
		}
	}
	
	@DeleteMapping("/remove-address/{id}")
	public ResponseEntity<Map<String, Object>> removeAddress(@PathVariable(name = "id") Long addressId) {
		Map<String, Object> res = new LinkedHashMap<>();

		try {
			addressService.removeAddress(addressId);
			res.put("message", "Address removed successfully");
			return ResponseEntity.status(HttpStatus.OK).body(res);
		} catch (Exception e) {
			res.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(res);
		}
	}
	
	@PutMapping("/update-address/{id}")
	public ResponseEntity<Map<String, Object>> updateAddress(@PathVariable(name = "id") Long addressId, @RequestBody  AddressRequest request) {
		Map<String, Object> res = new LinkedHashMap<>();

		try {
			AddressResponse updatedAddress = addressService.updateAddress(addressId, request);
			res.put("message", "Address updated successfully");
			res.put("Address", updatedAddress);
			return ResponseEntity.status(HttpStatus.OK).body(res);
		} catch (Exception e) {
			res.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(res);
		}
	}
	
	@GetMapping("/addresses")
	public ResponseEntity<Map<String, Object>> getAddressByUser() {
		Map<String, Object> res = new LinkedHashMap<>();

		try {
			List<AddressResponse> addresses = addressService.getAddressesByUser();
			res.put("Address", addresses);
			return ResponseEntity.status(HttpStatus.OK).body(res);
		} catch (Exception e) {
			res.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.OK).body(res);
		}
	}
	
}
