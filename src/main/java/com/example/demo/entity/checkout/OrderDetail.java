package com.example.demo.entity.checkout;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Getter;

@Entity
@Getter
public class OrderDetail {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Product product;
	
	private Integer quantity;
	private Double subtotal;
	
	@Override
	public int hashCode() {
		return 11;
	}
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof OrderDetail)) return false;
		OrderDetail o = (OrderDetail) obj;
		if (o.getId() == null || this.getId() == null) return false;
		return this.getId() == o.getId();
	}
	@Override
	public String toString() {
		return "ID: " + this.getId() + " - ProductID: " + this.getProduct().getId() + " - Quantity: " + this.getQuantity() + " - Subtotal: " + this.getSubtotal();
	}
}
