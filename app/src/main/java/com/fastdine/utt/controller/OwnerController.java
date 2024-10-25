package com.fastdine.utt.controller;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.fastdine.utt.R;
import com.fastdine.utt.model.Food;
import com.fastdine.utt.view.FoodAdapter;
import java.util.List;

public class OwnerController {
    private Context context;

    public OwnerController(Context context) {
        this.context = context.getApplicationContext();
    }

    // Interface để callback về Activity
    public interface AddFoodListener {
        void onFoodAdded();
    }

    // Hàm để hiển thị danh sách món ăn
    public void viewFoodList(RecyclerView recyclerView) {
        // Gọi hàm getFoodList từ Food.java
        Food.getFoodList(new Food.OnFoodListListener() {
            @Override
            public void onComplete(List<Food> foodList) {
                // Tạo adapter với dữ liệu món ăn
                FoodAdapter foodAdapter = new FoodAdapter(foodList);

                // Cài đặt LayoutManager cho RecyclerView
                recyclerView.setLayoutManager(new LinearLayoutManager(context));

                // Cài đặt adapter cho RecyclerView
                recyclerView.setAdapter(foodAdapter);

                // Thông báo thành công (tuỳ chọn)
                Toast.makeText(context, "Danh sách món ăn đã được cập nhật", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Exception e) {
                // Xử lý khi có lỗi xảy ra
                Toast.makeText(context, "Lỗi khi lấy danh sách món ăn: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Hàm để hiển thị Dialog thêm món ăn
    public void addFood(RecyclerView recyclerView, Context context) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_add_food);

        // Lấy các view từ Dialog
        EditText editTextName = dialog.findViewById(R.id.editText_food_name);
        EditText editTextDescription = dialog.findViewById(R.id.editText_food_description);
        EditText editTextPrice = dialog.findViewById(R.id.editText_food_price);
        EditText editTextImage = dialog.findViewById(R.id.editText_food_image);
        Button buttonCancel = dialog.findViewById(R.id.button_cancel);
        Button buttonAdd = dialog.findViewById(R.id.button_add);

        // Xử lý sự kiện nút "Hủy"
        buttonCancel.setOnClickListener(v -> dialog.dismiss());

        // Xử lý sự kiện nút "Thêm"
        buttonAdd.setOnClickListener(v -> {
            String name = editTextName.getText().toString().trim();
            String description = editTextDescription.getText().toString().trim();
            String priceStr = editTextPrice.getText().toString().trim();
            String imageUrl = editTextImage.getText().toString().trim();

            if (name.isEmpty() || description.isEmpty() || priceStr.isEmpty() || imageUrl.isEmpty()) {
                Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            double price = Double.parseDouble(priceStr);

            // Tạo đối tượng món ăn mới
            Food food = new Food(name, description, imageUrl, price);

            // Gọi hàm addFood từ Food.java
            Food.addFood(food, new Food.OnFoodListListener() {
                @Override
                public void onComplete(List<Food> foodList) {

                    // Thông báo thành công (tuỳ chọn)
                    Toast.makeText(context, "Thêm món ăn thành công", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(Exception e) {
                    // Xử lý khi có lỗi xảy ra
                    Toast.makeText(context, "Thêm món ăn thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            dialog.dismiss();
            viewFoodList(recyclerView);
        });
        // Hiển thị Dialog
        dialog.show();
    }

    //Sửa món ăn
    public void updateFood(){

    }

    //Xoá món ăn
    public void deleteFood(){

    }

}
