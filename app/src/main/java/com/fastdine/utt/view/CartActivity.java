package com.fastdine.utt.view;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fastdine.utt.R;
import com.fastdine.utt.model.CartItem;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private RecyclerView cartRecyclerView;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Khởi tạo danh sách item giả
        cartItemList = new ArrayList<>();
        cartItemList.add(new CartItem("Trà Chanh Nha Đam", 2, 17000, R.drawable.ic_image));
        // Thêm nhiều item hơn nếu cần

        // Thiết lập adapter
        cartAdapter = new CartAdapter(cartItemList);
        cartRecyclerView.setAdapter(cartAdapter);
    }

    public void closeCart(View view) {
        finish();
    }
}
