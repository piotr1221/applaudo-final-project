package com.example.demo.entity.checkout;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCart {
	@Id
	private Long id;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "shopping_cart_id", referencedColumnName = "id")
	private List<ShoppingCartDetail> shoppingCartDetails;
	
	@Override
	public int hashCode() {
		return 7;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ShoppingCart)) return false;
		ShoppingCart o = (ShoppingCart) obj;
		if (o.getId() == null || this.getId() == null) return false;
		return this.getId() == o.getId();
	}

	@Override
	public String toString() {
		return "ShoppingCartID: " + this.getId();
	}
}
