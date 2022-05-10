package com.example.demo.dto;

import com.example.demo.entity.PaymentMethod;

import lombok.Data;

@Data
public class BankAccountDTO extends PaymentMethodDTO {
	private String accountNumber;
}
