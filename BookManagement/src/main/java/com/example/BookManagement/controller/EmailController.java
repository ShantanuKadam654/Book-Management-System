package com.example.BookManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.example.BookManagement.service.EmailService;
import org.springframework.stereotype.Controller;

@Controller
public class EmailController 
{
    @Autowired
    private EmailService emailService;

    @PostMapping("/connect")
    @ResponseBody
    public String connect(@RequestParam("email") String email) 
    {
        try 
        {
            emailService.sendConfirmationEmail(email); 
            
            return "success"; 
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            return "fail";  
        }
    }
}
