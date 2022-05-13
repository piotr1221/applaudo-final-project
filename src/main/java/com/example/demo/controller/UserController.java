package com.example.demo.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.payment.PaymentMethodDTO;
import com.example.demo.dto.user.AddressDTO;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("user")
public class UserController {
	
	@Autowired
	private UserService userService;

	@GetMapping("/addresses")
	@ResponseBody
	public ResponseEntity<List<AddressDTO>> getAddresses(HttpServletRequest request) {
		return ResponseEntity.ok(userService.getAddresses(request.getUserPrincipal()));
	}
	
	@GetMapping("/payment-methods")
	@ResponseBody
	public ResponseEntity<List<PaymentMethodDTO>> getPaymentMethods(HttpServletRequest request) {
		return ResponseEntity.ok(userService.getPaymentMethods(request.getUserPrincipal()));
	}
}
