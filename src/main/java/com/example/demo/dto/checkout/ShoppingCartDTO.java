package com.example.demo.dto.checkout;

import java.util.List;

import lombok.Data;

@Data
public class ShoppingCartDTO {
	private Long id;
	private List<ShoppingCartDetailDTO> shoppingCartDetails;
}
