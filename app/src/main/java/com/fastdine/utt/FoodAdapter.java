package com.fastdine.utt;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    private List<Food> foodList;

    public FoodAdapter(List<Food> foodList) {
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Food foodItem = foodList.get(position);
        holder.nameTextView.setText(foodItem.getName());
        holder.descriptionTextView.setText(foodItem.getDescription());
        holder.priceTextView.setText(String.valueOf(foodItem.getPrice()) + "đ");

        // Sử dụng Glide để tải ảnh món ăn
        Glide.with(holder.itemView.getContext())
                .load(foodItem.getImage())
                .into(holder.foodImageView);
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, descriptionTextView, priceTextView;
        ImageView foodImageView;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            foodImageView = itemView.findViewById(R.id.foodImageView);
        }
    }
}
