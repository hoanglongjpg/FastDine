package com.fastdine.utt.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart {
    private String cart_id;
    private List<Cart.CartItems> items;

    public static class CartItems extends Food{
        private int quantity;

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public CartItems(String id, String name, String description, String image, double price, int quantity) {
        super(id, name, description, image, price);
        this.quantity = quantity;
        }
    }

    public Cart(String cart_id, List<Cart.CartItems> items) {
        this.cart_id = cart_id;
        this.items = items;
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public List<CartItems> getItems() {
        return items;
    }

    public void setItems(List<CartItems> items) {
        this.items = items;
    }

    public double calTotalPrice(List<Cart.CartItems> items) {
        double total = 0;
        for (CartItems item : items) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }

    public interface OnCartListListener {
        void onComplete(List<CartItems> items);

        void onError(Exception e);
    }

    //Lấy danh sách món ăn trong giỏ hàng từ Firebase
    public static void getCartItems(final OnCartListListener listener) {
        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail(); // Lấy email người dùng hiện tại
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference cartRef = db.collection("cart").document(userEmail);

        cartRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                // Kiểm tra xem tài liệu đã tồn tại hay chưa
                if (task.getResult().exists()) {
                    List<CartItems> items = new ArrayList<>();
                    // Lấy Map items từ tài liệu
                    Map<String, List<Object>> itemsData = (Map<String, List<Object>>) task.getResult().get("items");

                    // Kiểm tra xem itemsData có tồn tại hay không
                    if (itemsData != null) {
                        // Lặp qua từng món ăn trong Map
                        for (Map.Entry<String, List<Object>> entry : itemsData.entrySet()) {
                            List<Object> itemData = entry.getValue(); // Lấy List của từng món ăn

                            // Giả sử mỗi món ăn có cấu trúc như sau:
                            String id = (String) itemData.get(0); // id món ăn
                            String name = (String) itemData.get(1); // tên món ăn
                            String description = (String) itemData.get(2); // mô tả
                            String image = (String) itemData.get(3); // ảnh
                            double price = ((Number) itemData.get(4)).doubleValue(); // giá
                            int quantity = ((Long) itemData.get(5)).intValue(); // số lượng

                            // Thêm món ăn vào danh sách
                            items.add(new CartItems(id, name, description, image, price, quantity));
                        }
                    }
                    listener.onComplete(items); // Trả về danh sách món trong giỏ hàng
                } else {
                    // Nếu tài liệu không tồn tại, tạo một tài liệu mới với email người dùng
                    Map<String, Object> newCartData = new HashMap<>();
                    newCartData.put("cart_id", userEmail); // Đặt cart_id là email người dùng
                    Map<String, List<Object>> itemsMap = new HashMap<>(); // Tạo một danh sách rỗng cho items
                    newCartData.put("items", itemsMap);

                    cartRef.set(newCartData) // Lưu Cart mới vào Firestore
                            .addOnSuccessListener(aVoid -> {
                                listener.onComplete(new ArrayList<>()); // Trả về danh sách rỗng
                            })
                            .addOnFailureListener(e -> {
                                listener.onError(e); // Xử lý lỗi nếu có
                            });
                }
            } else {
                listener.onError(task.getException()); // Trả về lỗi nếu có
            }
        });
    }

    public static void clearCart(OnCartClearListener listener) {
        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference cartRef = db.collection("cart").document(userEmail);

        cartRef.update("items", new HashMap<>())  // Clear all items by setting to an empty map
                .addOnSuccessListener(aVoid -> listener.onSuccess())
                .addOnFailureListener(listener::onError);
    }

    public interface OnCartClearListener {
        void onSuccess();
        void onError(Exception e);
    }

}

