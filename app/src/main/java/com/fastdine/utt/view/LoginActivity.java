package com.fastdine.utt.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fastdine.utt.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Khởi tạo Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Liên kết các thành phần giao diện
        emailEditText = findViewById(R.id.login_email);
        passwordEditText = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);

        // Xử lý sự kiện khi người dùng nhấn nút đăng nhập
        loginButton.setOnClickListener(v -> loginUser());

        // Điều hướng đến màn hình đăng ký nếu chưa có tài khoản
        findViewById(R.id.register_redirect).setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    // Hàm xử lý đăng nhập người dùng
    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Kiểm tra nếu email hoặc mật khẩu rỗng
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email là bắt buộc");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Mật khẩu là bắt buộc");
            return;
        }

        // Đăng nhập người dùng bằng Firebase Authentication
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Đăng nhập thành công, kiểm tra vai trò người dùng
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

                        // Lấy thông tin người dùng hiện tại
                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        FirebaseFirestore db = FirebaseFirestore.getInstance();

                        // Kiểm tra xem người dùng có phải là chủ cửa hàng không
                        db.collection("owner")
                                .whereEqualTo("email", email)
                                .get()
                                .addOnSuccessListener(queryDocumentSnapshots -> {
                                    Intent intent;
                                    if (!queryDocumentSnapshots.isEmpty()) {
                                        // Người dùng là chủ cửa hàng
                                        intent = new Intent(LoginActivity.this, OwnerActivity.class);
                                    } else {
                                        // Người dùng là khách hàng
                                        intent = new Intent(LoginActivity.this, CustomerActivity.class);
                                    }
                                    startActivity(intent);
                                    finish(); // Đóng LoginActivity
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(LoginActivity.this, "Lỗi khi truy xuất vai trò người dùng.", Toast.LENGTH_SHORT).show();
                                    finish(); // Đóng LoginActivity
                                });
                    } else {
                        // Hiển thị lỗi nếu đăng nhập thất bại
                        Toast.makeText(LoginActivity.this, "Đăng nhập thất bại: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
