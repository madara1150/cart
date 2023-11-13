package com.example.cartservice.pojo;
import lombok.Data;
import java.util.Date;

import lombok.NonNull;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;
@Data
@Document("User")
public class User {
    @Id
    private String _id;
    private String email;
    private String password;
    private boolean has_shop;
    @LastModifiedDate
    private Date updated_at;
    @CreatedDate
    private Date created_at;
    private UserInfo info;

    // Default Constructor
    public User() {
    }

    // Parameterized Constructor
    public User(String _id, String email, String password, boolean has_shop, Date updated_at, Date created_at, UserInfo info) {
        this._id = _id;
        this.email = email;
        this.password = password;
        this.has_shop = has_shop;
        this.updated_at = updated_at;
        this.created_at = created_at;
        this.info = info;
    }
}
