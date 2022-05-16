package com.example.demo.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakSecurityComponents;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.dto.checkout.OrderDTO;
import com.example.demo.dto.checkout.ProductDTO;
import com.example.demo.dto.checkout.ShoppingCartDTO;
import com.example.demo.dto.checkout.ShoppingCartDetailDTO;
import com.example.demo.entity.user.User;
import com.example.demo.service.CheckoutService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(CheckoutController.class)
@AutoConfigureMockMvc(addFilters = false)
@ComponentScan(basePackageClasses = { KeycloakSecurityComponents.class, KeycloakSpringBootConfigResolver.class })
@DisplayName("Test suite for CheckoutController")
class CheckoutControllerTest {

	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private CheckoutService checkoutService;
	
	@Mock
	private Principal principal;
	
	@MockBean
	private User user;
	
	ShoppingCartDTO shoppingCartDTO;
	ShoppingCartDetailDTO shoppingCartDetailDTO;
	ProductDTO productDTO;
	List<ShoppingCartDetailDTO> shoppingCarDetails;
	
	@BeforeEach
	void init() {
		productDTO = new ProductDTO();
		productDTO.setId(1L);
		productDTO.setName("Product");
		productDTO.setPrice(1d);
		productDTO.setStock(100);
		shoppingCartDetailDTO = new ShoppingCartDetailDTO();
		shoppingCartDetailDTO.setId(1L);
		shoppingCartDetailDTO.setProduct(productDTO);
		shoppingCartDetailDTO.setQuantity(2);
		shoppingCartDetailDTO.setSubtotal(productDTO.getPrice() * shoppingCartDetailDTO.getQuantity());
		shoppingCartDTO = new ShoppingCartDTO();
		shoppingCartDTO.setId(1L);
		shoppingCarDetails = new ArrayList<>();
		shoppingCarDetails.add(shoppingCartDetailDTO);
		shoppingCartDTO.setShoppingCartDetails(shoppingCarDetails);
	}
	
	@Test
	@DisplayName("Get checkout")
	void getCheckout() throws Exception {
		when(checkoutService.getCheckout(isNull())).thenReturn(shoppingCartDTO);
		
		mvc.perform(get("/checkout/").contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.id").value(shoppingCartDTO.getId()))
			.andExpect(status().isOk());
	}
	
	@Test
	@DisplayName("Start checkout")
	void startCheckout() throws Exception {
		when(checkoutService.startCheckout(anyList(), isNull())).thenReturn(shoppingCartDTO);

		mvc.perform(post("/checkout/").contentType(MediaType.APPLICATION_JSON)
										.content(objectMapper.writeValueAsString(shoppingCarDetails)))
			.andExpect(status().isCreated());
	}
	
	@Test
	@DisplayName("Add product")
	void addProduct() throws Exception {
		when(checkoutService.addProductToShoppingCart(anyList(), isNull())).thenReturn(shoppingCartDTO);
		
		mvc.perform(put("/checkout/products/").contentType(MediaType.APPLICATION_JSON)
										.content(objectMapper.writeValueAsString(shoppingCarDetails)))
			.andExpect(status().isOk());
	}
	
	@Test
	@DisplayName("Add product")
	void updateQuantity() throws Exception {
		when(checkoutService.addProductToShoppingCart(anyList(), isNull())).thenReturn(shoppingCartDTO);
		Map<String, Object> productQuantity = new HashMap<>();
		productQuantity.put("quantity", shoppingCartDetailDTO.getQuantity() + 1);
		
		mvc.perform(put("/checkout/products/{productId}", productDTO.getId()).contentType(MediaType.APPLICATION_JSON)
										.content(objectMapper.writeValueAsString(productQuantity)))
			.andExpect(status().isOk());
	}
	
	@Test
	@DisplayName("Remove product")
	void removeProduct() throws Exception {
		when(checkoutService.removeProductFromShoppingCart(anyInt(), isNull())).thenReturn(shoppingCartDTO);
		
		mvc.perform(delete("/checkout/products/{productId}", productDTO.getId()).contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}
	
	@Test
	@DisplayName("Add address")
	void addAddress() throws Exception {
		when(checkoutService.addShippingAddress(anyMap(), isNull())).thenReturn(shoppingCartDTO);
		Map<String, Object> address = new HashMap<>();
		address.put("addressId", 1);
		mvc.perform(put("/checkout/address/").contentType(MediaType.APPLICATION_JSON)
												.content(objectMapper.writeValueAsString(address)))
			.andExpect(status().isOk());
	}
	
	@Test
	@DisplayName("Add payment method")
	void addPaymentMethod() throws Exception {
		when(checkoutService.addPaymentMethod(anyMap(), isNull())).thenReturn(shoppingCartDTO);
		Map<String, Object> paymentMethod = new HashMap<>();
		paymentMethod.put("paymentMethodId", 1);
		mvc.perform(put("/checkout/payment-method/").contentType(MediaType.APPLICATION_JSON)
												.content(objectMapper.writeValueAsString(paymentMethod)))
			.andExpect(status().isOk());
	}
	
	@Test
	@DisplayName("Create order")
	void createOrder() throws Exception {
		when(checkoutService.createOrder(isNull())).thenReturn(new OrderDTO());

		mvc.perform(post("/checkout/orders/").contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated());
	}
	
	@Test
	@DisplayName("Get orders")
	void getOrders() throws Exception {
		List<OrderDTO> orders = new ArrayList<>();
		orders.add(new OrderDTO());
		when(checkoutService.getOrders(isNull())).thenReturn(orders);
		
		mvc.perform(get("/checkout/orders/").contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}

}
