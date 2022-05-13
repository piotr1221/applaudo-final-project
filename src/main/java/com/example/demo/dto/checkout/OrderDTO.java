package com.example.demo.dto.checkout;

import java.util.List;

import com.example.demo.dto.payment.PaymentMethodDTO;
import com.example.demo.dto.user.AddressDTO;
import com.example.demo.dto.user.UserBasicInfoDTO;

import lombok.Data;

@Data
public class OrderDTO {
	private Long id;
	private List<OrderDetailDTO> shoppingCartDetails;
	private Double total;
	private AddressDTO address;
	private PaymentMethodDTO paymentMethod;
	private UserBasicInfoDTO user;
}
