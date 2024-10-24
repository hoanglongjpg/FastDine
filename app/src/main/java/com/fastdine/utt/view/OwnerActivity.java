package com.fastdine.utt.view;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fastdine.utt.controller.OwnerController;
import com.fastdine.utt.R;
import com.fastdine.utt.model.Food;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class OwnerActivity extends AppCompatActivity implements OwnerController.AddFoodListener {

    private RecyclerView recyclerView;
    private FoodAdapter adapter;
    private List<Food> foodList;

    //Khởi tạo giao diện
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner);

        // Khởi tạo RecyclerView và adapter
        recyclerView = findViewById(R.id.recyclerView_food_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        foodList = new ArrayList<>();
        adapter = new FoodAdapter(foodList);
        recyclerView.setAdapter(adapter);

        // Lấy dữ liệu từ Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("foods")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
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
        // Xử lý sự kiện khi nhấn nút "Thêm"
        FloatingActionButton fabAddFood = findViewById(R.id.add_food);
        fabAddFood.setOnClickListener(v -> new OwnerController(this, this).showAddFoodDialog());
    }

    // Hàm lấy dữ liệu món ăn từ Firestore
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

    // Triển khai hàm callback để cập nhật RecyclerView khi thêm món ăn thành công
    @Override
    public void onFoodAdded() {
        // Gọi lại loadFoodData để cập nhật danh sách món ăn
        loadFoodData();
    }
}
