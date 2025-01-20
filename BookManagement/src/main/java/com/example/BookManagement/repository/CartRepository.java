package com.example.BookManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.BookManagement.model.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
}
