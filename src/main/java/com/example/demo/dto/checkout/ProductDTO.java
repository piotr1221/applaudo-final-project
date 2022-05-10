package com.example.demo.dto;

import lombok.Data;
import lombok.Getter;

@Data
public class ProductDTO {
	private Long id;
	private String name;
	private Double price;
	private Integer stock;
}

