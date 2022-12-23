package com.bookshopfront.model;


import java.util.Objects;

public class Book {

    private int id;

    private String title;
    private String author;
    private int quantity;
    private String genre;
    private double price;
    public Book(){

    }
    public Book(int id, String title, String author, int quantity, String genre, double price) {
        super();
        this.id = id;
        this.title = title;
        this.author = author;
        this.quantity = quantity;
        this.genre = genre;
        this.price = price;
    }

    public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        return "Book [id=" + id + ", title=" + title + ", author=" + author + ", quantity=" + quantity + ", genre="
                + genre + "]";
    }

}