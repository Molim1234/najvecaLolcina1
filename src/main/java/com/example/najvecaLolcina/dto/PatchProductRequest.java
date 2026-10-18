package com.example.najvecaLolcina.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class PatchProductRequest {

    @Size(min = 2, max = 100)
    @Pattern(
            regexp = "(?s).*\\S.*",
            message = "Name must contain at least one non-whitespace character"
    )
    private String name;

    @PositiveOrZero
    private Integer quantity;

    @Positive
    @Digits(integer = 17, fraction = 2)
    private BigDecimal price;

    @Size(max = 250)
    private String description;

    public PatchProductRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
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

