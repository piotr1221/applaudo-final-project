package com.example.demo.entity.checkout;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Product {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String name;
	private Double price;
	private Integer stock;
	
	@Override
	public int hashCode() {
		return 5;
	}
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Product)) return false;
		Product o = (Product) obj;
		if (o.getId() == null || this.getId() == null) return false;
		return this.getId() == o.getId();
	}
	@Override
	public String toString() {
		return "ID: " + this.getId() + " - Name: " + this.getName() + " - Price: " + this.getPrice() + " - Stock: " + this.getStock();
	}
	
	
}
