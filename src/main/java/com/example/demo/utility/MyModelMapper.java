package com.example.demo.utility;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MyModelMapper {
	
	private ModelMapper modelMapper;
	
	public <T, K> K convertEntityToDTO(T entity, Class<K> dtoClass) {
		return this.modelMapper.map(entity, dtoClass);
	}
	
	public <T, K> List<K> convertAllEntitiesToDTO(List<T> list, Class<K> dtoClass){
		return list.stream()
					.map(e -> this.convertEntityToDTO(e, dtoClass))
					.collect(Collectors.toList());
	}
	
	/*
	 * T is Entity superclass
	 * K is DTO superclass
	 * */
	public <T, K> List<K> convertAllEntitiesToDTO(List<T> list, Map<Class<? extends T>, Class<? extends K>> map) {
		return list.stream()
					.map(e -> this.convertEntityToDTO(e, map.get(e.getClass())))
					.collect(Collectors.toList());
	}
}
