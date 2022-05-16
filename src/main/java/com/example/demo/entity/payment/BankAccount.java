package com.example.demo.entity.payment;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class BankAccount extends PaymentMethod {
	private String accountNumber;
}
