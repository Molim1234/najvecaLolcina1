package com.example.najvecaLolcina.entity;

import java.math.BigDecimal;

public class ProductDTO {

    private String name;
    private String type;
    private BigDecimal price;
    private String description;

    public ProductDTO(String name, String type, BigDecimal price, String description) {
        this.name = name;
        this.type = type;
        this.price = price;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
