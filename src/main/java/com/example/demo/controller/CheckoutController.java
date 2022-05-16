package com.example.demo.controller;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.servlet.http.HttpServletRequest;

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

import com.example.demo.dto.checkout.OrderDTO;
import com.example.demo.dto.checkout.ShoppingCartDTO;
import com.example.demo.exception.responsestatus.BadRequestStatusException;
import com.example.demo.exception.responsestatus.ConflictStatusException;
import com.example.demo.exception.responsestatus.NotFoundStatusException;
import com.example.demo.service.CheckoutService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("checkout")
public class CheckoutController {
	
	@Autowired
	private CheckoutService checkoutService;
	
	@Operation(summary = "Get user checkout information")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Checkout in process",
					content = {@Content(mediaType = "application/json",
								schema = @Schema(implementation = ShoppingCartDTO.class))
							}),
			@ApiResponse(responseCode = "404",
			description = "The user hasn't started a checkout process",
			content = {@Content(mediaType = "application/json",
								schema = @Schema(implementation = NotFoundStatusException.class))
			})
	})
	@GetMapping("/")
	@ResponseBody
	public ResponseEntity<ShoppingCartDTO> getCheckout(HttpServletRequest request){
		return ResponseEntity.ok(checkoutService.getCheckout(request.getUserPrincipal()));
	}
	
	@Operation(summary = "Start checkout process")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201",
					description = "Checkout in process",
					content = {@Content(mediaType = "application/json",
								schema = @Schema(implementation = ShoppingCartDTO.class))
							}),
			@ApiResponse(responseCode = "409",
			description = "The user hasn't started a checkout process",
			content = {@Content(mediaType = "application/json",
								schema = @Schema(implementation = ConflictStatusException.class))
			})
	})
	@PostMapping("/")
	@ResponseBody
	public ResponseEntity<ShoppingCartDTO> startCheckout(@RequestBody List<Map<String, Object>> orderDetails, HttpServletRequest request){
		return new ResponseEntity<>(checkoutService.startCheckout(orderDetails, request.getUserPrincipal()), HttpStatus.CREATED);
	}
	
	@Operation(summary = "Add product to shopping cart")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Product added to shopping cart",
					content = {@Content(mediaType = "application/json",
								schema = @Schema(implementation = ShoppingCartDTO.class))}),
			@ApiResponse(responseCode = "404",
			description = "The user hasn't started a checkout process",
			content = {@Content(mediaType = "application/json",
								schema = @Schema(implementation = NotFoundStatusException.class))}),
			@ApiResponse(responseCode = "409",
			description = "The product is already in the shopping cart",
			content = {@Content(mediaType = "application/json",
								schema = @Schema(implementation = ConflictStatusException.class))})
	})
	@PutMapping("/products")
	@ResponseBody
	public ResponseEntity<ShoppingCartDTO> addProductToShoppingCart(@RequestBody List<Map<String, Object>> orderDetails, HttpServletRequest request) {
		return ResponseEntity.ok(checkoutService.addProductToShoppingCart(orderDetails, request.getUserPrincipal()));
	}
	
	@Operation(summary = "Update product quantity in shopping cart")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Product quantity updated",
					content = {@Content(mediaType = "application/json",
										schema = @Schema(implementation = ShoppingCartDTO.class))}),
			@ApiResponse(responseCode = "404",
			description = "The user hasn't started a checkout process",
			content = {@Content(mediaType = "application/json",
								schema = @Schema(implementation = NotFoundStatusException.class))}),
			@ApiResponse(responseCode = "404",
			description = "The product doesn't exist",
			content = {@Content(mediaType = "application/json",
								schema = @Schema(implementation = NoSuchElementException.class))})
	})
	@PutMapping("/products/{productId}")
	public ResponseEntity<ShoppingCartDTO> updateProductQuantity(@PathVariable Integer productId, @RequestBody Map<String, Object> productQuantity, HttpServletRequest request) {
		return ResponseEntity.ok(checkoutService.updateProductQuantity(productId, productQuantity, request.getUserPrincipal()));
	}
	
	@Operation(summary = "Remove product from shopping cart")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "Product removed from shopping cart",
					content = {@Content(mediaType = "application/json",
											schema = @Schema(implementation = ShoppingCartDTO.class))}),
			@ApiResponse(responseCode = "404",
			description = "The user hasn't started a checkout process",
			content = {@Content(mediaType = "application/json",
								schema = @Schema(implementation = NotFoundStatusException.class))}),
			@ApiResponse(responseCode = "404",
			description = "The product doesn't exist",
			content = {@Content(mediaType = "application/json",
								schema = @Schema(implementation = NoSuchElementException.class))})
	})
	@DeleteMapping("/products/{productId}")
	public ResponseEntity<ShoppingCartDTO> removeProductFromShoppingCart(@PathVariable Integer productId, HttpServletRequest request){
		return ResponseEntity.ok(checkoutService.removeProductFromShoppingCart(productId, request.getUserPrincipal()));
	}
	
	@Operation(summary = "Add user's shipping address to shopping cart")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "User's shipping address added to shopping cart",
					content = {@Content(mediaType = "application/json",
											schema = @Schema(implementation = ShoppingCartDTO.class))}),
			@ApiResponse(responseCode = "404",
			description = "The user hasn't started a checkout process",
			content = {@Content(mediaType = "application/json",
								schema = @Schema(implementation = NotFoundStatusException.class))}),
			@ApiResponse(responseCode = "404",
			description = "The address doesn't exist",
			content = {@Content(mediaType = "application/json",
								schema = @Schema(implementation = NoSuchElementException.class))})
	})
	@PutMapping("/address")
	@ResponseBody
	public ResponseEntity<ShoppingCartDTO> addShippingAddress(@RequestBody Map<String, Object> address, HttpServletRequest request) {
		return ResponseEntity.ok(checkoutService.addShippingAddress(address, request.getUserPrincipal()));
	}
	
	@Operation(summary = "Add user's payment method to shopping cart")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "User's payment method added to shopping cart",
					content = {@Content(mediaType = "application/json",
											schema = @Schema(implementation = ShoppingCartDTO.class))}),
			@ApiResponse(responseCode = "404",
			description = "The user hasn't started a checkout process",
			content = {@Content(mediaType = "application/json",
								schema = @Schema(implementation = NotFoundStatusException.class))}),
			@ApiResponse(responseCode = "404",
			description = "The payment method doesn't exist",
			content = {@Content(mediaType = "application/json",
								schema = @Schema(implementation = NoSuchElementException.class))})
	})
	@PutMapping("/payment-method")
	@ResponseBody
	public ResponseEntity<ShoppingCartDTO> addPaymentMethod(@RequestBody Map<String, Object> paymentMethod, HttpServletRequest request) {
		return ResponseEntity.ok(checkoutService.addPaymentMethod(paymentMethod, request.getUserPrincipal()));
	}
	
	@Operation(summary = "Create and register order from shopping cart")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201",
					description = "Order succesfully registered",
					content = {@Content(mediaType = "application/json",
											schema = @Schema(implementation = OrderDTO.class))}),
			@ApiResponse(responseCode = "404",
			description = "The user hasn't started a checkout process",
			content = {@Content(mediaType = "application/json",
								schema = @Schema(implementation = NotFoundStatusException.class))}),
			@ApiResponse(responseCode = "400",
			description = "The shopping cart doesn't have an address or payment method associated",
			content = {@Content(mediaType = "application/json",
								schema = @Schema(implementation = BadRequestStatusException.class))})
	})
	@PostMapping("/orders")
	@ResponseBody
	public ResponseEntity<OrderDTO> createOrder(HttpServletRequest request) {
		return new ResponseEntity<>(checkoutService.createOrder(request.getUserPrincipal()), HttpStatus.CREATED);
	}
	
	@Operation(summary = "Get all user's orders")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
					description = "All user's orders retrieved",
					content = {@Content(mediaType = "application/json",
										array = @ArraySchema(schema = @Schema(implementation = OrderDTO.class)))}),
			@ApiResponse(responseCode = "404",
			description = "The user hasn't started a checkout process",
			content = {@Content(mediaType = "application/json",
								schema = @Schema(implementation = NotFoundStatusException.class))}),
			@ApiResponse(responseCode = "400",
			description = "The shopping cart doesn't have an address or payment method associated",
			content = {@Content(mediaType = "application/json",
								schema = @Schema(implementation = BadRequestStatusException.class))})
	})
	@GetMapping("/orders")
	@ResponseBody
	public ResponseEntity<List<OrderDTO>> getOrders(HttpServletRequest request) {
		return ResponseEntity.ok(checkoutService.getOrders(request.getUserPrincipal()));
	}
	
}
