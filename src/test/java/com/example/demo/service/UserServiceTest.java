package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.security.Principal;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat; 
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.example.demo.dto.payment.BankAccountDTO;
import com.example.demo.dto.payment.PaymentMethodDTO;
import com.example.demo.dto.user.AddressDTO;
import com.example.demo.entity.payment.BankAccount;
import com.example.demo.entity.payment.PaymentMethod;
import com.example.demo.entity.user.Address;
import com.example.demo.entity.user.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.utility.ScopeVerifier;

@SpringBootTest
@DisplayName("Test suite for UserService")
class UserServiceTest {
	
	@Autowired
	UserService userService;

	@MockBean
	UserRepository userRepository;
	
	@MockBean
	private ScopeVerifier scopeVerifier;
	
	@MockBean
	private KeycloakAuthenticationToken principal;
	
	User user;
	Address address;
	BankAccount paymentMethod;
	
	@BeforeEach
	void init() {
		user = new User();
		user.setId(1L);
		user.setUsername("edwin@gmail.com");
		user.setPassword("123");
		user.setFullName("Edwin Aguilar");
		address = new Address();
		address.setId(1L);
		address.setAddress("Address");
		user.setAddresses(List.of(address));
		BankAccount bankAccount = new BankAccount();
		bankAccount.setId(1L);
		bankAccount.setFunds(1000d);
		bankAccount.setAccountNumber("123 456789");
		paymentMethod = bankAccount;
		user.setPaymentMethods(List.of(paymentMethod));
	}
	
	@Test
	@DisplayName("Get user by username")
	void getUser() {
		when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
		
		User getUser = userService.getUser(user.getUsername());
		assertThat(user, equalTo(getUser));
	}
	
	@Test
	@DisplayName("Get user address with required scope")
	void getAddress() {
		doNothing().when(scopeVerifier).hasScope(isNull(), anyString());
		doReturn(user.getUsername()).when(principal).getName();
		when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
		
		Address otherAddress = userService.getAddress(address.getId(), principal);
		assertThat(address, equalTo(otherAddress));
	}

	
	@Test
	@DisplayName("Get all user addresses with required scope")
	void getAddresses() {
		doNothing().when(scopeVerifier).hasScope(isNull(), anyString());
		doReturn(user.getUsername()).when(principal).getName();
		when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
		
		List<AddressDTO> otherAddresses = userService.getAddresses(principal);
		AddressDTO addressDTO = new AddressDTO();
		addressDTO.setAddress(address.getAddress());
		addressDTO.setId(address.getId());
		List<AddressDTO> addresses = List.of(addressDTO);
		
		assertThat(addresses, equalTo(otherAddresses));
	}
	
	@Test
	@DisplayName("Get user payment method with required scope")
	void getPaymentMethod() {
		doNothing().when(scopeVerifier).hasScope(isNull(), anyString());
		doReturn(user.getUsername()).when(principal).getName();
		when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
		
		PaymentMethod otherPaymentMethod = userService.getPaymentMethod(paymentMethod.getId(), principal);
		assertThat(paymentMethod, equalTo(otherPaymentMethod));
	}
	
	@Test
	@DisplayName("Get all user payment methods with required scope")
	void getPaymentMethods() {
		doNothing().when(scopeVerifier).hasScope(isNull(), anyString());
		doReturn(user.getUsername()).when(principal).getName();
		when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
		
		List<PaymentMethodDTO> otherPaymentMethods = userService.getPaymentMethods(principal);
		BankAccountDTO paymentMethodDTO = new BankAccountDTO();
		paymentMethodDTO.setId(paymentMethod.getId());
		paymentMethodDTO.setAccountNumber("123 456789");
		List<PaymentMethodDTO> paymentMethods = List.of(paymentMethodDTO);
		
		assertThat(paymentMethods, equalTo(otherPaymentMethods));
	}
}
