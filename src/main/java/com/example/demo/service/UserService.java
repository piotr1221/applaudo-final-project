package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.AddressDTO;
import com.example.demo.dto.PaymentMethodDTO;
import com.example.demo.entity.user.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.utility.EntityToDTOMap;
import com.example.demo.utility.MyModelMapper;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private MyModelMapper myModelMapper;
	
	public User getUser(String username) {
		return userRepository.findByUsername(username);
	}

	public List<AddressDTO> getAddresses(String username){
		return myModelMapper.convertAllEntitiesToDTO(this.getUser(username).getAddresses(), AddressDTO.class);
	}
	
	public List<PaymentMethodDTO> getPaymentMethods(String username){
		return myModelMapper.convertAllEntitiesToDTO(this.getUser(username).getPaymentMethods(), EntityToDTOMap.paymentMethods);
	}
}
