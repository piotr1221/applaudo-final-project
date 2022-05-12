package com.example.demo.entity.payment;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

import com.example.demo.entity.user.User;

import lombok.Getter;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
public abstract class PaymentMethod {
	@Id
	private Long id;
	private Double funds;
	
//	@ManyToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
//	private List<User> users;
	
	public void deduct(double amount) {
		this.funds -= amount;
	}
}
