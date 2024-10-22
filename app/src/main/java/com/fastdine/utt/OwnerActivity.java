package com.fastdine.utt;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class OwnerActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FoodAdapter adapter;
    private List<Food> foodList;

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
    }
}
