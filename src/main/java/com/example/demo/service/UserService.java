package com.example.demo.service;

import java.security.Principal;
import java.util.List;

import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.payment.PaymentMethodDTO;
import com.example.demo.dto.user.AddressDTO;
import com.example.demo.entity.payment.PaymentMethod;
import com.example.demo.entity.user.Address;
import com.example.demo.entity.user.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.utility.EntityToDTOMap;
import com.example.demo.utility.ScopeVerifier;
import com.example.demo.utility.MyModelMapper;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ScopeVerifier scopeVerifier;
	
	@Autowired
	private MyModelMapper myModelMapper;
	
	public User getUser(String username) {
		return userRepository.findByUsername(username);
	}
	
	public Address getAddress(Long id, Principal principal) {
		scopeVerifier.hasScope((KeycloakAuthenticationToken) principal, "read-user-addresses");
		User user = this.getUser(principal.getName());
		return user.getAddresses().stream()
								.filter(e -> e.getId().equals(id))
								.findFirst()
								.orElseThrow();
	}
	
	public PaymentMethod getPaymentMethod(Long id, Principal principal) {
		scopeVerifier.hasScope((KeycloakAuthenticationToken) principal, "read-payment-methods");
		User user = this.getUser(principal.getName());
		return user.getPaymentMethods().stream()
									.filter(e -> e.getId().equals(id))
									.findFirst()
									.orElseThrow();
	}

	public List<AddressDTO> getAddresses(Principal principal){
		User user = this.getUser(principal.getName());
		return myModelMapper.convertAllEntitiesToDTO(user.getAddresses(), AddressDTO.class);
	}
	
	public List<PaymentMethodDTO> getPaymentMethods(Principal principal){
		scopeVerifier.hasScope((KeycloakAuthenticationToken)principal, "read-payment-methods");
		User user = this.getUser(principal.getName());
		return myModelMapper.convertAllEntitiesToDTO(user.getPaymentMethods(), EntityToDTOMap.paymentMethods);
	}
	
	public User save(User user) {
		return userRepository.save(user);
	}
}
