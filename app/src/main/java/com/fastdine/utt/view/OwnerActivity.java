package com.fastdine.utt.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button; // Thêm import cho Button
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fastdine.utt.controller.OwnerController;
import com.fastdine.utt.R;
import com.fastdine.utt.model.Food;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class OwnerActivity extends AppCompatActivity {
    private OwnerController ctrl;
    private RecyclerView recyclerView;
    private BottomNavigationView bottomNavigationView;

    //Khởi tạo giao diện
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner);
        ctrl = new OwnerController(this);

        // Khởi tạo RecyclerView và adapter
        recyclerView = findViewById(R.id.recyclerView_food_list);
        ctrl.viewFoodList(recyclerView);

        // Khởi tạo và xử lý sự kiện cho BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_food) {
                // Hiển thị danh sách món ăn
                ctrl.viewFoodList(recyclerView);
                return true;
            } else if (item.getItemId() == R.id.nav_orders) {
                // Hiển thị danh sách đơn hàng
                ctrl.viewOrderList(recyclerView);
                return true;
            }
            return false;
        });

        // Xử lý sự kiện khi nhấn nút "Thêm"
        FloatingActionButton fabAddFood = findViewById(R.id.add_food);
        fabAddFood.setOnClickListener(v -> ctrl.addFood(recyclerView, this));

        // Khởi tạo nút đăng xuất
        FloatingActionButton logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(v -> {
            // Xóa thông tin đăng nhập hoặc thực hiện các hành động cần thiết
            // Ví dụ: Chuyển đến LoginActivity
            Intent intent = new Intent(OwnerActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // Kết thúc OwnerActivity
        });
    }
}
