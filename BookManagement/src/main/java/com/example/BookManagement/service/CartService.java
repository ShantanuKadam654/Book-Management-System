package com.example.BookManagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.BookManagement.model.Cart;
import com.example.BookManagement.model.Book;
import com.example.BookManagement.repository.BookRepository;
import com.example.BookManagement.repository.CartRepository;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private BookRepository bookRepository;

    // Add book to cart
    public void addToCart(int bookId, int quantity) throws Exception {
        Book book = bookRepository.findById((long) bookId).orElseThrow(() -> new Exception("Book not found"));

        Cart cart = new Cart();
        cart.setBook(book);
        cart.setQuantity(quantity);
        cart.setBookname(book.getBookname());
        cart.setPrice(book.getBookprice());
        cartRepository.save(cart);
        
    }

    // Fetch all cart items
    public List<Cart> getCartItems() {
        return cartRepository.findAll();
    }

 // Service to calculate total cart price
    public double getCartTotal() {
        return cartRepository.findAll().stream()
                .mapToDouble(cart -> cart.getQuantity() * cart.getBook().getBookprice()) // Multiply quantity by price
                .sum(); // Sum up the total
    }

    
    public void deleteCartItem(int cartid) {
        cartRepository.deleteById(cartid);  // Delete by cartid, not bookid
    }
}
