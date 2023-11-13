package com.example.cartservice.pojo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
@Data
public class OrderRestModel implements Serializable {
    private String wallet_id;
    private BigDecimal price;

    public OrderRestModel(String wallet_id, BigDecimal price) {
        this.wallet_id = wallet_id;
        this.price = price;
    }
}
