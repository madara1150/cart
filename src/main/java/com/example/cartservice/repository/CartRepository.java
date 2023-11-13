package com.example.cartservice.repository;

import com.example.cartservice.pojo.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends MongoRepository<Cart, String> {

    @Query(value = "{customer_id:  '?0'}")
    Cart findByCustomerId(String customer_id);
}

