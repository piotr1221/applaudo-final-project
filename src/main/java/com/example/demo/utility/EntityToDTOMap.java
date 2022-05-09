package com.example.demo.utility;

import java.util.Map;

import com.example.demo.dto.BankAccountDTO;
import com.example.demo.dto.CreditCardDTO;
import com.example.demo.dto.PaymentMethodDTO;
import com.example.demo.entity.BankAccount;
import com.example.demo.entity.CreditCard;
import com.example.demo.entity.PaymentMethod;

public class EntityToDTOMap {
	public static Map<Class<? extends PaymentMethod>, Class<? extends PaymentMethodDTO>> paymentMethods = Map.ofEntries(
		    Map.entry(CreditCard.class, CreditCardDTO.class),
		    Map.entry(BankAccount.class, BankAccountDTO.class)
		);
	
	private EntityToDTOMap() {}
}
