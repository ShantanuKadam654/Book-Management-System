package com.example.BookManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.BookManagement.model.user;

@Repository
public interface UserRepository extends JpaRepository<user, Long>
{
    user findByUsername(String username);
}
