package com.example.demo.service;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.checkout.ShoppingCartDTO;
import com.example.demo.entity.checkout.ShoppingCart;
import com.example.demo.entity.checkout.ShoppingCartDetail;
import com.example.demo.entity.user.User;
import com.example.demo.exception.responsestatus.ConflictStatusException;
import com.example.demo.repository.NotFoundStatusException;
import com.example.demo.repository.ShoppingCartDetailRepository;
import com.example.demo.repository.ShoppingCartRepository;
import com.example.demo.utility.MyModelMapper;

@Service
public class CheckoutService {
	
	@Autowired
	private ShoppingCartRepository shoppingCartRepository;
	
	@Autowired
	private ShoppingCartDetailRepository shoppingCartDetailRepository;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MyModelMapper myModelMapper;
	
	public ShoppingCartDTO getCheckout(User user) {
		checkoutNotNull(user);
		return myModelMapper.map(user.getShoppingCart(), ShoppingCartDTO.class);
	}
	
	public ShoppingCartDTO startCheckout(List<Map<String, Object>> checkoutDetails, User user) {
		if(user.getShoppingCart() != null) throw new ConflictStatusException("The checkout process is already started");
		
		ShoppingCart shoppingCart = new ShoppingCart(user.getId(), createShoppingCartDetailsFromRequestBody(checkoutDetails));
		shoppingCart = shoppingCartRepository.save(shoppingCart);
		user.setShoppingCart(shoppingCart);
		userService.save(user);
		return myModelMapper.map(shoppingCart, ShoppingCartDTO.class);
	}
	
	public ShoppingCartDTO addProductToShoppingCart(List<Map<String, Object>> checkoutDetails, User user) {
		checkoutNotNull(user);
		List<ShoppingCartDetail> shoppingCartDetails = createShoppingCartDetailsFromRequestBody(checkoutDetails);
		List<ShoppingCartDetail> repeatedShoppingCartDetails = shoppingCartDetails.stream()
																				.filter(user.getShoppingCart()
																							.getShoppingCartDetails()::contains)
																				.collect(Collectors.toList());
		if (!repeatedShoppingCartDetails.isEmpty()) {
			throw new ConflictStatusException("The products: "
											+ repeatedShoppingCartDetails.stream()
																		.map(e -> e.getProduct().toString())
																		.collect(Collectors.toList())
											+ " are already in the checkout");
		}

		user.getShoppingCart()
			.addShoppingCartDetails(shoppingCartDetails);
		shoppingCartRepository.save(user.getShoppingCart());
		return myModelMapper.map(user.getShoppingCart(), ShoppingCartDTO.class);
	}
	
	public ShoppingCartDTO updateProductQuantity(Integer productId, Map<String, Object> productQuantity, User user) {
		checkoutNotNull(user);
		ShoppingCartDetail shoppingCartDetail = findShoppingCartDetailByProductId(productId, user);
		shoppingCartDetail.setQuantity( (Integer) productQuantity.get("quantity") );
		shoppingCartDetailRepository.save(shoppingCartDetail);
		return myModelMapper.map(user.getShoppingCart(), ShoppingCartDTO.class);
	}
	
	public ShoppingCartDTO removeProductFromShoppingCart(Integer productId, User user) {
		checkoutNotNull(user);
		ShoppingCartDetail shoppingCartDetail = findShoppingCartDetailByProductId(productId, user);
		user.getShoppingCart().removeShoppingCartDetail(shoppingCartDetail);
		shoppingCartDetailRepository.delete(shoppingCartDetail);
		
		if (user.getShoppingCart().isEmpty()) {
			ShoppingCart shoppingCart = user.getShoppingCart();
			user.setShoppingCart(null);
			shoppingCartRepository.delete(shoppingCart);
			return null;
		}
		return myModelMapper.map(user.getShoppingCart(), ShoppingCartDTO.class);
	}
	
	public ShoppingCartDTO addShippingAddress(Map<String, Object> address, Principal principal) {
		User user = userService.getUser(principal.getName());
		checkoutNotNull(user);
		user.getShoppingCart().setAddress(userService.getAddress(( (Integer) address.get("addressId") ).longValue(), principal));
		shoppingCartRepository.save(user.getShoppingCart());
		return myModelMapper.map(user.getShoppingCart(), ShoppingCartDTO.class);
	}
	
	private List<ShoppingCartDetail> createShoppingCartDetailsFromRequestBody(List<Map<String, Object>> checkoutDetails) {
		return checkoutDetails.stream()
								.map(e -> new ShoppingCartDetail(
										productService.getProduct( ((Integer) e.get("productId")).longValue() ), 
										(Integer) e.get("quantity")
									)
								).collect(Collectors.toList());
	}
	
	private ShoppingCartDetail findShoppingCartDetailByProductId(Integer productId, User user) {
		return user.getShoppingCart()
					.getShoppingCartDetails().stream()
											.filter(e -> e.getProduct().getId().equals(productId.longValue()))
											.findFirst()
											.orElseThrow();
	}
	
	private void checkoutNotNull(User user) {
		if(user.getShoppingCart() == null) throw new NotFoundStatusException("The user hasn't started the checkout process");
	}
	
}
