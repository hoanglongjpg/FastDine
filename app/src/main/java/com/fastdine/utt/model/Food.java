package com.fastdine.utt.model;

public class Food {
    private String name;
    private String description;
    private String image;
    private double price;

    public Food(String name, String description, String image, double price) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.price = price;
    }

    // Các phương thức getter
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public double getPrice() {
        return price;
    }
}
