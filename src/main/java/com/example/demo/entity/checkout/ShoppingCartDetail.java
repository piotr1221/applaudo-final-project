package com.example.demo.entity.checkout;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Getter;

@Entity
@Getter
public class ShoppingCartDetail {
	@Id
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Product product;
	
	private Integer quantity;
	private Double subtotal;
}
