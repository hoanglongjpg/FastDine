package com.fastdine.utt;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class OwnerController {
    private static Context context;
    private static AddFoodListener addFoodListener;

    public OwnerController(Context context, AddFoodListener listener) {
        this.context = context;
        this.addFoodListener = listener;
    }

    public OwnerController(Context context) {
        this.context = context;
    }

    // Interface để callback về Activity
    public interface AddFoodListener {
        void onFoodAdded();
    }

    // Hàm để hiển thị Dialog thêm món ăn
    public static void showAddFoodDialog() {
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
            Map<String, Object> food = new HashMap<>();
            food.put("name", name);
            food.put("description", description);
            food.put("price", price);
            food.put("image", imageUrl);

            // Thêm món ăn vào Firestore
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("foods")
                    .add(food)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(context, "Thêm món ăn thành công", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();  // Đóng Dialog sau khi thêm thành công

                        if (addFoodListener != null) {
                            addFoodListener.onFoodAdded();
                        }

                    })

                    .addOnFailureListener(e -> Toast.makeText(context, "Lỗi khi thêm món ăn", Toast.LENGTH_SHORT).show());


        });

        // Hiển thị Dialog
        dialog.show();
    }
}
