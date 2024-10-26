package com.fastdine.utt.controller;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.widget.Toast;
import com.fastdine.utt.model.Cart;
import com.fastdine.utt.model.Food;
import com.fastdine.utt.view.CartActivity;
import com.fastdine.utt.view.CartAdapter;
import com.fastdine.utt.view.FoodAdapter;
import java.util.List;

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

    public void getCart(){
        // Gọi hàm getFoodList từ Food.java
        Cart.getCartItems(new Cart.OnCartListListener() {
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
    }

    public void viewCart(RecyclerView recyclerView){
        // Gọi hàm getFoodList từ Food.java
        Cart.getCartItems(new Cart.OnCartListListener() {
            @Override
            public void onComplete(List<Cart.CartItems> items) {
                // Tạo adapter với dữ liệu món ăn
                CartAdapter cartAdapter = new CartAdapter(items);

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

    // Hàm gọi addItemToCart trong controller
    public void addItemToCart(Cart.CartItems newItem) {
        // Thêm món ăn vào giỏ hàng và cập nhật Firestore
        Cart.addItemToCart(newItem);


        // Gọi lại hàm getCartItems để cập nhật lại RecyclerView và tổng giá
        Cart.getCartItems(new Cart.OnCartListListener() {
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

}

