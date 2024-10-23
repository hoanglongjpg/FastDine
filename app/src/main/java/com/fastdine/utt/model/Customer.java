package com.fastdine.utt.model;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;

public class Customer {
    private String customerId;
    private String email;
    private String name;
    private String phone;
    private String address;
    private FirebaseFirestore db;

    public Customer() {
        // Khởi tạo Firebase Firestore
        db = FirebaseFirestore.getInstance();
    }

    // Constructor
    public Customer(String customerId, String email, String name, String phone, String address) {
        this.customerId = "customer_" + customerId;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.address = address;
        db = FirebaseFirestore.getInstance();
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

    // Hàm lưu thông tin khách hàng vào Firestore
    public void saveCustomer() {
        db.collection("customers").document(customerId)
            .set(this, SetOptions.merge())
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // Lưu thành công
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Lưu thất bại
                }
            });
    }

    // Hàm lấy thông tin khách hàng từ Firestore
    public void getCustomerInfo(String customerId, OnSuccessListener<DocumentSnapshot> listener) {
        DocumentReference docRef = db.collection("customers").document(customerId);
        docRef.get().addOnSuccessListener(listener);
    }
}
