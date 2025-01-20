package com.example.BookManagement.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.BookManagement.model.Book;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT b FROM Book b WHERE b.deleted = 0")
    List<Book> findAllActiveBooks();
    
    @Query("SELECT b FROM Book b WHERE b.deleted = 0")
    List<Book> findAllNonDeletedBooks();
    
    List<Book> findByDeleted(int deletedStatus);    
}
