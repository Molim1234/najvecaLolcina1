package com.example.najvecaLolcina.Entity;

public class ProductDTO {

    private String name;
    private String type;
    private double price;
    private String description;

    public ProductDTO(String name, String type, double price, String description) {
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
