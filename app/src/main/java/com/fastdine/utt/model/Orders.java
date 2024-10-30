package com.fastdine.utt.model;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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

    public Orders(){}

    // Getters and Setters
    private void setItems(List<Cart.CartItems> itemsList) {
        this.items = itemsList;
    }
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

    public int getTotalQuantity() {
        int totalQuantity = 0; // Initialize total quantity
        if (items != null) { // Ensure items list is not null
            for (Cart.CartItems item : items) {
                totalQuantity += item.getQuantity(); // Sum up quantities
            }
        }
        return totalQuantity; // Return the total quantity
    }

    public static void getOrderList(OnOrderListListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("orders")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Orders> ordersList = new ArrayList<>();
                        QuerySnapshot querySnapshot = task.getResult();

                        for (QueryDocumentSnapshot document : querySnapshot) {
                            // Chuyển đổi tài liệu thành đối tượng Orders
                            Orders order = document.toObject(Orders.class);
                            order.setOrderId(document.getId());

                            // Khôi phục items từ Map
                            Map<String, Map<String, Object>> itemsMap = (Map<String, Map<String, Object>>) document.get("items");
                            List<Cart.CartItems> itemsList = new ArrayList<>();

                            if (itemsMap != null) {
                                for (Map.Entry<String, Map<String, Object>> entry : itemsMap.entrySet()) {
                                    Cart.CartItems item = new Cart.CartItems();
                                    item.setId(entry.getKey()); // ID từ key của Map

                                    // Lấy thông tin từ giá trị (Map) của item
                                    Map<String, Object> itemData = entry.getValue();
                                    if (itemData != null) {
                                        item.setName((String) itemData.get("name"));
                                        item.setDescription((String) itemData.get("description"));
                                        item.setImage((String) itemData.get("image"));
                                        item.setPrice((Double) itemData.get("price"));
                                        item.setQuantity(((Long) itemData.get("quantity")).intValue()); // Chuyển đổi Long thành int
                                    }

                                    itemsList.add(item);
                                }
                            }

                            order.setItems(itemsList); // Gán danh sách items vào đối tượng Orders
                            ordersList.add(order);
                        }

                        listener.onOrderListReceived(ordersList);
                    } else {
                        listener.onError(task.getException());
                    }
                });
    }



    public static void addOrder(Orders order, OnOrderListener listener) {
        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        order.setCustomerId(userEmail);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Chuyển đổi danh sách món ăn trong đơn hàng thành một Map để lưu trữ trên Firestore
        Map<String, Object> itemsMap = new HashMap<>();
        for (Cart.CartItems item : order.items) { // Sử dụng getter để lấy danh sách món ăn
            Map<String, Object> itemData = new HashMap<>();
            itemData.put("id", item.getId());
            itemData.put("name", item.getName());
            itemData.put("description", item.getDescription());
            itemData.put("image", item.getImage());
            itemData.put("price", item.getPrice());
            itemData.put("quantity", item.getQuantity());
            itemsMap.put(item.getId(), itemData); // Lưu từng món ăn theo ID
        }

        // Tính tổng giá trị đơn hàng
        double total = 0;
        for (Cart.CartItems item : order.items) {
            total += item.getPrice() * item.getQuantity();
        }

        order.setTotalPrice(total);

        // Chuẩn bị dữ liệu để lưu lên Firestore
        Map<String, Object> orderData = new HashMap<>();
        orderData.put("customerId", order.getCustomerId());
        orderData.put("items", itemsMap); // Lưu itemsMap
        orderData.put("totalPrice", order.getTotalPrice());
        orderData.put("orderTime", order.getOrderTime());
        orderData.put("status", order.getStatus());

        // Thêm đơn hàng vào Firestore
        db.collection("orders")
                .add(orderData)
                .addOnSuccessListener(documentReference -> {
                    // Gán documentID vào trường orderId của đối tượng Orders
                    order.setOrderId(documentReference.getId());
                    listener.onComplete(order.getOrderId());
                })
                .addOnFailureListener(listener::onError);
    }


    public interface OnOrderListListener {
        void onOrderListReceived(List<Orders> ordersList);
        void onError(Exception e);
    }

    public interface OnOrderListener {
        void onComplete(String id);
        void onError(Exception e);
    }
}