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
import com.example.demo.entity.payment.BankAccount;
import com.example.demo.entity.payment.CreditCard;
import com.example.demo.exception.responsestatus.ForbiddenStatusException;
import com.example.demo.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;

@RestController
@RequestMapping("user")
public class UserController {
	
	@Autowired
	private UserService userService;

	@Operation(summary = "Get all user's shipping addresses")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "User's shipping addresses retrieved",
					content = {@Content(mediaType = "application/json",
										array = @ArraySchema(schema = @Schema(implementation = AddressDTO.class)))
							}),
			@ApiResponse(responseCode = "403",
					description = "Required scope read-user-addreses not provided",
					content = {@Content(mediaType = "application/json",
										schema = @Schema(implementation = ForbiddenStatusException.class))
					})
	})
	@GetMapping("/addresses")
	@ResponseBody
	public ResponseEntity<List<AddressDTO>> getAddresses(HttpServletRequest request) {
		return ResponseEntity.ok(userService.getAddresses(request.getUserPrincipal()));
	}
	
	@Operation(summary = "Get all user's payment methods")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "User's payment methods retrieved",
					content = {@Content(mediaType = "application/json",
										array = @ArraySchema(schema = @Schema(anyOf = {CreditCard.class, BankAccount.class})))
							}),
			@ApiResponse(responseCode = "403",
					description = "Required scope read-payment-methods not provided",
					content = {@Content(mediaType = "application/json",
										schema = @Schema(implementation = ForbiddenStatusException.class))
					})
	})
	@GetMapping("/payment-methods")
	@ResponseBody
	public ResponseEntity<List<PaymentMethodDTO>> getPaymentMethods(HttpServletRequest request) {
		return ResponseEntity.ok(userService.getPaymentMethods(request.getUserPrincipal()));
	}
}
