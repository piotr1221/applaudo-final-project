package com.example.demo.entity.checkout;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ShoppingCart {
	@Id
	private Long id;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "shopping_cart_id", referencedColumnName = "id")
	private List<ShoppingCartDetail> shoppingCartDetails;
}
