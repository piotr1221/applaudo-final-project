package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.checkout.ShoppingCartDTO;
import com.example.demo.entity.checkout.Product;
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
	private MyModelMapper myModelMapper;
	
	public ShoppingCartDTO startCheckout(List<Map<String, Object>> orderDetails, User username) {
		List<ShoppingCartDetail> shoppingCartDetails = new ArrayList<>();
		for (int i = 0; i < orderDetails.size(); i++) {
			shoppingCartDetails.add(new ShoppingCartDetail());
			shoppingCartDetails.get(i).setProduct(productService.getProduct( ((Integer) orderDetails.get(i).get("productId")).longValue() ));
			shoppingCartDetails.get(i).setQuantity((Integer) orderDetails.get(i).get("quantity"));
			shoppingCartDetails.get(i).setSubtotal(shoppingCartDetails.get(i).getProduct().getPrice() * shoppingCartDetails.get(i).getQuantity());
		}
		
		ShoppingCart shoppingCart = new ShoppingCart();
		shoppingCart.setId(username.getId());
		shoppingCart.setShoppingCartDetails(shoppingCartDetails);
		
		shoppingCart = checkoutRepository.save(shoppingCart);
		return myModelMapper.map(shoppingCart, ShoppingCartDTO.class);
	}
}
