package com.fastdine.utt.view;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fastdine.utt.R;
import com.fastdine.utt.controller.OwnerController;
import com.fastdine.utt.model.Orders;

import java.text.DecimalFormat;

public class OrderDetailActivity extends AppCompatActivity {
    private TextView txtOrderId, txtCustomerName, txtPhone, txtAddress, txtTotalAmount;
    private RecyclerView recyclerViewOrderItems;
    private OrderItemAdapter orderItemAdapter;
    private OwnerController ctrl;

    DecimalFormat currencyFormat = new DecimalFormat("#,###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        ImageButton btnBack = findViewById(R.id.backButton);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Ánh xạ các thành phần giao diện
        txtOrderId = findViewById(R.id.txtOrderId);
        txtCustomerName = findViewById(R.id.txtCustomerName);
        txtPhone = findViewById(R.id.txtPhone);
        txtAddress = findViewById(R.id.txtAddress);
        txtTotalAmount = findViewById(R.id.txtTotalAmount);
        recyclerViewOrderItems = findViewById(R.id.recyclerViewOrderItems);
        recyclerViewOrderItems.setLayoutManager(new LinearLayoutManager(this));

        // Nhận orderId từ Intent
        String orderId = getIntent().getStringExtra("orderId");

        // Gọi phương thức trong Controller để lấy chi tiết đơn hàng
        ctrl = new OwnerController(this);
        ctrl.viewOrderDetail(orderId, new Orders.OnDetailListener() {
            @Override
            public void onComplete(Orders order) {
                // Khi nhận được thông tin đơn hàng từ model, hiển thị lên giao diện
                String price = currencyFormat.format(order.getTotalPrice());
                txtOrderId.setText("Mã đơn hàng: " + order.getOrderId());
                txtCustomerName.setText("Tên khách hàng: " + order.getName());
                txtPhone.setText("Số điện thoại: " + order.getPhone());
                txtAddress.setText("Địa chỉ: " + order.getAddress());
                txtTotalAmount.setText("Tổng cộng: " + String.format("%sđ", price));

                // Cập nhật danh sách món ăn vào RecyclerView
                orderItemAdapter = new OrderItemAdapter(order.getItems());
                recyclerViewOrderItems.setAdapter(orderItemAdapter);
            }

            @Override
            public void onError(Exception e) {
                // Xử lý lỗi nếu có
                Toast.makeText(OrderDetailActivity.this, "Lỗi tải thông tin đơn hàng", Toast.LENGTH_SHORT).show();
            }
        });
    }
}