package com.example.BookManagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.BookManagement.model.Cart;
import com.example.BookManagement.service.CartService;

@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    // Endpoint to add a book to the cart
    @PostMapping("/add_to_cart/{bookId}")
    public String addToCart(@PathVariable("bookId") int bookId, @RequestParam("quantity") int quantity) {
        try {
            cartService.addToCart(bookId, quantity);
            return "redirect:/books"; // Redirect to book list after adding
        } catch (Exception e) {
            e.printStackTrace();
            return "error"; // Error page in case of an issue
        }
    }

    // Endpoint to view the cart
    @GetMapping("/cart")
    public String viewCart(Model model) {
        try {
            List<Cart> cartItems = cartService.getCartItems();
            double total = cartService.getCartTotal(); // Get the total price of the cart
            model.addAttribute("cartItems", cartItems);
            model.addAttribute("total", total); // Add total to model
            return "cart"; // Render cart view
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Unable to fetch cart items.");
            return "error";
        }
    }

 // Endpoint to delete a cart item
    @PostMapping("/cart/delete")
    public String deleteFromCart(@RequestParam("cartid") int cartid) {
        try {
            cartService.deleteCartItem(cartid);  // Pass cartid instead of bookid
            return "redirect:/cart"; // Redirect to cart view after deletion
        } catch (Exception e) {
            e.printStackTrace();
            return "error"; // Show error page if an issue occurs
        }
    }

}
