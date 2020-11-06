// Zexin Cai is responsible for this part
// This is a data about an outgoing request

package com.example.books4share;

import java.io.Serializable;

public class OutgoingRequest implements Serializable {

    private String bookName;
    private String bookStatus;
    private String ownerName;
    private int index = -1;

    OutgoingRequest(String bookName, String bookStatus, String ownerName) {
        this.bookName = bookName;
        this.bookStatus = bookStatus;
        this.ownerName = ownerName;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookStatus() {
        return bookStatus;
    }

    public void setBookStatus(String bookStatus) {
        this.bookStatus = bookStatus;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
