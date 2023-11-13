package com.example.cartservice.pojo;

public class UserInfo {
    private String first_name;
    private String last_name;
    private Address address;

    // Default Constructor
    public UserInfo() {
    }

    // Parameterized Constructor
    public UserInfo(String first_name, String last_name, Address address) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.address = address;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public Address getAddress() {
        return address;
    }

    // Getters and setters...
}