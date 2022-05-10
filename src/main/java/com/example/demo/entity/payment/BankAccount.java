package com.example.demo.entity.payment;

import javax.persistence.Entity;

import lombok.Getter;

@Entity
@Getter
public class BankAccount extends PaymentMethod{
	private String accountNumber;
}
