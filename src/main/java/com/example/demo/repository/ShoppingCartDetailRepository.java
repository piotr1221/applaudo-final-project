package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.checkout.ShoppingCartDetail;

public interface ShoppingCartDetailRepository extends JpaRepository<ShoppingCartDetail, Integer> {

}
