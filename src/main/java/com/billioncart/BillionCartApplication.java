package com.billioncart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.billioncart.service.serviceImpl.WishlistServiceImpl;

@SpringBootApplication
public class BillionCartApplication {

	public static void main(String[] args) {
		SpringApplication.run(BillionCartApplication.class, args);
	}
}
