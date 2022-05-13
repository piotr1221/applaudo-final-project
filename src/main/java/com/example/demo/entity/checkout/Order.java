package com.example.demo.entity.checkout;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.example.demo.entity.payment.PaymentMethod;
import com.example.demo.entity.user.Address;
import com.example.demo.entity.user.User;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="orders")
@Getter
@NoArgsConstructor
public class Order {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "order_id", referencedColumnName = "id")
	private List<OrderDetail> orderDetails;
	
	private Double total;
	@ManyToOne
	private Address address;
	@ManyToOne
	private PaymentMethod paymentMethod;
	
	@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private User user;
	
	public Order(ShoppingCart shoppingCart, List<OrderDetail> orderDetails) {
		this.total = shoppingCart.getTotal();
		this.address = shoppingCart.getAddress();
		this.paymentMethod = shoppingCart.getPaymentMethod();
		this.user = shoppingCart.getUser();
		this.orderDetails = orderDetails;
	}

	@Override
	public int hashCode() {
		return 7;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Order)) return false;
		Order o = (Order) obj;
		if (o.getId() == null || this.getId() == null) return false;
		return this.getId() == o.getId();
	}

	@Override
	public String toString() {
		return "Order ID: " + this.getId();
	}
}
