package com.example.BookManagement.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import com.example.BookManagement.model.Book;
import com.example.BookManagement.repository.BookRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private BookRepository bookRepository;

    public void sendConfirmationEmail(String email)
    {
        var message = new SimpleMailMessage();
        message.setFrom("shankadam129@gmail.com");
        message.setTo(email);
        message.setSubject("Connected with BookLedger");
        message.setText("You have successfully connected with BookLedger!");
        mailSender.send(message);
    }

    public void sendBookListEmail(String email) throws MessagingException {
        List<Book> books = bookRepository.findAllNonDeletedBooks(); 

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("shankadam129@gmail.com");
        helper.setTo(email);
        helper.setSubject("List of Available Books in BookLedger");

        StringBuilder emailContent = new StringBuilder("<h2>Available Books</h2>");
        emailContent.append("<table style='border-collapse: collapse; width: 100%;'>")
                    .append("<tr>")
                    .append("<th style='border: 1px solid black; padding: 8px;'>ID</th>")
                    .append("<th style='border: 1px solid black; padding: 8px;'>Title</th>")
                    .append("<th style='border: 1px solid black; padding: 8px;'>Information</th>")
                    .append("<th style='border: 1px solid black; padding: 8px;'>Year</th>")
                    .append("</tr>");

        for (Book book : books) {
            emailContent.append("<tr>")
                        .append("<td style='border: 1px solid black; padding: 8px;'>").append(book.getBookid()).append("</td>")
                        .append("<td style='border: 1px solid black; padding: 8px;'>").append(book.getBookname()).append("</td>")
                        .append("<td style='border: 1px solid black; padding: 8px;'>").append(book.getBookinfo()).append("</td>")
                        .append("<td style='border: 1px solid black; padding: 8px;'>").append(book.getPublishyr()).append("</td>")
                        .append("</tr>");
        }

        emailContent.append("</table>");
        helper.setText(emailContent.toString(), true);
        mailSender.send(message);
    }


    public void sendBookDeletedEmail(String email) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true); 

        helper.setFrom("shankadam129@gmail.com");
        helper.setTo(email);
        helper.setSubject("Successfully Book Deleted");

        String emailContent = "<h2>Book Deleted Successfully!</h2>"
                + "<p>The book has been successfully deleted from the BookLedger system.</p>";

        helper.setText(emailContent, true);
        mailSender.send(message);
    }
}
