package com.billioncart.service.serviceImpl;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.billioncart.exception.CartNotFoundException;
import com.billioncart.mapper.UserRequestMapper;
import com.billioncart.mapper.UserResponseMapper;
import com.billioncart.model.Account;
import com.billioncart.model.Cart;
import com.billioncart.model.User;
import com.billioncart.payload.UserRequest;
import com.billioncart.payload.UserResponse;
import com.billioncart.repository.AccountRepository;
import com.billioncart.repository.UserRepository;
import com.billioncart.service.UserService;

@Service
public class UserServiceImpl implements UserService{
	private UserRepository userRepository;
	private AccountRepository accountRepository;

	
	public UserServiceImpl(UserRepository userRepository, AccountRepository accountRepository) {
		this.userRepository = userRepository;
		this.accountRepository = accountRepository;
	}
	
	@Override
	public UserResponse getUserProfileInformation() {
		User existingUser = getAssociatedUser();
		UserResponse userResponse = UserResponseMapper.INSTANCE.toPayload(existingUser);
		return userResponse;
	}
	
	private User getAssociatedUser() {
		String username = UserDetailsUtils.getAuthenticatedUsername();
		Account existingAccount = accountRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Account not found"));

		User existingUser = existingAccount.getUser();
		return existingUser;
	}
	
	@Override
	public void updateProfilePicture(MultipartFile imageFile) {		 	
		 System.out.println("seraj");
		 User existingUser = getAssociatedUser();
		 try {			
			 if (imageFile.isEmpty()) {
		            throw new IllegalArgumentException("File is empty");
		        }
			 System.out.println("amit");
		        if (!Arrays.asList("image/jpeg", "image/jpg", "image/png").contains(imageFile.getContentType())) {
		            throw new IllegalArgumentException("File type not supported");
		        }
		        System.out.println("raju");
		        byte[] imageData = imageFile.getBytes();
		        existingUser.setImage(imageData);
		        System.out.println("rohit");
		        existingUser.setImageName(imageFile.getOriginalFilename());
		        existingUser.setImageType(imageFile.getContentType());
		        System.out.println("aakash");
		        userRepository.save(existingUser);
		    }  catch (EOFException e) {
	            // Log the exception
	            e.printStackTrace();
	            // Handle the exception as needed (e.g., retry mechanism, notify admin, etc.)
	            throw new RuntimeException("Error processing file upload: " + e.getMessage(), e);
	        }catch (IOException e) {
		        System.err.println("Error reading image file: " + e.getMessage());
		    }
		 
		 userRepository.save(existingUser);
	}

	@Override
	public UserResponse updateProfile(UserRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

}
