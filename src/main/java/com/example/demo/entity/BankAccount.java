package com.example.demo.entity;

import javax.persistence.Entity;

import lombok.Getter;

@Entity
@Getter
public class BankAccount extends PaymentMethod{
	private String accountNumber;
}
