package com.example.demo.dto.payment;

import lombok.Data;

@Data
public class BankAccountDTO extends PaymentMethodDTO {
	private String accountNumber;
}
