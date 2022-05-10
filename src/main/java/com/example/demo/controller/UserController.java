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

import com.example.demo.dto.AddressDTO;
import com.example.demo.dto.PaymentMethodDTO;
import com.example.demo.service.UserService;
import com.example.demo.utility.KeycloakScopeVerifier;

@RestController
@RequestMapping("user")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	KeycloakScopeVerifier scopeVerifier;

	@GetMapping("/addresses")
	@ResponseBody
	public ResponseEntity<List<AddressDTO>> getAddresses(HttpServletRequest request) {
		KeycloakAuthenticationToken principal = (KeycloakAuthenticationToken) request.getUserPrincipal();
		scopeVerifier.hasScope(principal, "read-user-addresses");
		return ResponseEntity.ok(userService.getAddresses(principal.getName()));
	}
	
	@GetMapping("/payment-methods")
	@ResponseBody
	public ResponseEntity<List<PaymentMethodDTO>> getPaymentMethods(HttpServletRequest request) {
		KeycloakAuthenticationToken principal = (KeycloakAuthenticationToken) request.getUserPrincipal();
		scopeVerifier.hasScope(principal, "read-payment-methods");
		return ResponseEntity.ok(userService.getPaymentMethods(principal.getName()));
	}
}
