package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.ProductDTO;
import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.utility.MyModelMapper;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	MyModelMapper myModelMapper;
	
	
	public List<ProductDTO> getProducts(){
		return myModelMapper.convertAllEntitiesToDTO(productRepository.findAll(), ProductDTO.class);
	}
}
