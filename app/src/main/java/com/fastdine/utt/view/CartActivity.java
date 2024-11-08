package com.fastdine.utt.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.fastdine.utt.R;
import com.fastdine.utt.controller.CustomerController;

import java.text.DecimalFormat;

public class CartActivity extends AppCompatActivity {
    private CustomerController ctrl;
    public RecyclerView cartRecyclerView;
    private TextView totalPriceText;

    DecimalFormat currencyFormat = new DecimalFormat("#,###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cart);
        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        totalPriceText = findViewById(R.id.totalPriceText);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ctrl = new CustomerController(this);

        ctrl.viewCart(cartRecyclerView);

        findViewById(R.id.clearCartButton).setOnClickListener(v -> showDeleteConfirmationDialog());

        //Sự kiện nút Giao hàng
        Button orderButton = findViewById(R.id.orderButton);
        orderButton.setOnClickListener(v -> ctrl.showOrderDialog(this));
    }

    public void updateTotalPrice(double total) {
        String strtotal = currencyFormat.format(total);
        totalPriceText.setText(String.format("%sđ", strtotal));
    }

    public void closeCart(View view) {
        finish();
        ctrl.updateCart();
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Bạn có chắc chắn muốn xóa giỏ hàng không?")
                .setPositiveButton("Xóa", (dialog, id) -> {
                    // Xóa giỏ hàng bằng cách gọi controller
                    ctrl.clearCart();
                    dialog.dismiss();
                })
                .setNegativeButton("Hủy", (dialog, id) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
