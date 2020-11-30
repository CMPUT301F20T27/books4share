package com.example.books4share;
/**
 * @author Wei Shanshan
 */

import java.io.Serializable;

/**
 * This class is user's contact information
 */

public class ProfileUser implements Serializable {
    private String userName;
    private String phone;
    private String address;

    public ProfileUser() {
    }

    public ProfileUser(String userName, String phone, String address) {

        this.userName = userName;
        this.phone = phone;
        this.address = address;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
