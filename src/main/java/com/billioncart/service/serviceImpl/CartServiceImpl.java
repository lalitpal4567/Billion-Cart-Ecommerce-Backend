package com.billioncart.service.serviceImpl;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.billioncart.exception.CartItemNotFoundException;
import com.billioncart.exception.CartNotFoundException;
import com.billioncart.exception.NotEnoughQuantityException;
import com.billioncart.exception.ProductNotFoundException;
import com.billioncart.exception.ProductOutOfStockException;
import com.billioncart.mapper.CartItemResponseMapper;
import com.billioncart.model.Account;
import com.billioncart.model.Cart;
import com.billioncart.model.CartItem;
import com.billioncart.model.productCatalogue.Product;
import com.billioncart.payload.CartItemResponse;
import com.billioncart.repository.AccountRepository;
import com.billioncart.repository.CartItemRepository;
import com.billioncart.repository.CartRepository;
import com.billioncart.repository.ProductRepository;
import com.billioncart.service.CartService;

@Service
public class CartServiceImpl implements CartService {
	private CartRepository cartRepository;
	private ProductRepository productRepository;
	private AccountRepository accountRepository;
	private CartItemRepository cartItemRepository;

	public CartServiceImpl(CartRepository cartRepository, ProductRepository productRepository,
			AccountRepository accountRepository, CartItemRepository cartItemRepository) {
		this.cartRepository = cartRepository;
		this.productRepository = productRepository;
		this.accountRepository = accountRepository;
		this.cartItemRepository = cartItemRepository;
	}

	@Override
	public void addToCart(Long productId) {
		Cart associatedCart = getAssociatedCart();
		Product existingProduct = productRepository.findById(productId)
				.orElseThrow(() -> new ProductNotFoundException("Product not found"));

		Optional<CartItem> existingCartItem = cartItemRepository.findByCartAndProduct(associatedCart, existingProduct);

		if (existingProduct.getQuantity() <= 0) {
			throw new ProductOutOfStockException("Product is out of stock.");
		}

		if (existingCartItem.isPresent()) {
			CartItem cartItem = existingCartItem.get();

			if (existingProduct.getQuantity() == cartItem.getQuantity()) {
				throw new NotEnoughQuantityException("Quantity is insufficient.");
			}

			cartItem.setQuantity(cartItem.getQuantity() + 1);
			cartItemRepository.save(cartItem);

			associatedCart.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
			cartRepository.save(associatedCart);
			throw new RuntimeException("Product is already present");
		}

		// add new cartItem if it not present
		CartItem newCartItem = new CartItem();
		newCartItem.setCart(associatedCart);
		newCartItem.setProduct(existingProduct);
		newCartItem.setQuantity(1L);
		newCartItem.setIsSelectedForOrder(true);
		cartItemRepository.save(newCartItem);

		// update the Timestamp of the associated cart
		associatedCart.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		associatedCart.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
		cartRepository.save(associatedCart);
	}

	@Override
	@Transactional
	public void changeCartItemQuantity(Long quantity, Long cartItemId) {
		System.out.println("red");
		Cart associatedCart = getAssociatedCart();

		System.out.println("white");
		CartItem existingCartItem = cartItemRepository.findByCartItemIdAndCart(cartItemId, associatedCart)
				.orElseThrow(() -> new CartItemNotFoundException("Cart item not found"));

		System.out.println("blue");
		if (existingCartItem.getProduct().getQuantity() < quantity) {
			throw new NotEnoughQuantityException("Enough quantity is not avaible");
		}

		System.out.println("green");
		existingCartItem.setQuantity(quantity);
		associatedCart.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
		cartItemRepository.save(existingCartItem);

		System.out.println("pink");
		cartRepository.save(associatedCart);
	}

	@Override
	public void removeCartItem(Long cartItemId) {
		Cart associatedCart = getAssociatedCart();
		CartItem existingCartItem = cartItemRepository.findByCartItemIdAndCart(cartItemId, associatedCart)
				.orElseThrow(() -> new CartItemNotFoundException("Cart item not found"));

		cartItemRepository.delete(existingCartItem);

		Cart existingCart = getAssociatedCart();

		if (existingCart.getCartItems().isEmpty()) {
			existingCart.setCreatedAt(null);
			existingCart.setUpdatedAt(null);
			cartRepository.save(existingCart);
			return;
		}

		existingCart.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
		cartRepository.save(existingCart);
	}

	@Override
	public void toggleCartItemForOrder(Long cartItemId) {
		Cart associatedCart = getAssociatedCart();
		CartItem existingCartItem = cartItemRepository.findByCartItemIdAndCart(cartItemId, associatedCart)
				.orElseThrow(() -> new CartItemNotFoundException("Cart item not found"));

		existingCartItem.setIsSelectedForOrder(!existingCartItem.getIsSelectedForOrder());

		cartItemRepository.save(existingCartItem);
	}

	@Override
	@Transactional
	public List<CartItemResponse> getCartItemResponse() {
		Cart associatedCart = getAssociatedCart();

		List<CartItem> cartItems = associatedCart.getCartItems();

		List<CartItemResponse> cartItemResponses = cartItems.stream().map(item -> {
			Product product = productRepository.findById(item.getProduct().getProductId())
					.orElseThrow(() -> new ProductNotFoundException("Product not found"));
			CartItemResponse cartItemResponse = CartItemResponseMapper.INSTANCE.toPayload(product);

			cartItemResponse.setCartId(associatedCart.getCartId());
			cartItemResponse.setCartItemId(item.getCartItemId());
			cartItemResponse.setQuantity(item.getQuantity());
			cartItemResponse.setIsSelectedForOrder(item.getIsSelectedForOrder());
			return cartItemResponse;
		}).collect(Collectors.toList());

		return cartItemResponses;

	}

	private Cart getAssociatedCart() {
		String username = UserDetailsUtils.getAuthenticatedUsername();
		Account existingAccount = accountRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Account not found"));

		Cart existingCart = cartRepository.findById(existingAccount.getUser().getCart().getCartId())
				.orElseThrow(() -> new CartNotFoundException("Cart not found"));

		return existingCart;
	}
}
