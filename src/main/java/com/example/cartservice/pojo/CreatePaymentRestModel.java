package com.example.cartservice.pojo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
@Builder

@Data
public class CreatePaymentRestModel {
    private List<OrderRestModel> orders;
    private BigDecimal amount;
    private BigDecimal tax;

    public CreatePaymentRestModel(List<OrderRestModel> orders, BigDecimal amount, BigDecimal tax) {
        this.orders = orders;
        this.amount = amount;
        this.tax = tax;
    }
}
