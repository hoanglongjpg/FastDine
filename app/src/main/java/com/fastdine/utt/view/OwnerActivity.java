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

public class OwnerActivity extends AppCompatActivity {
    private OwnerController ctrl;
    private RecyclerView recyclerView;
    private FoodAdapter adapter;
    private List<Food> foodList;

    //Khởi tạo giao diện
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner);
        ctrl = new OwnerController(this);

        // Khởi tạo RecyclerView và adapter
        recyclerView = findViewById(R.id.recyclerView_food_list);
        OwnerController ctrl = new OwnerController (this);
        ctrl.viewFoodList(recyclerView);

        // Xử lý sự kiện khi nhấn nút "Thêm"
        FloatingActionButton fabAddFood = findViewById(R.id.add_food);
        fabAddFood.setOnClickListener(v -> ctrl.addFood(recyclerView, this));
    }

}
