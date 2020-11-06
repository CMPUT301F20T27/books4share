package com.example.books4share;

public class Book {
    private String title;
    private String author;
    private String isbn;
    private String description;
    private Status currentStatus;
    // Waiting for User class to be completed



    public static enum Status{
        AVAILABLE, REQUESTED, ACCEPTED, BORROWED;
    }


    public Book(String title, String author, String isbn){

        this.author = author;
        this.title = title;
        this.isbn = isbn;
        this.currentStatus = Status.AVAILABLE;

    }

    public Book(String title, String author, String isbn, Status status){

        this.author = author;
        this.title = title;
        this.isbn = isbn;
        this.currentStatus = status;

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

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }


    public Status getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(Status currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
