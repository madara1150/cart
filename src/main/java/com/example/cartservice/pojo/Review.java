package com.example.cartservice.pojo;

import lombok.Data;

@Data
public class Review {
    private String content;
    private double rate;
    private String owner_name;
    private boolean anonymous;

    public Review(String content, double rate, String owner_name, boolean anonymous) {
        this.content = content;
        this.rate = rate;
        this.owner_name = owner_name;
        this.anonymous = anonymous;
    }
}
