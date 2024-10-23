package com.fastdine.utt.model;

import java.util.List;

public class Orders {
    private String order_id;
    private List<OrderItems> items;
    private String status;
    private Double totalPrice;

    public static class OrderItems{
        private String foodId;
        private String name;
        private int quantity;
        private double price;
    }
}
