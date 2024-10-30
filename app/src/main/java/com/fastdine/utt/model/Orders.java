package com.fastdine.utt.model;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Orders {
    private String orderId;
    private String customerId;
    private List<Cart.CartItems> items;
    private String name;
    private String address;
    private Date orderTime;
    private String phone;
    private String status;
    private double totalPrice;
    

    public Orders(String name, String address, String phone, List<Cart.CartItems> items, Date orderTime, String status) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.items = items;
        this.orderTime = orderTime;
        this.status = status;
    }

    //public Orders(){}

    // Getters and Setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Map<String, Object> getItems() {
        return (Map<String, Object>) items;
    }

    public void setItems(Map<String, Object> items) {
        this.items = (List<Cart.CartItems>) items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void saveOrder(OnOrderListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> orderData = new HashMap<>();
        orderData.put("customerId", this.customerId);
        orderData.put("items", this.items);
        orderData.put("name", this.name);
        orderData.put("orderTime", this.orderTime);
        orderData.put("phone", this.phone);
        orderData.put("status", this.status);
        orderData.put("totalPrice", this.totalPrice);

        db.collection("orders").add(orderData)
                .addOnSuccessListener(documentReference -> {
                    listener.onComplete(documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    listener.onError(e);
                });
    }

    public static void addOrder(Orders order, OnOrderListener listener) {
        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        order.setCustomerId(userEmail);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Chuyển đổi danh sách món ăn trong đơn hàng thành một Map để lưu trữ trên Firestore
        Map<String, List<Object>> itemsMap = new HashMap<>();
        for (Cart.CartItems item : order.items) {
            List<Object> itemData = new ArrayList<>();
            itemData.add(item.getId());
            itemData.add(item.getName());
            itemData.add(item.getDescription());
            itemData.add(item.getImage());
            itemData.add(item.getPrice());
            itemData.add(item.getQuantity());
            itemsMap.put(item.getId(), itemData);
        }

        double total = 0;
        for (Cart.CartItems item : order.items) {
            total += item.getPrice() * item.getQuantity();
        }

        order.setTotalPrice(total);
        // Chuẩn bị dữ liệu để lưu lên Firestore
        Map<String, Object> orderData = new HashMap<>();
        orderData.put("customer_id", order.getCustomerId());
        orderData.put("name", order.getName());
        orderData.put("address", order.getAddress());
        orderData.put("phone", order.getPhone());
        orderData.put("items", itemsMap);
        orderData.put("orderTime", order.orderTime);
        orderData.put("status", order.getStatus());
        orderData.put("totalPrice", order.getTotalPrice());

        // Thêm đơn hàng vào Firestore
        db.collection("orders")
                .add(orderData)
                .addOnSuccessListener(documentReference -> {
                    // Gán documentID vào trường id của đối tượng Orders
                    order.setOrderId(documentReference.getId());
                    listener.onComplete(order.getOrderId());
                })
                .addOnFailureListener(listener::onError);
    }

    public interface OnOrderListener {
        void onComplete(String id);
        void onError(Exception e);
    }
}