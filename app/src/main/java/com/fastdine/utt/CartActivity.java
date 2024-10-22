package com.fastdine.utt;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class CartActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
    }

    // Đóng giao diện giỏ hàng khi người dùng bấm nút đóng
    public void closeCart(View view) {
        finish();  // Đóng CartActivity và quay về CustomerActivity
    }
}
