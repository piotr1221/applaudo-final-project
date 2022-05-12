package com.example.demo.entity.checkout;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.example.demo.entity.payment.PaymentMethod;
import com.example.demo.entity.user.Address;

import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ShoppingCart {
	@Id
	private Long id;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "shopping_cart_id", referencedColumnName = "id")
	private List<ShoppingCartDetail> shoppingCartDetails;
	
	private Double total;
	@ManyToOne
	private Address address;
	@ManyToOne
	private PaymentMethod paymentMethod;
	
	public ShoppingCart(Long id, List<ShoppingCartDetail> shoppingCartDetails) {
		this.id = id;
		this.shoppingCartDetails = shoppingCartDetails;
		this.updateTotal();
	}
	
	public void addShoppingCartDetails(List<ShoppingCartDetail> shoppingCartDetails) {
		this.getShoppingCartDetails().addAll(shoppingCartDetails);
		this.updateTotal();
	}
	
	public void removeShoppingCartDetail(ShoppingCartDetail shoppingCartDetail) {
		this.getShoppingCartDetails().remove(shoppingCartDetail);
		this.updateTotal();
	}
	
	public void updateTotal() {
		this.total = this.shoppingCartDetails.stream()
											.map(ShoppingCartDetail::getSubtotal)
											.reduce((double) 0, Double::sum);
	}
	
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
