package com.billioncart.service.serviceImpl;

import java.sql.Timestamp;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.billioncart.exception.CartNotFoundException;
import com.billioncart.exception.ProductNotFoundException;
import com.billioncart.exception.WishlistItemNotFoundException;
import com.billioncart.exception.WishlistNotFoundException;
import com.billioncart.mapper.WishlistItemResponseMapper;
import com.billioncart.model.Account;
import com.billioncart.model.Cart;
import com.billioncart.model.Wishlist;
import com.billioncart.model.WishlistItem;
import com.billioncart.model.productCatalogue.Product;
import com.billioncart.payload.ProductResponse;
import com.billioncart.payload.WishlistItemResponse;
import com.billioncart.repository.AccountRepository;
import com.billioncart.repository.ProductRepository;
import com.billioncart.repository.WishlistItemRepository;
import com.billioncart.repository.WishlistRepository;
import com.billioncart.service.WishlistService;

@Service
public class WishlistServiceImpl implements WishlistService {
	private WishlistRepository wishlistRepository;
	private ProductRepository productRepository;
	private AccountRepository accountRepository;
	private WishlistItemRepository wishlistItemRepository;

	public WishlistServiceImpl(WishlistRepository wishlistRepository, ProductRepository productRepository,
			AccountRepository accountRepository, WishlistItemRepository wishlistItemRepository) {
		this.wishlistRepository = wishlistRepository;
		this.productRepository = productRepository;
		this.accountRepository = accountRepository;
		this.wishlistItemRepository = wishlistItemRepository;
	}

	@Override
	public void addToWishlist(Long productId) {
		Wishlist existingWishlist = getAssociatedWishlist();
		
		Product existingProduct = productRepository.findById(productId)
				.orElseThrow(() -> new ProductNotFoundException("Product not found"));

		Optional<WishlistItem> wishlistItem = wishlistItemRepository
				.findWishlistItemByWishlistAndProduct(existingWishlist, existingProduct);

		if (wishlistItem.isEmpty()) {
			WishlistItem newWishlistItem = new WishlistItem();
			newWishlistItem.setProduct(existingProduct);
			newWishlistItem.setWishlist(existingWishlist);

			if (existingWishlist.getWishlistItems().isEmpty()) {
				existingWishlist.setCreatedAt(new Timestamp(System.currentTimeMillis()));
			}
			wishlistItemRepository.save(newWishlistItem);

			existingWishlist.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
			wishlistRepository.save(existingWishlist);
		} else {
			throw new RuntimeException("Product already exists.");
		}
	}

	@Override
	public void removeFromWishlist(Long wishlistItemId) {
		String username = UserDetailsUtils.getAuthenticatedUsername();

		Account existingAccount = accountRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Account not found"));
		Wishlist existingWishlist = wishlistRepository.findById(existingAccount.getUser().getWishlist().getWishlistId())
				.orElseThrow(() -> new WishlistNotFoundException("Wishlist not found"));

		WishlistItem existingWishlistItem = wishlistItemRepository
				.findByWishlistItemIdAndWishlist(wishlistItemId, existingWishlist)
				.orElseThrow(() -> new WishlistItemNotFoundException("Wishlist item not found"));

		wishlistItemRepository.deleteById(wishlistItemId);

		if (existingWishlist.getWishlistItems().isEmpty()) {
			existingWishlist.setCreatedAt(null);
			existingWishlist.setUpdatedAt(null);
			wishlistRepository.save(existingWishlist);
		}
	}

	@Override
	@Transactional
	public Page<WishlistItemResponse> getAllWishlistItems(Integer page, Integer size) {
		String username = UserDetailsUtils.getAuthenticatedUsername();

		Account existingAccount = accountRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Account not found"));
		Wishlist existingWishlist = wishlistRepository.findById(existingAccount.getUser().getWishlist().getWishlistId())
				.orElseThrow(() -> new WishlistNotFoundException("Wishlist not found"));

		Page<WishlistItem> wishlistItemPage = wishlistItemRepository.findAllByWishlist(PageRequest.of(page, size),
				existingWishlist);

		ProductServiceImpl objImpl = new ProductServiceImpl();
		Page<WishlistItemResponse> wishlistItemResPage = wishlistItemPage.map(item -> {
			WishlistItemResponse wishlistItemResponse = WishlistItemResponseMapper.iNSTANCE.toPayload(item.getProduct());
			wishlistItemResponse.setWishlistId(existingWishlist.getWishlistId());
			wishlistItemResponse.setWishlistItemId(item.getWishlistItemId());
			return wishlistItemResponse;
		});
		return wishlistItemResPage;
	}

	private Wishlist getAssociatedWishlist() {
		String username = UserDetailsUtils.getAuthenticatedUsername();
		Account existingAccount = accountRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Account not found"));

		Wishlist existingWishlist = wishlistRepository.findById(existingAccount.getUser().getWishlist().getWishlistId())
				.orElseThrow(() -> new WishlistNotFoundException("Wishlist not found"));
		return existingWishlist;
	}
}
