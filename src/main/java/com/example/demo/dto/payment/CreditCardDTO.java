package com.example.demo.dto;

import lombok.Data;

@Data
public class CreditCardDTO extends PaymentMethodDTO {
	private String creditCardNumber;
	private String cvc;
	private String expDate;
}
