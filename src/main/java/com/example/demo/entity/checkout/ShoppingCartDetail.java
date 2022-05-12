package com.example.demo.entity.checkout;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Positive;

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
	
	@Positive
	private Integer quantity;
	private Double subtotal;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private ShoppingCart shoppingCart;
	
	public ShoppingCartDetail(Product product, Integer quantity) {
		this.product = product;
		this.quantity = quantity;
		this.subtotal = product.getPrice() * this.quantity;
	}
	
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
		this.subtotal = this.getQuantity() * this.getProduct().getPrice();
	}
	
	@Override
	public int hashCode() {
		return 11;
	}
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ShoppingCartDetail)) return false;
		ShoppingCartDetail o = (ShoppingCartDetail) obj;
		return this.getProduct().equals(o.getProduct());
	}
	@Override
	public String toString() {
		return "ID: " + this.getId() + " - ProductID: " + this.getProduct().getId() + " - Quantity: " + this.getQuantity() + " - Subtotal: " + this.getSubtotal();
	}
}
