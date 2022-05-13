package com.example.demo.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.payment.PaymentMethodDTO;
import com.example.demo.dto.user.AddressDTO;
import com.example.demo.service.UserService;
import com.example.demo.utility.KeycloakScopeVerifier;

@RestController
@RequestMapping("user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private KeycloakScopeVerifier scopeVerifier;

	@GetMapping("/addresses")
	@ResponseBody
	public ResponseEntity<List<AddressDTO>> getAddresses(HttpServletRequest request) {
		KeycloakAuthenticationToken principal = (KeycloakAuthenticationToken) request.getUserPrincipal();
		return ResponseEntity.ok(userService.getAddresses(userService.getUser(principal.getName())));
	}
	
	@GetMapping("/payment-methods")
	@ResponseBody
	public ResponseEntity<List<PaymentMethodDTO>> getPaymentMethods(HttpServletRequest request) {
		KeycloakAuthenticationToken principal = (KeycloakAuthenticationToken) request.getUserPrincipal();
		scopeVerifier.hasScope(principal, "read-payment-methods");
		return ResponseEntity.ok(userService.getPaymentMethods(userService.getUser(principal.getName())));
	}
}
