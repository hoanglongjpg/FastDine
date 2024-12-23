package com.fastdine.utt.controller;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.fastdine.utt.R;
import com.fastdine.utt.model.Food;
import com.fastdine.utt.model.Orders;
import com.fastdine.utt.view.FoodAdapter;
import com.fastdine.utt.view.OrderAdapter;

import java.security.acl.Owner;
import java.util.List;

public class OwnerController {
    private Context context;

    public OwnerController(Context context) {
        this.context = context;
    }

    public OwnerController() {
        this.context = context.getApplicationContext();
    }

    public void viewFoodList(RecyclerView recyclerView) {
        // Gọi hàm getFoodList từ Food.java
        Food.getFoodList(new Food.OnFoodListListener() {
            @Override
            public void onComplete(List<Food> foodList) {
                // Tạo adapter với dữ liệu món ăn
                FoodAdapter foodAdapter = new FoodAdapter(foodList, OwnerController.this );

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

    public void editFood(Food foodItem, RecyclerView recyclerView, Context context) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_update_food);

        // Lấy các view từ dialog và điền sẵn thông tin món ăn
        EditText editTextName = dialog.findViewById(R.id.editText_food_name);
        EditText editTextDescription = dialog.findViewById(R.id.editText_food_description);
        EditText editTextPrice = dialog.findViewById(R.id.editText_food_price);
        EditText editTextImage = dialog.findViewById(R.id.editText_food_image);
        Button buttonCancel = dialog.findViewById(R.id.button_cancel);
        Button buttonSave = dialog.findViewById(R.id.button_save);

        editTextName.setText(foodItem.getName());
        editTextDescription.setText(foodItem.getDescription());
        editTextPrice.setText(String.valueOf(foodItem.getPrice()));
        editTextImage.setText(foodItem.getImage());

        // Sự kiện hủy
        buttonCancel.setOnClickListener(v -> dialog.dismiss());

        // Sự kiện lưu
        buttonSave.setOnClickListener(v -> {
            foodItem.setName(editTextName.getText().toString().trim());
            foodItem.setDescription(editTextDescription.getText().toString().trim());
            foodItem.setPrice(Double.parseDouble(editTextPrice.getText().toString().trim()));
            foodItem.setImage(editTextImage.getText().toString().trim());

            // Gọi hàm updateFood từ Food.java
            Food.updateFood(foodItem, new Food.OnFoodListListener() {
                @Override
                public void onComplete(List<Food> foodList) {
                    Toast.makeText(context, "Cập nhật món ăn thành công", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(Exception e) {
                    Toast.makeText(context, "Cập nhật món ăn thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            dialog.dismiss();
            viewFoodList(recyclerView);
        });
        dialog.show();
    }

    public void deleteFood(String foodId, RecyclerView recyclerView) {
        // Tạo dialog xác nhận xóa
        new AlertDialog.Builder(context)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa món ăn này?")
                .setPositiveButton("Có", (dialog, which) -> {
                    // Gọi hàm deleteFood từ Food.java
                    Food.deleteFood(foodId, new Food.OnFoodListListener() {
                        @Override
                        public void onComplete(List<Food> foodList) {
                            // Thông báo thành công
                            Toast.makeText(context, "Xóa món ăn thành công", Toast.LENGTH_SHORT).show();
                            // Cập nhật lại danh sách món ăn
                            viewFoodList(recyclerView);
                        }

                        @Override
                        public void onError(Exception e) {
                            // Xử lý lỗi
                            Toast.makeText(context, "Xóa món ăn thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Không", null) // Nút hủy
                .show();
    }

    public void viewOrderList(RecyclerView recyclerView) {
        Orders.getOrderList(new Orders.OnOrderListListener() {
            @Override
            public void onOrderListReceived(List<Orders> ordersList) {
                // Tạo adapter với dữ liệu đơn hàng
                OrderAdapter orderAdapter = new OrderAdapter(ordersList, OwnerController.this, recyclerView);

                // Cài đặt LayoutManager cho RecyclerView
                recyclerView.setLayoutManager(new LinearLayoutManager(context));

                // Cài đặt adapter cho RecyclerView
                recyclerView.setAdapter(orderAdapter);

                // Thông báo thành công
                Toast.makeText(context, "Danh sách đơn hàng đã được cập nhật", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Exception e) {
                // Xử lý khi có lỗi xảy ra
                Toast.makeText(context, "Lỗi khi lấy danh sách đơn hàng: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateStatus(String orderId, RecyclerView recyclerView) {
        // Tạo một mảng chứa các trạng thái
        String[] statuses = {"Đang chuẩn bị", "Đã giao"};

        // Tạo AlertDialog Builder cho việc chọn trạng thái
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Chọn trạng thái đơn hàng")
                .setItems(statuses, (dialog, which) -> {
                    // Khi người dùng chọn một trạng thái
                    String selectedStatus = statuses[which];

                    // Kiểm tra nếu trạng thái được chọn là "Đã giao"
                    if ("Đã giao".equals(selectedStatus)) {
                        // Hiển thị thêm dialog xác nhận
                        new AlertDialog.Builder(context)
                                .setTitle("Xác nhận đã giao")
                                .setMessage("Bạn có chắc chắn muốn cập nhật trạng thái đơn hàng này là 'Đã giao'?")
                                .setPositiveButton("Có", (confirmDialog, confirmWhich) -> {
                                    // Gọi hàm updateStatus trong model khi người dùng xác nhận
                                    Orders.updateStatus(orderId, selectedStatus, new Orders.OnOrderListener() {
                                        @Override
                                        public void onComplete(String orderId) {
                                            Toast.makeText(context, "Trạng thái đơn hàng đã được cập nhật: " + selectedStatus, Toast.LENGTH_SHORT).show();
                                            viewOrderList(recyclerView); // Cập nhật danh sách đơn hàng
                                        }

                                        @Override
                                        public void onError(Exception e) {
                                            Toast.makeText(context, "Cập nhật thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                })
                                .setNegativeButton("Không", (confirmDialog, confirmWhich) -> confirmDialog.dismiss())
                                .show();
                    } else {
                        // Gọi hàm updateStatus trong model nếu không cần xác nhận thêm
                        Orders.updateStatus(orderId, selectedStatus, new Orders.OnOrderListener() {
                            @Override
                            public void onComplete(String orderId) {
                                Toast.makeText(context, "Trạng thái đơn hàng đã được cập nhật: " + selectedStatus, Toast.LENGTH_SHORT).show();
                                viewOrderList(recyclerView); // Cập nhật danh sách đơn hàng
                            }

                            @Override
                            public void onError(Exception e) {
                                Toast.makeText(context, "Cập nhật thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss())
                .show();
    }

    public void cancelOrder(String orderId, RecyclerView recyclerView) {
        // Gọi phương thức updateStatus để cập nhật trạng thái đơn hàng thành "Đã huỷ"
        Orders.updateStatus(orderId, "Đã huỷ", new Orders.OnOrderListener() {
            @Override
            public void onComplete(String orderId) {
                Toast.makeText(context, "Đơn hàng đã được huỷ", Toast.LENGTH_SHORT).show();
                viewOrderList(recyclerView); // Cập nhật danh sách đơn hàng
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(context, "Lỗi khi huỷ đơn hàng: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void viewOrderDetail(String orderId, Orders.OnDetailListener listener) {
        // Gọi phương thức trong Orders để truy vấn chi tiết đơn hàng từ Firebase
        Orders.getOrderDetail(orderId, new Orders.OnDetailListener() {
            @Override
            public void onComplete(Orders order) {
                // Truyền kết quả qua listener
                listener.onComplete(order);
            }

            @Override
            public void onError(Exception e) {
                // Truyền lỗi qua listener
                listener.onError(e);
            }
        });
    }
}
