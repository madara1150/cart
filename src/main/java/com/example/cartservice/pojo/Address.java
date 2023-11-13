package com.example.cartservice.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
public class Address {
    private String province;
    private String tambon;
    private String amphur;
    private int postalcode;
    private String more;

    // Default Constructor
    public Address() {
    }

    // Parameterized Constructor
    public Address(String province, String tambon, String amphur, int postalcode, String more) {
        this.province = province;
        this.tambon = tambon;
        this.amphur = amphur;
        this.postalcode = postalcode;
        this.more = more;
    }

    // Getters and setters...
}