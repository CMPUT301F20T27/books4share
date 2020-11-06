package com.example.books4share;

public class Books {
    private String title;
    private String author;
    private String ISBN; // ？
    //picture

    private enum status{
        available("available",0),
        requested("requested",1),
        accepted("accepted",2),
        borrowed("borrowed",3);

        private String stringValue;
        private int intValue;
        private status(String stringValue, int intValue) {
            this.stringValue = stringValue;
            this.intValue = intValue;
        }

        public String getStringValue() {
            return stringValue;
        }

        public void setStringValue(String stringValue) {
            this.stringValue = stringValue;
        }

        public int getIntValue() {
            return intValue;
        }

        public void setIntValue(int intValue) {
            this.intValue = intValue;
        }
    }

    Books(String title,String author,String ISBN){

        this.author = author;
        this.title = title;

        this.ISBN = ISBN;

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

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }


}

