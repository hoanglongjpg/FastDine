package com.fastdine.utt.controller;

import static com.fastdine.utt.model.Cart.getCartItems;
import static com.fastdine.utt.model.Cart.items;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fastdine.utt.R;
import com.fastdine.utt.model.Cart;
import com.fastdine.utt.model.Customer;
import com.fastdine.utt.model.Food;
import com.fastdine.utt.model.Orders;
import com.fastdine.utt.view.CartActivity;
import com.fastdine.utt.view.CartAdapter;
import com.fastdine.utt.view.FoodAdapter;
import com.fastdine.utt.view.OrderAdapter;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class CustomerController {
    private Context context;
    public CustomerController(Context context) {
        this.context = context;
    }

    public CustomerController(){
        this.context = context.getApplicationContext();
    }

    public void viewAvailableFood(RecyclerView recyclerView) {
        // Gọi hàm getFoodList từ Food.java
        Food.getFoodList(new Food.OnFoodListListener() {
            @Override
            public void onComplete(List<Food> foodList) {
                // Tạo adapter với dữ liệu món ăn
                FoodAdapter foodAdapter = new FoodAdapter(foodList,CustomerController.this);

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

    public List<Cart.CartItems> getCart(){
        // Gọi hàm getFoodList từ Food.java
        getCartItems(new Cart.OnCartListListener() {
            @Override
            public void onComplete(List<Cart.CartItems> items) {
                // Thông báo thành công (tuỳ chọn)
                Toast.makeText(context, "Giỏ hàng đã được cập nhật", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Exception e) {
                // Xử lý khi có lỗi xảy ra
                Toast.makeText(context, "Lỗi khi tải giỏ hàng " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
        return items;
    }

    public void viewCart(RecyclerView recyclerView){
        // Gọi hàm getFoodList từ Food.java
        getCartItems(new Cart.OnCartListListener() {
            @Override
            public void onComplete(List<Cart.CartItems> items) {
                // Tạo adapter với dữ liệu món ăn
                CartAdapter cartAdapter = new CartAdapter(context,items);

                // Cài đặt LayoutManager cho RecyclerView
                recyclerView.setLayoutManager(new LinearLayoutManager(context));

                // Cài đặt adapter cho RecyclerView
                recyclerView.setAdapter(cartAdapter);

                Cart cart = new Cart("user_email", items);
                double total = cart.calTotalPrice(items);
                ((CartActivity) context).updateTotalPrice(total);

                // Thông báo thành công (tuỳ chọn)
                Toast.makeText(context, "Giỏ hàng đã được cập nhật", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Exception e) {
                // Xử lý khi có lỗi xảy ra
                Toast.makeText(context, "Lỗi khi tải giỏ hàng " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void clearCart() {
        Cart.clearCart(new Cart.OnCartClearListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(context, "Đã xóa tất cả các món trong giỏ hàng", Toast.LENGTH_SHORT).show();
                viewCart(((CartActivity) context).cartRecyclerView);  // Refresh cart view
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(context, "Lỗi khi xóa giỏ hàng: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateCart() {
        Cart.updateCart(new Cart.OnCartListListener() {
            @Override
            public void onComplete(List<Cart.CartItems> items) {

                // Thông báo thành công (tuỳ chọn)
                Toast.makeText(context, "Giỏ hàng đã được cập nhật", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Exception e) {
                // Xử lý khi có lỗi xảy ra
                Toast.makeText(context, "Lỗi khi cập nhật giỏ hàng " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addItemToCart(Cart.CartItems newItem) {
        // Thêm món ăn vào giỏ hàng và cập nhật Firestore
        Cart.addItemToCart(newItem);


        // Gọi lại hàm getCartItems để cập nhật lại RecyclerView và tổng giá
        getCartItems(new Cart.OnCartListListener() {
            @Override
            public void onComplete(List<Cart.CartItems> items) {
                Toast.makeText(context, "Món ăn đã được thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Exception e) {
                // Xử lý khi có lỗi xảy ra
                Toast.makeText(context, "Lỗi khi thêm món ăn vào giỏ hàng " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void viewCustomerInfo(TextView nameView, TextView phoneView, TextView emailView, TextView addressView) {
        Customer.getCustomerInfo(new Customer.OnCustomerInfoListener() {
            @Override
            public void onComplete(Customer customer) {
                // Kiểm tra và hiển thị thông tin lên các TextView
                if (nameView != null) {
                    nameView.setText(customer.getName());
                }
                if (phoneView != null) {
                    phoneView.setText(customer.getPhone());
                }
                if (emailView != null) {
                    emailView.setText(customer.getEmail());
                }
                if (addressView != null) {
                    addressView.setText(customer.getAddress());
                }
            }

            @Override
            public void onError(Exception e) {
                // Xử lý lỗi nếu có
                Log.e("CustomerController", "Lỗi khi lấy thông tin khách hàng: " + e.getMessage());
            }
        });
    }

    public void saveInfoOnRegister(String email) {
        String newName = ""; // Để trống
        String newAddress = ""; // Để trống
        String newPhone = ""; // Để trống

        Customer.saveCustomer(newName, newAddress, newPhone, new Customer.OnCustomerListener() {
            @Override
            public void onComplete() {
                // Thông báo thành công (tuỳ chọn)
                Toast.makeText(context, "Đăng ký thành công. Thông tin đã được lưu.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Exception e) {
                // Xử lý khi có lỗi xảy ra
                Toast.makeText(context, "Lỗi khi lưu thông tin khách hàng: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void saveInfo(String newName, String newAddress, String newPhone){

        Customer.saveCustomer(newName, newAddress, newPhone, new Customer.OnCustomerListener() {
            @Override
            public void onComplete() {
                // Thông báo thành công (tuỳ chọn)
                Toast.makeText(context, "Cập nhật thông tin khách hàng thành công", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(Exception e) {
                // Xử lý khi có lỗi xảy ra
                Toast.makeText(context, "Lỗi khi cập nhật thông tin cá nhân " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createOrder(String name, String address, String phone, Dialog dialog) {
        // Giả sử bạn đã có phương thức để lấy danh sách món ăn trong giỏ hàng
        List<Cart.CartItems> cartItems = this.getCart(); // Lấy danh sách món ăn từ giỏ hàng
        if (cartItems.isEmpty()) {
            Toast.makeText(dialog.getContext(), "Giỏ hàng trống", Toast.LENGTH_SHORT).show();
            return;
        }
        Date date = new Date(System.currentTimeMillis());
        // Tạo đơn hàng
        Orders order = new Orders(name, address, phone, cartItems, date , "Đã nhận");

        // Lưu đơn hàng vào Firestore
        Orders.addOrder(order, new Orders.OnOrderListener() {
            @Override
            public void onComplete(String orderId) {
                Toast.makeText(dialog.getContext(), "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                clearCart(); // Xóa giỏ hàng sau khi đặt hàng thành công
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(dialog.getContext(), "Đặt hàng thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void confirmAndSaveInfoIfChanged(String originalName, String originalAddress, String originalPhone, String newName, String newAddress, String newPhone) {
        boolean infoChanged = !originalName.equals(newName) || !originalAddress.equals(newAddress) || !originalPhone.equals(newPhone);

        if (infoChanged) {
            // Lưu thông tin nếu có sự thay đổi
            saveInfo(newName, newAddress, newPhone);
        }
    }

    public void showOrderDialog(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_customer_info);

        // Lấy các view từ Dialog
        EditText editTextName = dialog.findViewById(R.id.editTextName);
        EditText editTextAddress = dialog.findViewById(R.id.editTextAddress);
        EditText editTextPhone = dialog.findViewById(R.id.editTextPhone);
        EditText editTextEmail = dialog.findViewById(R.id.editTextEmail);
        editTextEmail.setEnabled(false);
        Button buttonCancel = dialog.findViewById(R.id.buttonCancel);
        Button buttonPlaceOrder = dialog.findViewById(R.id.buttonConfirm);

        // Xử lý sự kiện nút "Hủy"
        buttonCancel.setOnClickListener(v -> dialog.dismiss());

        Customer.getCustomerInfo(new Customer.OnCustomerInfoListener() {
            @Override
            public void onComplete(Customer customer) {
                // Lưu thông tin khách hàng ban đầu
                final String originalName = customer.getName();
                final String originalAddress = customer.getAddress();
                final String originalPhone = customer.getPhone();

                // Set text cho các trường thông tin
                editTextName.setText(originalName);
                editTextAddress.setText(originalAddress);
                editTextPhone.setText(originalPhone);
                editTextEmail.setText(customer.getEmail());

                // Xử lý sự kiện nút "Đặt hàng"
                buttonPlaceOrder.setOnClickListener(v -> {
                    String newName = editTextName.getText().toString().trim();
                    String newAddress = editTextAddress.getText().toString().trim();
                    String newPhone = editTextPhone.getText().toString().trim();

                    if (newName.isEmpty() || newAddress.isEmpty() || newPhone.isEmpty()) {
                        Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Kiểm tra thay đổi và lưu nếu cần
                    confirmAndSaveInfoIfChanged(originalName, originalAddress, originalPhone, newName, newAddress, newPhone);

                    // Tạo đơn hàng sau khi xác nhận
                    createOrder(newName, newAddress, newPhone, dialog);
                });
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(context, "Không thể tải thông tin khách hàng", Toast.LENGTH_SHORT).show();
            }
        });

        // Hiển thị Dialog
        dialog.show();
    }

    public void viewOrderList(RecyclerView recyclerView) {
        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail(); // Lấy email của người dùng hiện tại
        Orders.getOrderListCustomer(userEmail, new Orders.OnOrderListListener() {
            @Override
            public void onOrderListReceived(List<Orders> ordersList) {
                // Tạo adapter với dữ liệu đơn hàng
                OrderAdapter orderAdapter = new OrderAdapter(ordersList, CustomerController.this);

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


}
