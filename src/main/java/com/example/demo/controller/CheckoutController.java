package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import com.example.demo.service.UserService;

@RestController
@RequestMapping("checkout")
public class CheckoutController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CheckoutService checkoutService;
	
	@GetMapping("/")
	@ResponseBody
	public ResponseEntity<ShoppingCartDTO> getCheckout(HttpServletRequest request){
		KeycloakAuthenticationToken principal = (KeycloakAuthenticationToken) request.getUserPrincipal();
		return ResponseEntity.ok(checkoutService.getCheckout(userService.getUser(principal.getName())));
	}
	
	@PostMapping("/")
	@ResponseBody
	public ResponseEntity<ShoppingCartDTO> startCheckout(@RequestBody List<Map<String, Object>> orderDetails, HttpServletRequest request){
		KeycloakAuthenticationToken principal = (KeycloakAuthenticationToken) request.getUserPrincipal();
		return new ResponseEntity<>(checkoutService.startCheckout(orderDetails, userService.getUser(principal.getName())), HttpStatus.CREATED);
	}
	
	@PutMapping("/products")
	@ResponseBody
	public ResponseEntity<ShoppingCartDTO> addProductToShoppingCart(@RequestBody List<Map<String, Object>> orderDetails, HttpServletRequest request) {
		KeycloakAuthenticationToken principal = (KeycloakAuthenticationToken) request.getUserPrincipal();
		return ResponseEntity.ok(checkoutService.addProductToShoppingCart(orderDetails, userService.getUser(principal.getName())));
	}
	
	@PutMapping("/products/{productId}")
	public ResponseEntity<ShoppingCartDTO> updateProductQuantity(@PathVariable Integer productId, @RequestBody Map<String, Object> productQuantity, HttpServletRequest request) {
		KeycloakAuthenticationToken principal = (KeycloakAuthenticationToken) request.getUserPrincipal();
		return ResponseEntity.ok(checkoutService.updateProductQuantity(productId, productQuantity, userService.getUser(principal.getName())));
	}
	
	@DeleteMapping("/products/{productId}")
	public ResponseEntity<ShoppingCartDTO> removeProductFromShoppingCart(@PathVariable Integer productId, HttpServletRequest request){
		KeycloakAuthenticationToken principal = (KeycloakAuthenticationToken) request.getUserPrincipal();
		return ResponseEntity.ok(checkoutService.removeProductFromShoppingCart(productId, userService.getUser(principal.getName())));
	}
	
	@PutMapping("/address")
	@ResponseBody
	public ResponseEntity<ShoppingCartDTO> addShippingAddress(@RequestBody Map<String, Object> address, HttpServletRequest request) {
		return ResponseEntity.ok(checkoutService.addShippingAddress(address, request.getUserPrincipal()));
	}
	
}
