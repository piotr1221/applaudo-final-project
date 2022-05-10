package com.example.demo.utility;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MyModelMapper {
	
	private ModelMapper modelMapper;
	
	public <T, K> K map(T source, Class<K> targetClass) {
		return this.modelMapper.map(source, targetClass);
	}
	
	public <T, K> List<K> convertAllEntitiesToDTO(List<T> sourceList, Class<K> targetClass){
		return sourceList.stream()
						.map(e -> this.map(e, targetClass))
						.collect(Collectors.toList());
	}
	
	/*
	 * T is source superclass
	 * K is target superclass
	 * */
	public <T, K> List<K> convertAllEntitiesToDTO(List<T> sourceList, Map<Class<? extends T>, Class<? extends K>> map) {
		return sourceList.stream()
						.map(e -> this.map(e, map.get(e.getClass())))
						.collect(Collectors.toList());
	}
}
