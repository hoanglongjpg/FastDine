package com.fastdine.utt;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class CustomerActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FoodAdapter adapter;
    private List<Food> foodList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        // Khởi tạo RecyclerView và adapter
        recyclerView = findViewById(R.id.recyclerView_food_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        foodList = new ArrayList<>();
        adapter = new FoodAdapter(foodList);
        recyclerView.setAdapter(adapter);
        loadFoodData();


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

    private void loadFoodData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("foods")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        foodList.clear();  // Xóa danh sách cũ
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String name = document.getString("name");
                            String description = document.getString("description");
                            String image = document.getString("image");
                            double price = document.getDouble("price");

                            // Thêm món ăn vào danh sách
                            foodList.add(new Food(name, description, image, price));
                        }
                        // Cập nhật adapter sau khi lấy dữ liệu
                        adapter.notifyDataSetChanged();
                    } else {
                        // Xử lý lỗi nếu không lấy được dữ liệu
                        System.out.println("Lỗi khi lấy dữ liệu: " + task.getException());
                    }
                });
    }
}
