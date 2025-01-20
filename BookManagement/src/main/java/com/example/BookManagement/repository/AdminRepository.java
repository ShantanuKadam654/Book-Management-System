package com.example.BookManagement.repository;

import com.example.BookManagement.model.admin; 

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<admin, Long> {
    admin findByUsername(String username);  
}
