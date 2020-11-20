package com.example.books4share;

import android.graphics.drawable.Drawable;

public class Book {
    private String title;
    private String author;
    private String isbn;
    //private String description;
    private Status currentStatus;
    // Waiting for User class to be completed
    private Users usersId;
    private Users owner;
    private Users borrower;
    private IncomingRequest request;
    private Drawable image;



    public static enum Status{
        AVAILABLE, REQUESTED, ACCEPTED, BORROWED;
    }



    public Book(String title, String author, String isbn){

        this.author = author;
        this.title = title;
        this.isbn = isbn;
        this.currentStatus = Status.AVAILABLE;
        //this.usersId = usersId.getUid();
        this.owner = owner;

    }

    public Book(String title, String author, String isbn, Status status,Users owner,Users borrower){

        this.author = author;
        this.title = title;
        this.isbn = isbn;
        this.currentStatus = status;
        this.owner = owner;
        this.borrower = borrower;

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

    public Users getOwner() {
        return owner;
    }

    public void setOwner(Users owner) {
        this.owner = owner;
    }

    public Users getBorrower() {
        return borrower;
    }

    public void setBorrower(Users borrower) {
        this.borrower = borrower;
    }

    public IncomingRequest getRequest() {
        return request;
    }

    public void setRequest(IncomingRequest request) {
        this.request = request;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    //    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
}
