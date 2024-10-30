package com.fastdine.utt.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.fastdine.utt.R;
import com.fastdine.utt.controller.CustomerController;
import com.fastdine.utt.model.Customer;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {
    private TextView profileName, profilePhone, profileEmail, profileAddress;
    private Button logoutButton;
    private CustomerController ctrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_view_profile); // Đảm bảo bạn đặt đúng layout

        // Khởi tạo các View
        profileName = findViewById(R.id.profileName);
        profilePhone = findViewById(R.id.profilePhone);
        profileEmail = findViewById(R.id.profileEmail);
        profileAddress = findViewById(R.id.profileAddress);
        logoutButton = findViewById(R.id.logoutButton);

        ctrl = new CustomerController(this);
        viewCustomerInfo();

        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut(); // Đăng xuất khỏi Firebase
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class); // Tạo Intent chuyển đến LoginActivity
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent); // Bắt đầu LoginActivity
            finish(); // Kết thúc ProfileActivity
        });
    }

    private void viewCustomerInfo() {
        ctrl.viewCustomerInfo(profileName, profilePhone, profileEmail, profileAddress);
    }
}
