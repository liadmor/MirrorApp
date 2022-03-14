package com.example.mirrorapp;

import java.util.List;

public class User {

    public String fullName, age, email;
    public List<Post> posts;

    public User(){

    }

    public User(String fullName, String age, String email){
        this.age = age;
        this.fullName = fullName;
        this.email = email;
        this.posts = null;
    }

}
