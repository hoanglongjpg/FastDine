package com.fastdine.utt.view;

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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fastdine.utt.R;
import com.fastdine.utt.controller.CustomerController;
import com.fastdine.utt.controller.OwnerController;
import com.fastdine.utt.model.Orders;

import java.text.SimpleDateFormat;
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

        // Hiển thị mã đơn hàng
        holder.orderIdTextView.setText("Đơn hàng #" + order.getOrderId());

        // Hiển thị tổng số món
        holder.foodQuantityTextView.setText("Tổng số món: " + order.getTotalQuantity() + " món");

        // Hiển thị tổng tiền
        holder.totalPriceTextView.setText("Tổng tiền: " + order.getTotalPrice() + "đ");

        // Hiển thị thời gian đặt hàng
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm, dd/MM/yyyy", Locale.getDefault());
        String formattedTime = dateFormat.format(order.getOrderTime());
        holder.orderTimeTextView.setText("Thời gian đặt hàng: " + formattedTime);
        holder.acceptOrderButton.setText(order.getStatus());
        holder.orderStatusButton.setText(order.getStatus());

        // Thêm listener cho nút acceptOrderButton
        holder.acceptOrderButton.setOnClickListener(v -> {
            // Gọi phương thức updateStatus từ OwnerController
            if (ownerController != null) {
                ownerController.updateStatus(order.getOrderId(), recyclerView);
            }
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
