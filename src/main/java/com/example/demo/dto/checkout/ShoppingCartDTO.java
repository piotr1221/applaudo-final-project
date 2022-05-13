package com.example.demo.dto.checkout;

import java.util.List;

import com.example.demo.dto.payment.PaymentMethodDTO;
import com.example.demo.dto.user.AddressDTO;

import lombok.Data;

@Data
public class ShoppingCartDTO {
	private Long id;
	private List<ShoppingCartDetailDTO> shoppingCartDetails;
	private Double total;
	private AddressDTO address;
	private PaymentMethodDTO paymentMethod;
}
