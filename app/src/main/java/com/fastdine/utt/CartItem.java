package com.fastdine.utt;

public class CartItem {
    private String name;
    private int quantity;
    private double price;
    private int imageResId;

    public CartItem(String name, int quantity, double price, int imageResId) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.imageResId = imageResId;
    }

    public double getPrice() {
        return price;
    }

    public String getName() {
    }

    public int getQuantity() {
    }

    public int getImageResId() {
        return 0;
    }

    public void setQuantity(int i) {
    }

    // Getter và Setter cho các thuộc tính
}

