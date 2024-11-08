package com.fastdine.utt.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fastdine.utt.R;
import com.fastdine.utt.model.Cart;

import java.text.DecimalFormat;
import java.util.List;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder> {
    private List<Cart.CartItems> items;

    DecimalFormat currencyFormat = new DecimalFormat("#,###");

    public OrderItemAdapter(List<Cart.CartItems> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_detail_item, parent, false);
        return new OrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        Cart.CartItems item = items.get(position);
        String price = currencyFormat.format(item.getPrice());
        holder.foodName.setText(item.getName());
        holder.foodQuantity.setText("Số lượng: " + item.getQuantity());
        holder.foodPrice.setText("Giá: " + String.format("%sđ", price));


        Glide.with(holder.itemView.getContext())
                .load(item.getImage())
                .into(holder.itemImage);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class OrderItemViewHolder extends RecyclerView.ViewHolder {
        TextView foodName, foodQuantity, foodPrice;
        ImageView itemImage;
        public OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.itemImage);
            foodName = itemView.findViewById(R.id.itemName);
            foodQuantity = itemView.findViewById(R.id.itemQuantity);
            foodPrice = itemView.findViewById(R.id.itemPrice);
        }
    }
}

