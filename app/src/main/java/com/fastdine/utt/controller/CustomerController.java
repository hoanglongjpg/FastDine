package com.fastdine.utt.controller;

import static com.fastdine.utt.model.Cart.getCartItems;
import static com.fastdine.utt.model.Cart.items;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
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
    // Hàm để hiển thị danh sách món ăn
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

    // Hàm gọi addItemToCart trong controller
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

    public void getInfo(Customer.OnCustomerInfoListener onCustomerInfoListener){

    }

    public void saveInfo(){
        Customer.saveCustomer(new Customer.OnCustomerListener() {
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
        Orders order = new Orders(name, address, phone, cartItems, date , "Pending");

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

    public void showOrderDialog(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_customer_info);

        // Lấy các view từ Dialog
        EditText editTextName = dialog.findViewById(R.id.editTextName);
        EditText editTextAddress = dialog.findViewById(R.id.editTextAddress);
        EditText editTextPhone = dialog.findViewById(R.id.editTextPhone);
        Button buttonCancel = dialog.findViewById(R.id.buttonCancel);
        Button buttonPlaceOrder = dialog.findViewById(R.id.buttonConfirm);

        // Xử lý sự kiện nút "Hủy"
        buttonCancel.setOnClickListener(v -> dialog.dismiss());

        // Xử lý sự kiện nút "Đặt hàng"
        buttonPlaceOrder.setOnClickListener(v -> {
            String name = editTextName.getText().toString().trim();
            String address = editTextAddress.getText().toString().trim();
            String phone = editTextPhone.getText().toString().trim();

            if (name.isEmpty() || address.isEmpty() || phone.isEmpty()) {
                Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            // Gọi hàm tạo đơn hàng
            createOrder(name, address, phone, dialog);
        });

        // Hiển thị Dialog
        dialog.show();
    }

    // Hàm để hiển thị danh sách đơn hàng
    public void viewOrderList(RecyclerView recyclerView) {
        Orders.getOrderList(new Orders.OnOrderListListener() {
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
