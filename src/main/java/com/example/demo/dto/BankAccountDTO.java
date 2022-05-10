package com.example.demo.dto;

import com.example.demo.entity.payment.PaymentMethod;

import lombok.Data;

@Data
public class BankAccountDTO extends PaymentMethodDTO {
	private String accountNumber;
}
