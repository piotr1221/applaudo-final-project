package com.example.demo.utility;

import java.util.Map;

import com.example.demo.dto.payment.BankAccountDTO;
import com.example.demo.dto.payment.CreditCardDTO;
import com.example.demo.dto.payment.PaymentMethodDTO;
import com.example.demo.entity.payment.BankAccount;
import com.example.demo.entity.payment.CreditCard;
import com.example.demo.entity.payment.PaymentMethod;

public class EntityToDTOMap {
	public static Map<Class<? extends PaymentMethod>, Class<? extends PaymentMethodDTO>> paymentMethods = Map.ofEntries(
		    Map.entry(CreditCard.class, CreditCardDTO.class),
		    Map.entry(BankAccount.class, BankAccountDTO.class)
		);
	
	private EntityToDTOMap() {}
}
