package com.example.BookManagement.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.BookManagement.model.Book;
import com.example.BookManagement.repository.BookRepository;
import com.example.BookManagement.service.AdminService;
import com.example.BookManagement.service.BookService;

import jakarta.mail.MessagingException;


@Controller
public class AdminController {

    @Autowired
    private AdminService adminService;
    
    @Autowired
    private BookService bookService;
    
    @Autowired
    private BookRepository bookRepository;
    
    
    @GetMapping("/admin_dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("totalBooks", adminService.getTotalBooks());
        model.addAttribute("totalUsers", adminService.getTotalUsers());
        model.addAttribute("books", adminService.findAllNonDeletedBooks());
        return "admin_dashboard";
    }

    @PostMapping("/adminLogin")
    public String adminLogin(@RequestParam("username") String username, @RequestParam("password") String password, Model model) {
        if (adminService.authenticateAdmin(username, password)) {
            return "redirect:/admin_dashboard";
        } else {
            model.addAttribute("error", "Invalid username or password for admin");
            return "login";
        }
    }
    
    @PostMapping("/add")
    public String addBook(@RequestParam("bookname") String bookname, @RequestParam("bookprice") double bookprice,
                          @RequestParam("publishyr") int publishyr, @RequestParam("bookphoto") MultipartFile bookphoto,
                          @RequestParam("bookinfo") String bookinfo, @RequestParam("booktype") String booktype,
                          RedirectAttributes redirectAttributes) {
        try {
            String uploadDir = "D:\\Sumago\\Img";
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String fileName = System.currentTimeMillis() + "_" + bookphoto.getOriginalFilename();
            File photoFile = new File(uploadDir, fileName);
            bookphoto.transferTo(photoFile);

            Book newBook = new Book(bookname, bookprice, publishyr, fileName, bookinfo, booktype);
            bookService.saveBook(newBook);

            redirectAttributes.addFlashAttribute("message", "Book added successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error uploading the photo!");
        }

        return "redirect:/admin_dashboard"; 
    }

    
    @GetMapping("/add")
    public String showAddBookForm(Model model) {
        return "addBook";  
    }

    @GetMapping("/editBook/{id}")
    public String showEditBookForm(@PathVariable Long id, Model model) {
        Book book = bookService.findById(id);
        if (book != null) {
            model.addAttribute("book", book);
            return "editBook";
        }
        return "redirect:/admin_dashboard";
    }

    @PostMapping("/edit")
    public String editBook(@RequestParam("bookid") Long bookid, @RequestParam("bookname") String bookname,
                           @RequestParam("bookprice") double bookprice, @RequestParam("publishyr") int publishyr,
                           @RequestParam("booktype") String booktype, @RequestParam("bookinfo") String bookinfo,
                           @RequestParam(value = "bookphoto", required = false) MultipartFile bookphoto,
                           RedirectAttributes redirectAttributes) {
        try {
            Book existingBook = bookService.findById(bookid);
            if (existingBook != null) {
                existingBook.setBookname(bookname);
                existingBook.setBookprice(bookprice);
                existingBook.setPublishyr(publishyr);
                existingBook.setBooktype(booktype);
                existingBook.setBookinfo(bookinfo);

                if (bookphoto != null && !bookphoto.isEmpty()) {
                    String uploadDir = "D:\\Sumago\\Img";
                    String fileName = System.currentTimeMillis() + "_" + bookphoto.getOriginalFilename();
                    File photoFile = new File(uploadDir, fileName);
                    bookphoto.transferTo(photoFile);
                    existingBook.setBookphoto(fileName);
                }

                bookService.saveBook(existingBook);
                redirectAttributes.addFlashAttribute("message", "Book updated successfully!");
            }
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Error updating the book!");
        }
        return "redirect:/admin_dashboard";
    }
    
    @DeleteMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        boolean isSoftDeleted = adminService.softDeleteBookById(id);
        if (isSoftDeleted) {
            redirectAttributes.addFlashAttribute("message", "Book marked as deleted successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Book not found!");
        }
        return "redirect:/admin_dashboard";
    }

}
