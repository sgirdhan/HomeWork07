package com.example.sharangirdhani.homework07;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sharangirdhani on 11/20/17.
 */

public class User implements Serializable {
    private String user_id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Date dob;

    public ArrayList<Post> posts;
    public ArrayList<User> friends;

    public User() {}

    public User(String user_id, String firstName, String lastName, String email, String password, Date dob) {
        this.user_id = user_id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.dob = dob;

        this.posts = new ArrayList<>();
        this.friends = new ArrayList<>();
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }
}
