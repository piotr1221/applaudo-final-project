package com.example.demo.entity.checkout;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ShoppingCartDetail {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Product product;
	
	private Integer quantity;
	private Double subtotal;
	
	public ShoppingCartDetail(Product product, Integer quantity) {
		this.product = product;
		this.quantity = quantity;
		this.subtotal = product.getPrice() * this.quantity;
	}
}
