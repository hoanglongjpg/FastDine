package com.fastdine.utt.model;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

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
                .orderBy("orderTime", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Orders> ordersList = new ArrayList<>();
                        QuerySnapshot querySnapshot = task.getResult();

                        for (QueryDocumentSnapshot document : querySnapshot) {
                            // Chuyển đổi tài liệu thành đối tượng Orders
                            Orders order = document.toObject(Orders.class);
                            order.setOrderId(document.getId());

                            // Khôi phục items từ List
                            List<Map<String, Object>> itemsList = (List<Map<String, Object>>) document.get("items");
                            List<Cart.CartItems> cartItemsList = new ArrayList<>();

                            if (itemsList != null) {
                                for (Map<String, Object> itemData : itemsList) {
                                    Cart.CartItems item = new Cart.CartItems();
                                    item.setId((String) itemData.get("id")); // Lấy ID từ itemData

                                    // Lấy thông tin từ itemData
                                    item.setName((String) itemData.get("name"));
                                    item.setDescription((String) itemData.get("description"));
                                    item.setImage((String) itemData.get("image"));
                                    item.setPrice((Double) itemData.get("price"));
                                    item.setQuantity(((Long) itemData.get("quantity")).intValue()); // Chuyển đổi Long thành int

                                    cartItemsList.add(item); // Thêm item vào danh sách cartItemsList
                                }
                            }

                            order.setItems(cartItemsList); // Gán danh sách items vào đối tượng Orders
                            ordersList.add(order); // Thêm đơn hàng vào danh sách
                        }

                        listener.onOrderListReceived(ordersList);
                    } else {
                        listener.onError(task.getException());
                    }
                });
    }

    public static void getOrderListCustomer(String customerId, OnOrderListListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Lọc đơn hàng theo customer_id và sắp xếp theo orderTime
        db.collection("orders")
                .whereEqualTo("customer_id", customerId) // Lọc theo customer_id
                .orderBy("orderTime", Query.Direction.DESCENDING) // Sắp xếp theo orderTime từ mới nhất đến cũ nhất
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Orders> ordersList = new ArrayList<>();
                        QuerySnapshot querySnapshot = task.getResult();

                        for (QueryDocumentSnapshot document : querySnapshot) {
                            // Chuyển đổi tài liệu thành đối tượng Orders
                            Orders order = document.toObject(Orders.class);
                            order.setOrderId(document.getId());

                            // Khôi phục items từ List
                            List<Map<String, Object>> itemsList = (List<Map<String, Object>>) document.get("items");
                            List<Cart.CartItems> cartItemsList = new ArrayList<>();

                            if (itemsList != null) {
                                for (Map<String, Object> itemData : itemsList) {
                                    Cart.CartItems item = new Cart.CartItems();
                                    item.setId((String) itemData.get("id")); // Lấy ID từ itemData

                                    // Lấy thông tin từ itemData
                                    item.setName((String) itemData.get("name"));
                                    item.setDescription((String) itemData.get("description"));
                                    item.setImage((String) itemData.get("image"));
                                    item.setPrice((Double) itemData.get("price"));
                                    item.setQuantity(((Long) itemData.get("quantity")).intValue()); // Chuyển đổi Long thành int

                                    cartItemsList.add(item); // Thêm item vào danh sách cartItemsList
                                }
                            }

                            order.setItems(cartItemsList); // Gán danh sách items vào đối tượng Orders
                            ordersList.add(order); // Thêm đơn hàng vào danh sách
                        }

                        listener.onOrderListReceived(ordersList);
                    } else {
                        listener.onError(task.getException());
                    }
                });
    }


    public static void addOrder(Orders order, OnOrderListener listener) {
        // Lấy email của khách hàng
        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        order.setCustomerId(userEmail); // Đảm bảo email của khách hàng được đặt vào trường customer_id
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Tạo document ID theo định dạng yêu cầu
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy-HHmmss", Locale.getDefault());
        String currentTime = dateFormat.format(new Date());
        String randomDigits = String.format("%04d", new Random().nextInt(10000));
        String documentId = currentTime + "-" + randomDigits;

        // Chuyển đổi danh sách món ăn trong đơn hàng thành một List để lưu trữ trên Firestore
        List<Map<String, Object>> itemsList = new ArrayList<>();
        for (Cart.CartItems item : order.items) {
            Map<String, Object> itemData = new HashMap<>();
            itemData.put("id", item.getId());
            itemData.put("name", item.getName());
            itemData.put("description", item.getDescription());
            itemData.put("image", item.getImage());
            itemData.put("price", item.getPrice());
            itemData.put("quantity", item.getQuantity());
            itemsList.add(itemData);
        }

        double total = 0;
        for (Cart.CartItems item : order.items) {
            total += item.getPrice() * item.getQuantity();
        }
        order.setTotalPrice(total);

        // Chuẩn bị dữ liệu để lưu lên Firestore, bao gồm trường customer_id
        Map<String, Object> orderData = new HashMap<>();
        orderData.put("customer_id", order.getCustomerId());
        orderData.put("name", order.getName());
        orderData.put("address", order.getAddress());
        orderData.put("phone", order.getPhone());
        orderData.put("items", itemsList);
        orderData.put("orderTime", order.orderTime);
        orderData.put("status", order.getStatus());
        orderData.put("totalPrice", order.getTotalPrice());

        // Thêm đơn hàng vào Firestore với document ID tùy chỉnh
        db.collection("orders")
                .document(documentId)
                .set(orderData)
                .addOnSuccessListener(aVoid -> {
                    // Gán documentID vào trường id của đối tượng Orders
                    order.setOrderId(documentId);
                    listener.onComplete(order.getOrderId());
                })
                .addOnFailureListener(listener::onError);
    }

    public static void updateStatus(String orderId, String newStatus, OnOrderListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Cập nhật trạng thái đơn hàng trong Firestore
        db.collection("orders")
                .document(orderId)
                .update("status", newStatus)
                .addOnSuccessListener(aVoid -> {
                    // Cập nhật thành công
                    listener.onComplete(orderId);
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