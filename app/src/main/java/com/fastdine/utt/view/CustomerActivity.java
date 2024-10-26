package com.fastdine.utt.view;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fastdine.utt.R;
import com.fastdine.utt.controller.CustomerController;
import com.fastdine.utt.controller.OwnerController;
import com.fastdine.utt.model.Food;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class CustomerActivity extends AppCompatActivity {
    private CustomerController ctrl;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        ctrl = new CustomerController(this);

        // Khởi tạo RecyclerView và adapter
        recyclerView = findViewById(R.id.recyclerView_food_list);
        ctrl.viewAvailableFood(recyclerView);

        // Tìm ImageButton giỏ hàng và thiết lập sự kiện click
        ImageButton cartButton = findViewById(R.id.cartButton);
        ((View) cartButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang CartActivity khi bấm vào icon giỏ hàng
                Intent intent = new Intent(CustomerActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });
    }
}
