package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.AddressDTO;
import com.example.demo.dto.PaymentMethodDTO;
import com.example.demo.repository.UserRepository;
import com.example.demo.utility.EntityToDTOMap;
import com.example.demo.utility.MyModelMapper;

@Service
public class UserService {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	MyModelMapper myModelMapper;

	public List<AddressDTO> getAddresses(String username){
		return myModelMapper.convertAllEntitiesToDTO(userRepository.findByUsername(username).getAddresses(), AddressDTO.class);
	}
	
	public List<PaymentMethodDTO> getPaymentMethods(String username){
		return myModelMapper.convertAllEntitiesToDTO(userRepository.findByUsername(username).getPaymentMethods(), EntityToDTOMap.paymentMethods);
	}
}
