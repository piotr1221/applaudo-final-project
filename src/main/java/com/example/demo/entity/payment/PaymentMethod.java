package com.example.demo.entity.payment;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import lombok.Getter;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
public abstract class PaymentMethod {
	@Id
	private Long id;
	private Double funds;
	
	public void deduct(double amount) {
		this.funds -= amount;
	}
}
