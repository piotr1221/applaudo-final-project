package com.example.demo.utility;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.dto.ProductDTO;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MyModelMapper {
	
	ModelMapper modelMapper;
	
	public <T, K> K convertEntityToDTO(T entity, Class<K> dtoClass) {
		return modelMapper.map(entity, dtoClass);
	}
	
	public <T, K> List<K> convertAllEntitiesToDTO(List<T> list, Class<K> dtoClass){
		return list.stream()
					.map(e -> modelMapper.map(e, dtoClass))
					.collect(Collectors.toList());
	}
}
