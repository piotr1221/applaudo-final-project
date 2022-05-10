package com.example.demo.entity;

import javax.persistence.Entity;

import lombok.Getter;

@Entity
@Getter
public class CreditCard extends PaymentMethod{
	private String creditCardNumber;
	private String cvc;
	private String expDate;
}
