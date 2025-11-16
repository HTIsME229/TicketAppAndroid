package com.example.ticketapp.domain.model;

public class Account {
        private String uid;
        private String username;

        private String email;
    public Account() {
    }


    public Account(String uid, String username, String email) {
        this.username = username;
        this.email = email;
        this.uid = uid;

    }
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
