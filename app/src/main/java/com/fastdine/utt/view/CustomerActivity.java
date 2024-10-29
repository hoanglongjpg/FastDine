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
import com.google.android.material.bottomnavigation.BottomNavigationView;
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
        setContentView(R.layout.customer_view_profile);
        ctrl = new CustomerController(this);

        // Khởi tạo RecyclerView và adapter
        recyclerView = findViewById(R.id.recyclerView_food_list);
        ctrl.viewAvailableFood(recyclerView);
        ctrl.getCart();

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
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Thiết lập sự kiện khi nhấn vào các item trong BottomNavigationView
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_more) {
                // Mở ProfileActivity khi nhấn vào nav_more
                Intent intent = new Intent(CustomerActivity.this, ProfileActivity.class);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }
}
