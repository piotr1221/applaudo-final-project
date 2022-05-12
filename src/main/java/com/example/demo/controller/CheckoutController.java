package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.checkout.ShoppingCartDTO;
import com.example.demo.service.CheckoutService;
import com.example.demo.service.ProductService;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("checkout")
public class CheckoutController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private CheckoutService checkoutService;
	
	@PostMapping("/")
	@ResponseBody
	public ResponseEntity<ShoppingCartDTO> getCheckout(@RequestBody List<Map<String, Object>> orderDetails, HttpServletRequest request){
		KeycloakAuthenticationToken principal = (KeycloakAuthenticationToken) request.getUserPrincipal();
		return ResponseEntity.ok(checkoutService.startCheckout(orderDetails, userService.getUser(principal.getName())));
	}
	
	@PutMapping("/")
	@ResponseBody
	public ResponseEntity<ShoppingCartDTO> addProductToShoppingCart(@RequestBody List<Map<String, Object>> orderDetails, HttpServletRequest request) {
		KeycloakAuthenticationToken principal = (KeycloakAuthenticationToken) request.getUserPrincipal();
		return ResponseEntity.ok(checkoutService.addProductToShoppingCart(orderDetails, userService.getUser(principal.getName())));
	}
	
	@PutMapping("/products/{productId}")
	public ResponseEntity<ShoppingCartDTO> updateProductQuantity(@PathVariable Integer productId, @RequestBody Map<String, Object> productQuantity, HttpServletRequest request) {
		KeycloakAuthenticationToken principal = (KeycloakAuthenticationToken) request.getUserPrincipal();
		return ResponseEntity.ok(checkoutService.updateProductQuantity(productId, productQuantity,userService.getUser(principal.getName())));
	}
	
}
