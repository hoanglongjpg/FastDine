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
import com.fastdine.utt.model.Cart;

import java.util.List;

public class CartActivity extends AppCompatActivity {
    private CustomerController ctrl;
    public RecyclerView cartRecyclerView;
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
        // Set click listener for the clear cart button
        findViewById(R.id.clearCartButton).setOnClickListener(v -> showDeleteConfirmationDialog());

    }

    public void updateTotalPrice(double total) {
            totalPriceText.setText(String.format("%.2fđ", total)); // Hiển thị giá tổng
    }

    public void closeCart(View view) {
        finish();
        ctrl.updateCart();
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.deletecart_cf, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        Button cancelButton = dialogView.findViewById(R.id.cancelButton);
        Button confirmButton = dialogView.findViewById(R.id.confirmDeleteButton);

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        confirmButton.setOnClickListener(v -> {
            ctrl.clearCart();  // Call controller to clear the cart
            dialog.dismiss();
        });

        dialog.show();
    }

}
