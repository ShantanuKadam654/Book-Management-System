package com.example.BookManagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.BookManagement.model.Book;
import com.example.BookManagement.model.admin; // Import your own admin model
import com.example.BookManagement.repository.AdminRepository;
import com.example.BookManagement.repository.BookRepository;
import com.example.BookManagement.repository.UserRepository;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String registerAdmin(String username, String rawPassword, String email) {
        if (adminRepository.findByUsername(username) != null) {
            return "Admin already exists";
        }

        admin newAdmin = new admin();
        newAdmin.setUsername(username);
        newAdmin.setPassword(passwordEncoder.encode(rawPassword));
        newAdmin.setEmail(email);
        adminRepository.save(newAdmin);

        return "Admin registered successfully";
    }
    
    public boolean authenticateAdmin(String username, String password) {
        admin admin = adminRepository.findByUsername(username);
        return admin != null && passwordEncoder.matches(password, admin.getPassword());
    }

    public int getTotalBooks() {
        return (int) bookRepository.count();
    }

    public int getTotalUsers() {
        return (int) userRepository.count();
    }

    public boolean deleteBookById(Long id) {
        Book book = bookRepository.findById(id).orElse(null);
        if (book != null) {
            book.setDeleted();
            bookRepository.save(book);
            return true;
        }
        return false;
    }

    public List<Book> findAllNonDeletedBooks() {
        return bookRepository.findAllNonDeletedBooks();
    }

    public List<Book> getAllActiveBooks() {
        return bookRepository.findAllActiveBooks();
    }
    
    public boolean softDeleteBookById(Long id) {
        Book book = bookRepository.findById(id).orElse(null);
        
        if (book != null) {
            book.setDeleted();  
            bookRepository.save(book);
            return true;  
        }
        
        return false;  
    }

}
