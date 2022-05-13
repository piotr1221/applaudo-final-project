package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.checkout.Order;
import com.example.demo.entity.user.User;

public interface OrderRepository extends JpaRepository<Order, Long> {
	public List<Order> findByUser(User user);
}
