package com.example.demo.entity.user;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.example.demo.entity.checkout.Order;
import com.example.demo.entity.checkout.ShoppingCart;
import com.example.demo.entity.payment.PaymentMethod;

import lombok.Getter;

@Entity
@Getter
@Table(name="users")
public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String username;
	private String password;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumn(name="user_id", referencedColumnName="id")
	private List<Address> addresses;
	
	@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="users")
	private List<PaymentMethod> paymentMethods;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumn(name="user_id", referencedColumnName="id")
	private List<Order> orders;
	
	@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
//	@Transient
	private ShoppingCart shoppingCart;
}
