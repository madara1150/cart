package com.example.cartservice.pojo;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Data
@Document("cart")
public class Cart {
    @Id
    private String _id;
    private List<Product> product;
    private String customer_id;

    public Cart(String _id, List<Product> product, String customer_id) {
        this._id = _id;
        this.product = product;
        this.customer_id = customer_id;

    }

    public List<Product> getProduct() {
        return product;
    }


}
