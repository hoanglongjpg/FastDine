package com.fastdine.utt.view;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.fastdine.utt.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        logoutButton = findViewById(R.id.logout_button);

        // Kiểm tra xem người dùng đã đăng nhập chưa
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // Nếu chưa đăng nhập, chuyển đến màn hình đăng nhập
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        } else {
            // Nếu đã đăng nhập, kiểm tra vai trò người dùng
            checkUserRole(currentUser.getEmail());
        }

        // Xử lý sự kiện khi người dùng nhấn nút đăng xuất
        logoutButton.setOnClickListener(v -> {
            mAuth.signOut(); // Đăng xuất người dùng
            Toast.makeText(MainActivity.this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show(); // Thông báo xác nhận
            startActivity(new Intent(MainActivity.this, LoginActivity.class)); // Chuyển đến LoginActivity
            finish(); // Kết thúc MainActivity
        });
    }

    private void checkUserRole(String email) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Kiểm tra xem người dùng có phải là chủ cửa hàng không
        db.collection("owner")
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Intent intent;
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // Người dùng là chủ cửa hàng
                        intent = new Intent(MainActivity.this, OwnerActivity.class);
                    } else {
                        // Người dùng là khách hàng
                        intent = new Intent(MainActivity.this, CustomerActivity.class);
                    }
                    startActivity(intent);
                    finish(); // Đóng MainActivity
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(MainActivity.this, "Error retrieving user role.", Toast.LENGTH_SHORT).show();
                    finish(); // Đóng MainActivity
                });
    }
}
