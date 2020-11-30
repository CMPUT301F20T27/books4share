package com.example.books4share.bean;


import java.io.Serializable;

/**
 * Book Object class
 */
public class Book  implements Serializable {
    private String id;
    public String title;
    public String author;
    public String isbn;
    public String currentStatus;
    public String usersId;
    public String image;
  //  String[] status = {"Borrowed", "Requested", "Available", "Available"};
//    public static enum Status{
//        AVAILABLE, REQUESTED, ACCEPTED, BORROWED;
//    }
//    available：
//    requested：
//    accepted：
//    borrowed：

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getUsersId() {
        return usersId;
    }

    public void setUsersId(String usersId) {
        this.usersId = usersId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public Book() {
    }






}
