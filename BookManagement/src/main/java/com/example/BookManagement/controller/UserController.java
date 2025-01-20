package com.example.BookManagement.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.BookManagement.model.Book;
import com.example.BookManagement.repository.BookRepository;
import com.example.BookManagement.service.AdminService;
import com.example.BookManagement.service.EmailService;
import com.example.BookManagement.service.UserService;
import jakarta.servlet.http.HttpSession;
import net.minidev.json.JSONObject;

import com.razorpay.*;

@CrossOrigin(origins = "*")
@Controller
public class UserController 
{

    @Autowired
    private UserService userService;
    
    @Autowired
    private EmailService emailService;

    @Autowired
    private AdminService adminService;
    
    @Autowired
    private BookRepository bookRepository; 

    @GetMapping("/login")
    public String loginPage() 
    {
        return "login";  
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String username, 
                            @RequestParam String password, 
                            HttpSession session, 
                            Model model) 
    {
        if (userService.authenticate(username, password))
        {
            session.setAttribute("username", username); 

            String email = userService.getUserEmailByUsername(username); 
            
            try 
            {
                emailService.sendConfirmationEmail(email);  
                emailService.sendBookListEmail(email);  
            } 
            catch (Exception e)
            {
                model.addAttribute("error", "Login successful, but failed to send emails.");
                e.printStackTrace();
            }

            return "redirect:/home";  
        } 
        else
        {
            model.addAttribute("error", "Invalid username or password for user");
            return "login";
        }
    }

    @GetMapping("/home")
    public String homePage(Model model, HttpSession session)
    {
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", username);
        
        return "home";  
    }
    
    @GetMapping("/register")
    public String register() 
    {
        return "register";  
    }
    
    @PostMapping("/register")
    public String registerUser(@RequestParam String username, 
                               @RequestParam String password, 
                               @RequestParam String email, 
                               @RequestParam String registerAs, 
                               Model model) 
    {
        String registrationResult;
        
        if ("admin".equalsIgnoreCase(registerAs))
        {
            registrationResult = adminService.registerAdmin(username, password, email);
        }
        else 
        {
            registrationResult = userService.registerUser(username, password, email);
        }

        model.addAttribute("message", registrationResult);  
        return "login";  
    }
    
    @GetMapping("/user/books")
    public String getBookList(Model model) 
    {
        List<Book> books = bookRepository.findByDeleted(0);  
        model.addAttribute("books", books);
        
        return "bookList";  
    }
    
    @PostMapping("/create_order")
    @ResponseBody
    public ResponseEntity<String> createOrder(@RequestBody Map<String, Object> data) 
    {
        try 
        {
            double amt = Double.parseDouble(data.get("amount").toString());
            int amountInPaise = (int) (amt * 100);
            var client = new RazorpayClient("rzp_test_etuDi8WEJCyOzZ", "0OrskCK0cd3KMXCuL7o64oKQ");

            org.json.JSONObject orderRequest = new org.json.JSONObject();
            orderRequest.put("amount", amountInPaise); 
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "receipt#1");

            Order order = client.orders.create(orderRequest);
            
            System.out.println("Razorpay Order Response: " + order.toString());
            
            return ResponseEntity.ok(order.toString());
        } 
        catch (Exception e)
        {  
            e.printStackTrace();
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating order: " + e.getMessage());
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) 
    {
        session.invalidate(); 
        return "redirect:/login";  
    }
}
