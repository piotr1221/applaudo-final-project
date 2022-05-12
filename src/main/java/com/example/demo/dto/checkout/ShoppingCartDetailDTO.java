package com.example.demo.dto.checkout;

import lombok.Data;

@Data
public class ShoppingCartDetailDTO {
	private Long id;
	private ProductDTO product;
	private Integer quantity;
	private Double subtotal;
}
