package com.fastdine.utt.view;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fastdine.utt.R;
import com.fastdine.utt.controller.CustomerController;
import com.fastdine.utt.controller.OwnerController;
import com.fastdine.utt.model.Orders;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private RecyclerView recyclerView;
    private List<Orders> orderList;
    private OwnerController ownerController;
    private CustomerController customerController;

    public OrderAdapter(List<Orders> orderList, OwnerController ownerController, RecyclerView recyclerView ) {
        this.orderList = orderList;
        this.ownerController = ownerController;
        this.recyclerView = recyclerView;
    }

    public OrderAdapter(List<Orders> orderList, CustomerController customerController) {
        this.orderList = orderList;
        this.customerController= customerController;

    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);

        // Kiểm tra Activity là activity_customer hay activity_owner
        if (parent.getContext() instanceof CustomerActivity) {
            view.findViewById(R.id.customerActions).setVisibility(View.VISIBLE);
        } else if (parent.getContext() instanceof OwnerActivity) {
            view.findViewById(R.id.ownerActions).setVisibility(View.VISIBLE);
        }

        return new OrderViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Orders order = orderList.get(position);

        // Hiển thị thông tin đơn hàng
        holder.orderIdTextView.setText("Đơn hàng #" + order.getOrderId());
        holder.foodQuantityTextView.setText("Tổng số món: " + order.getTotalQuantity() + " món");
        holder.totalPriceTextView.setText("Tổng tiền: " + order.getTotalPrice() + "đ");

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm, dd/MM/yyyy", Locale.getDefault());
        String formattedTime = dateFormat.format(order.getOrderTime());
        holder.orderTimeTextView.setText("Thời gian đặt hàng: " + formattedTime);

        // Kiểm tra trạng thái của đơn hàng
        if ("Đã huỷ".equals(order.getStatus()) || "Đã giao".equals(order.getStatus())) {
            // Nếu trạng thái là "Đã huỷ", đặt màu xám và vô hiệu hoá các nút
            holder.cancelButton.setEnabled(false);
            holder.acceptOrderButton.setEnabled(false);

        } else {
            // Nếu trạng thái không phải "Đã huỷ"
            holder.cancelButton.setEnabled(true);
            holder.acceptOrderButton.setEnabled(true);
        }

        holder.acceptOrderButton.setText(order.getStatus());
        holder.orderStatusButton.setText(order.getStatus());

        // Sự kiện khi nhấn nút "Hủy"
        holder.cancelButton.setOnClickListener(v -> {
            new AlertDialog.Builder(holder.itemView.getContext())
                    .setTitle("Xác nhận hủy")
                    .setMessage("Bạn có chắc chắn muốn hủy đơn hàng này?")
                    .setPositiveButton("Có", (dialog, which) -> {
                        if (ownerController != null) {
                            ownerController.cancelOrder(order.getOrderId(), recyclerView);
                        }
                    })
                    .setNegativeButton("Không", null)
                    .show();
        });

        // Sự kiện khi nhấn nút "Thay đổi trạng thái"
        holder.acceptOrderButton.setOnClickListener(v -> {
            if (ownerController != null) {
                ownerController.updateStatus(order.getOrderId(), recyclerView);
            }
        });

        // Sự kiện khi nhấn vào item đơn hàng
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), OrderDetailActivity.class);
            intent.putExtra("orderId", order.getOrderId()); // Truyền orderId qua intent
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderIdTextView, foodQuantityTextView, totalPriceTextView, orderTimeTextView;
        Button cancelButton, acceptOrderButton, orderStatusButton;
        LinearLayout customerActions, ownerActions;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);

            // Ánh xạ các thành phần từ layout order_item.xml
            orderIdTextView = itemView.findViewById(R.id.orderIdTextView);
            foodQuantityTextView = itemView.findViewById(R.id.foodQuantityTextView);
            totalPriceTextView = itemView.findViewById(R.id.totalPriceTextView);
            orderTimeTextView = itemView.findViewById(R.id.orderTimeTextView);

            // Các nút dành cho khách hàng và chủ cửa hàng
            orderStatusButton = itemView.findViewById(R.id.orderStatusButton);
            cancelButton = itemView.findViewById(R.id.cancelButton);
            acceptOrderButton = itemView.findViewById(R.id.acceptOrderButton);

            // Layout chứa các nút tương tác cho khách hàng và chủ cửa hàng
            customerActions = itemView.findViewById(R.id.customerActions);
            ownerActions = itemView.findViewById(R.id.ownerActions);
        }
    }
}
