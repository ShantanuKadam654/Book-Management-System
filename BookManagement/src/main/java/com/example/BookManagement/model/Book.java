package com.example.BookManagement.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "booktb")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookid;
    private String bookname;
    private double bookprice;
    private int publishyr;
    private String bookphoto;
    private String bookinfo;
    private String booktype;
    private int deleted = 0;  

    public Book() {}

    public Book(String bookname, double bookprice, int publishyr, String bookphoto, String bookinfo, String booktype) {
        this.bookname = bookname;
        this.bookprice = bookprice;
        this.publishyr = publishyr;
        this.bookphoto = bookphoto;
        this.bookinfo = bookinfo;
        this.booktype = booktype;
    }

    public Long getBookid() {
        return bookid;
    }

    public void setBookid(Long bookid) {
        this.bookid = bookid;
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public double getBookprice() {
        return bookprice;
    }

    public void setBookprice(double bookprice) {
        this.bookprice = bookprice;
    }

    public int getPublishyr() {
        return publishyr;
    }

    public void setPublishyr(int publishyr) {
        this.publishyr = publishyr;
    }

    public String getBookphoto() {
        return bookphoto;
    }

    public void setBookphoto(String bookphoto) {
        this.bookphoto = bookphoto;
    }

    public String getBookinfo() {
        return bookinfo;
    }

    public void setBookinfo(String bookinfo) {
        this.bookinfo = bookinfo;
    }

    public String getBooktype() {
        return booktype;
    }

    public void setBooktype(String booktype) {
        this.booktype = booktype;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted() {
        this.deleted = 1;  
    }

}
