package com.example.cartservice.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Data
@Document("product")
public class Product implements Serializable {
    private String _id;
    private String name;
    private String description;
    private String img_path;
    private double price;
    private String category;
    private String shop_id;
    private double rating;
    private String create_at;
    private String edit_at;
    private List<Review> reviews;
    private int amount;

    public Product(String _id, String name, String description, String img_path, double price, String category, String shop_id, double rating, String create_at, String edit_at, List<Review> reviews, int amount) {
        this._id = _id;
        this.name = name;
        this.description = description;
        this.img_path = img_path;
        this.price = price;
        this.category = category;
        this.shop_id = shop_id;
        this.rating = rating;
        this.create_at = create_at;
        this.edit_at = edit_at;
        this.reviews = reviews;
        this.amount = amount;
    }


}
