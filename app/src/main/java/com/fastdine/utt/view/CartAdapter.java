package com.fastdine.utt.view;
import android.content.Context;
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
    private Context context;
    private List<Cart.CartItems> cartItemList;

    public CartAdapter(List<Cart.CartItems> cartItemList) {
        this.cartItemList = cartItemList;
    }

    public CartAdapter(Context context, List<Cart.CartItems> cartItemList) {
        this.context = context;
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

        Glide.with(holder.itemView.getContext())
                .load(cartItem.getImage())
                .into(holder.itemImage);

        // Xử lý tăng giảm số lượng
        holder.increaseQuantityButton.setOnClickListener(v -> {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            notifyItemChanged(position);
            double total = new Cart("user_email", cartItemList).calTotalPrice(cartItemList);
            ((CartActivity) context).updateTotalPrice(total);
        });

        holder.decreaseQuantityButton.setOnClickListener(v -> {
            int newQuantity = cartItem.getQuantity() - 1;
            if (newQuantity > 0) {
                cartItem.setQuantity(newQuantity);  // Giảm số lượng trong danh sách static
                notifyItemChanged(position);
                double total = new Cart("user_email", cartItemList).calTotalPrice(cartItemList);
                ((CartActivity) context).updateTotalPrice(total);
            } else {
                // Xóa món ăn nếu số lượng bằng 0
                cartItemList.remove(position);
                Cart.items.remove(cartItem);  // Xóa khỏi danh sách static của Cart
                notifyItemRemoved(position);  // Cập nhật giao diện sau khi xóa
                Cart.items = cartItemList;
                double total = new Cart("user_email", cartItemList).calTotalPrice(cartItemList);
                ((CartActivity) context).updateTotalPrice(total);
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

