    package com.fastdine.utt.view;

    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ImageButton;
    import android.widget.ImageView;
    import android.widget.LinearLayout;
    import android.widget.TextView;
    import androidx.annotation.NonNull;
    import androidx.recyclerview.widget.RecyclerView;
    import com.bumptech.glide.Glide;
    import com.fastdine.utt.R;
    import com.fastdine.utt.controller.OwnerController;
    import com.fastdine.utt.controller.CustomerController;
    import com.fastdine.utt.model.Cart;
    import com.fastdine.utt.model.Food;

    import java.text.DecimalFormat;
    import java.util.List;

    public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
        private List<Food> foodList;
        private OwnerController ownerController;
        private CustomerController customerController;

        DecimalFormat currencyFormat = new DecimalFormat("#,###");

        public FoodAdapter(List<Food> foodList, CustomerController customerController) {
            this.foodList = foodList;
            this.customerController = customerController;
        }

        public FoodAdapter(List<Food> foodList) {
            this.foodList = foodList;
        }

        public FoodAdapter(List<Food> foodList, OwnerController ownerController) {
            this.foodList = foodList;
            this.ownerController = ownerController;
        }

        @NonNull
        @Override
        public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item, parent, false);

            // Kiểm tra Activity là activity_customer hay activity_owner
            if (parent.getContext() instanceof CustomerActivity) {
                view.findViewById(R.id.customerActions).setVisibility(View.VISIBLE);
            } else if (parent.getContext() instanceof OwnerActivity) {
                view.findViewById(R.id.ownerActions).setVisibility(View.VISIBLE);
            }
            return new FoodViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
            Food foodItem = foodList.get(position);
            String price = currencyFormat.format(foodItem.getPrice());
            holder.nameTextView.setText(foodItem.getName());
            holder.descriptionTextView.setText(foodItem.getDescription());
            holder.priceTextView.setText(String.format("%sđ", price));

            // Sử dụng Glide để tải ảnh món ăn
            Glide.with(holder.itemView.getContext())
                    .load(foodItem.getImage())
                    .into(holder.foodImageView);

            // Thiết lập nút xóa
            holder.buttonDelete.setOnClickListener(v -> {
                // Gọi phương thức deleteFood trong OwnerController
                ownerController.deleteFood(foodItem.getId(), ((RecyclerView) holder.itemView.getParent()));
            });

            // Thiết lập nút sửa
            holder.editButton.setOnClickListener(v -> {
                // Gọi phương thức editFood trong OwnerController
                ownerController.editFood(foodItem, ((RecyclerView) holder.itemView.getParent()), holder.itemView.getContext());
            });

            // Thiết lập sự kiện click cho nút "+"
            holder.addButton.setOnClickListener(v -> {
                // Tạo CartItem từ Food để thêm vào giỏ hàng
                Cart.CartItems newItem = new Cart.CartItems(foodItem.getId(), foodItem.getName(), foodItem.getDescription(),
                        foodItem.getImage(), foodItem.getPrice(), 1);
                customerController.addItemToCart(newItem); // Thêm vào giỏ hàng thông qua controller
            });
        }

        @Override
        public int getItemCount() {
            return foodList.size();
        }

        public static class FoodViewHolder extends RecyclerView.ViewHolder {

            public View buttonDelete, editButton;
            ImageButton addButton;
            TextView nameTextView, descriptionTextView, priceTextView;
            ImageView foodImageView;

            public FoodViewHolder(@NonNull View itemView) {
                super(itemView);
                LinearLayout customerActions = itemView.findViewById(R.id.customerActions);
                editButton = itemView.findViewById(R.id.editButton);
                addButton = customerActions.findViewById(R.id.addButton);
                nameTextView = itemView.findViewById(R.id.nameTextView);
                descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
                priceTextView = itemView.findViewById(R.id.priceTextView);
                foodImageView = itemView.findViewById(R.id.foodImageView);
                buttonDelete = itemView.findViewById(R.id.deleteButton);
            }
        }
    }
