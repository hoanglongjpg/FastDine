package com.fastdine.utt.model;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class Food {
    private String id;
    private String name;
    private String description;
    private String image;
    private double price;

    public Food(String name, String description, String image, double price) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.price = price;
    }

    public Food(String id, String name, String description, String image, double price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.price = price;
    }

    public Food(){
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public double getPrice() {
        return price;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public interface OnFoodListListener {
        void onComplete(List<Food> foodList);
        void onError(Exception e);
    }

    public static void getFoodList(final OnFoodListListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("foods")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Food> foodList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String id = document.getId();
                            String name = document.getString("name");
                            String description = document.getString("description");
                            String image = document.getString("image");
                            double price = document.getDouble("price");

                            foodList.add(new Food(id, name, description, image, price));
                        }
                        listener.onComplete(foodList); // Trả về danh sách món ăn
                    } else {
                        listener.onError(task.getException()); // Trả về lỗi nếu có
                    }
                });
    }

    public static void addFood(Food food, OnFoodListListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("foods")
                .add(food)
                .addOnSuccessListener(documentReference -> {
                    // Gán documentID vào trường id của đối tượng Food
                    food.setId(documentReference.getId());

                    // Cập nhật lại tài liệu Firestore với id
                    db.collection("foods").document(food.getId())
                            .update("id", food.getId())
                            .addOnSuccessListener(aVoid -> listener.onComplete(null))
                            .addOnFailureListener(e -> listener.onError(e));
                })
                .addOnFailureListener(e -> listener.onError(null));
    }

    public static void updateFood(Food food, OnFoodListListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("foods").document(food.getId())
                .set(food)
                .addOnSuccessListener(aVoid -> listener.onComplete(null))
                .addOnFailureListener(listener::onError);
    }

    public static void deleteFood(String foodId, OnFoodListListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("foods").document(foodId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    if (listener != null) {
                        listener.onComplete(null);
                    }
                })
                .addOnFailureListener(e -> {
                    if (listener != null) {
                        listener.onError(e);
                    }
                });
    }
}
