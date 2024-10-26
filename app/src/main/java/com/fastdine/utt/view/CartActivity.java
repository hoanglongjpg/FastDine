package com.fastdine.utt.view;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.fastdine.utt.R;
import com.fastdine.utt.controller.CustomerController;
import com.fastdine.utt.model.Cart;

import java.util.List;

public class CartActivity extends AppCompatActivity {
    private CustomerController ctrl;
    private RecyclerView cartRecyclerView;
    private TextView totalPriceText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        totalPriceText = findViewById(R.id.totalPriceText);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ctrl = new CustomerController(this);

        ctrl.viewCart(cartRecyclerView);
    }

    public void updateTotalPrice(double total) {
            totalPriceText.setText(String.format("%.2fđ", total)); // Hiển thị giá tổng
    }

    public void closeCart(View view) {
        finish();
    }
}
