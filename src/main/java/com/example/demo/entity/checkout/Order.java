package com.example.demo.entity.checkout;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;

@Entity
@Table(name="orders")
@Getter
public class Order {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@OneToMany
	private List<OrderDetail> orderDetails;

	@Override
	public int hashCode() {
		return 7;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Order)) return false;
		Order o = (Order) obj;
		if (o.getId() == null || this.getId() == null) return false;
		return this.getId() == o.getId();
	}

	@Override
	public String toString() {
		return "Order ID: " + this.getId();
	}
}
