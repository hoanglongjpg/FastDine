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

    public Customer(String name, String address, String phone) {
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
    public static void saveCustomer(final Customer.OnCustomerListener listener) {
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail(); // Lấy email người dùng hiện tại
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> customerData = new HashMap<>();
        customerData.put("name", "");      // Trường name để trống
        customerData.put("email", email);  // Lưu email người dùng
        customerData.put("address", "");   // Trường address để trống
        customerData.put("phone", "");     // Trường phone để trống

        db.collection("customers").document(email)
                .set(customerData, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    listener.onComplete();
                        // Lưu thành công
                })
                .addOnFailureListener(e -> {
                    listener.onError(e);
                        // Lưu thất bại
                });
    }

    // Hàm lấy thông tin khách hàng từ Firestore
    public void getCustomerInfo(String customerId, OnSuccessListener<DocumentSnapshot> listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("customers").document(customerId);
        docRef.get().addOnSuccessListener(listener);
    }

    public interface OnCustomerInfoListener {
        void onInfoLoaded(Customer customer);
        void onError(Exception e);
    }
}
