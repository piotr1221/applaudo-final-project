package com.example.demo.entity.user;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.example.demo.entity.checkout.Order;
import com.example.demo.entity.checkout.ShoppingCart;
import com.example.demo.entity.payment.PaymentMethod;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="users")
public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String username;
	private String password;
	private String fullName;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumn(name="user_id", referencedColumnName="id")
	private List<Address> addresses;
	
	@OneToMany
	@JoinColumn(name="user_id", referencedColumnName="id")
	private List<PaymentMethod> paymentMethods;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumn(name="user_id", referencedColumnName="id")
	private List<Order> orders;
	
	@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private ShoppingCart shoppingCart;

	@Override
	public int hashCode() {
		return 17;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof User)) return false;
		User o = (User) obj;
		if (o.getId() == null || this.getId() == null) return false;
		return this.getId() == o.getId();
	}

	@Override
	public String toString() {
		return "ID: " + this.getId() + " - Username: " + this.username;
	}
	
}
