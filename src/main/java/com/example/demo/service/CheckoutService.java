package com.example.demo.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.checkout.ShoppingCartDTO;
import com.example.demo.entity.checkout.ShoppingCart;
import com.example.demo.entity.checkout.ShoppingCartDetail;
import com.example.demo.entity.user.User;
import com.example.demo.repository.CheckoutRepository;
import com.example.demo.utility.MyModelMapper;

@Service
public class CheckoutService {
	
	@Autowired
	private CheckoutRepository checkoutRepository;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MyModelMapper myModelMapper;
	
	public ShoppingCartDTO startCheckout(List<Map<String, Object>> checkoutDetails, User user) {
		user.setShoppingCart(new ShoppingCart(user.getId(), createShoppingCartDetailsFromRequestBody(checkoutDetails)));
		userService.save(user);
		return myModelMapper.map(user.getShoppingCart(), ShoppingCartDTO.class);
	}
	
	public ShoppingCartDTO addProductToShoppingCart(List<Map<String, Object>> checkoutDetails, User user) {
		user.getShoppingCart()
			.getShoppingCartDetails()
			.addAll(createShoppingCartDetailsFromRequestBody(checkoutDetails));
		userService.save(user);
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
}
