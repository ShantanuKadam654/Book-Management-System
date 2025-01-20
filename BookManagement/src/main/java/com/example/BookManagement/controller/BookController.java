package com.example.BookManagement.controller;

import com.example.BookManagement.model.Book;
import com.example.BookManagement.service.BookService;
import com.example.BookManagement.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.multipart.MultipartFile;
import jakarta.mail.MessagingException;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private EmailService emailService;

    @GetMapping()
    public String showBookList(Model model) { 
        List<Book> books = bookService.findAllBooksByDeletedStatus(0);  
        model.addAttribute("books", books);
        model.addAttribute("username");
        return "bookList"; 
    }

  
    @DeleteMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            boolean isDeleted = bookService.deleteBookById(id);
            if (isDeleted) {
                redirectAttributes.addFlashAttribute("message", "Book deleted successfully!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Book not found!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting the book!");
        }
        return "redirect:/books";
    }
    
    @GetMapping("/viewBook/{id}")
    public String viewBook(@PathVariable Long id, Model model) {
        Book book = bookService.findById(id);
        if (book != null) {
            model.addAttribute("book", book);
            return "viewBook";  
        }
        return "redirect:/books";  
    }

    @GetMapping("/aboutUs")
    public String aboutUs()
    {
    	return "aboutUs";
    }
}
