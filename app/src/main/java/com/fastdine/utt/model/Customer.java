package com.fastdine.utt.model;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Customer {
    private String customerId;
    private String email;
    private String name;
    private String phone;
    private String address;


    public Customer() {
    }

    // Constructor
    public Customer(String customerId, String email, String name, String phone, String address) {
        this.customerId = "customer_" + customerId;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.address = address;
    }

    public Customer(String address, String email, String name, String phone) {
        this.customerId = "customer_" + customerId;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.address = address;
    }

    // Getters and Setters
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = "customer_" + customerId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public interface OnCustomerListener {
        void onComplete();

        void onError(Exception e);
    }

    // Hàm lưu thông tin khách hàng vào Firestore
    public static void saveCustomer(String name, String address, String phone, OnCustomerListener listener) {
        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Tạo một Map với các thông tin mới
        Map<String, Object> customerData = new HashMap<>();
        customerData.put("name", name);
        customerData.put("address", address);
        customerData.put("phone", phone);
        customerData.put("email", userEmail); // Nếu muốn cập nhật email

        // Cập nhật vào Firestore
        db.collection("customers").document(userEmail)
                .set(customerData, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    listener.onComplete();
                })
                .addOnFailureListener(e -> {
                    listener.onError(e);
                });
    }

    public static void getCustomerInfo(OnCustomerInfoListener listener) {
        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference customerRef = db.collection("customers").document(userEmail);

        customerRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null && task.getResult().exists()) {
                DocumentSnapshot document = task.getResult();
                Customer customer = new Customer();
                customer.setName(document.getString("name"));
                customer.setPhone(document.getString("phone"));
                customer.setEmail(document.getString("email"));
                customer.setAddress(document.getString("address"));
                listener.onComplete(customer);
            } else {
                listener.onError(task.getException());
            }
        });
    }

    // Giao diện để nhận thông tin khách hàng
    public interface OnCustomerInfoListener {
        void onComplete(Customer customer);
        void onError(Exception e);
    }
}
