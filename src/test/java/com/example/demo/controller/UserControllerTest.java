package com.example.demo.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakSecurityComponents;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.dto.payment.PaymentMethodDTO;
import com.example.demo.dto.user.AddressDTO;
import com.example.demo.service.UserService;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ComponentScan(basePackageClasses = { KeycloakSecurityComponents.class, KeycloakSpringBootConfigResolver.class })
@DisplayName("Test suite for UserController")
class UserControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private UserService userService;
	
	AddressDTO addressDTO;
	PaymentMethodDTO paymentMethodDTO;
	
	@BeforeEach
	void init() {
		addressDTO = new AddressDTO();
		addressDTO.setId(1L);
		addressDTO.setAddress("Test Address");
		
		paymentMethodDTO = new PaymentMethodDTO();
		paymentMethodDTO.setId(1L);
	}

	@Nested
	class Addresses {
		@Test
		@DisplayName("Get addresses")
		void getCatalog() throws Exception {
			List<AddressDTO> addresses = new ArrayList<>();
			addresses.add(addressDTO);
			when(userService.getAddresses(isNull())).thenReturn(addresses);

			mvc.perform(get("/user/addresses/").contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].id").value(addressDTO.getId()))
				.andExpect(jsonPath("$[0].address").value(addressDTO.getAddress()))
				.andExpect(jsonPath("$", hasSize(addresses.size())))
				.andExpect(status().isOk());
		}
	}
	
	@Nested
	class PaymentMethods {
		@Test
		@DisplayName("Get payment methods")
		void getPaymentMethods() throws Exception {
			List<PaymentMethodDTO> paymentMethods = new ArrayList<>();
			paymentMethods.add(paymentMethodDTO);
			when(userService.getPaymentMethods(isNull())).thenReturn(paymentMethods);
			
			mvc.perform(get("/user/payment-methods/").contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$[0].id").value(paymentMethodDTO.getId()))
			.andExpect(jsonPath("$", hasSize(paymentMethods.size())))
			.andExpect(status().isOk());
		}
	}

}
