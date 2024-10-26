package com.fastdine.utt.view;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fastdine.utt.R;
import com.fastdine.utt.model.Cart;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<Cart.CartItems> cartItemList;

    public CartAdapter(List<Cart.CartItems> cartItemList) {
        this.cartItemList = cartItemList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Cart.CartItems cartItem = cartItemList.get(position);

        holder.itemName.setText(cartItem.getName());
        holder.itemPrice.setText(cartItem.getPrice() + "đ");
        holder.itemQuantity.setText(String.valueOf(cartItem.getQuantity()));

        // Assuming the image is stored as a URL; you may need a library like Glide or Picasso to load images from URLs.
        Glide.with(holder.itemView.getContext())
                .load(cartItem.getImage()) // Load image from URL or resource ID
                .into(holder.itemImage);

        // Xử lý tăng giảm số lượng
        holder.increaseQuantityButton.setOnClickListener(v -> {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            notifyItemChanged(position);
        });

        holder.decreaseQuantityButton.setOnClickListener(v -> {
            if (cartItem.getQuantity() > 1) {
                cartItem.setQuantity(cartItem.getQuantity() - 1);
                notifyItemChanged(position);
            }
        });


    }


    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {

        TextView itemName, itemPrice, itemQuantity;
        ImageView itemImage;
        ImageButton increaseQuantityButton, decreaseQuantityButton;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            itemQuantity = itemView.findViewById(R.id.itemQuantity);
            itemImage = itemView.findViewById(R.id.itemImage);
            increaseQuantityButton = itemView.findViewById(R.id.increaseQuantityButton);
            decreaseQuantityButton = itemView.findViewById(R.id.decreaseQuantityButton);
        }
    }
}

