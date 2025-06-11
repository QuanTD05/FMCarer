package com.example.fmcarer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import com.google.firebase.database.*;

import java.text.SimpleDateFormat;
import java.util.*;

public class RegisterActivity extends AppCompatActivity {

    EditText edtFullname, edtEmail, edtPassword, edtPhone, edtAddress;
    Button btnRegister;
    TextView txtLogin;

    FirebaseAuth auth;
    DatabaseReference reference;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtFullname = findViewById(R.id.fullname);
        edtEmail = findViewById(R.id.email);
        edtPassword = findViewById(R.id.password);
        edtPhone = findViewById(R.id.phone);
        edtAddress = findViewById(R.id.address);
        btnRegister = findViewById(R.id.register);
        txtLogin = findViewById(R.id.txt_login);

        auth = FirebaseAuth.getInstance();

        txtLogin.setOnClickListener(v ->
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class))
        );

        btnRegister.setOnClickListener(v -> {
            pd = new ProgressDialog(RegisterActivity.this);
            pd.setMessage("Đang đăng ký...");
            pd.setCancelable(false);
            pd.show();

            String fullName = edtFullname.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();
            String address = edtAddress.getText().toString().trim();

            if (TextUtils.isEmpty(fullName) || TextUtils.isEmpty(email) ||
                    TextUtils.isEmpty(password) || TextUtils.isEmpty(phone) ||
                    TextUtils.isEmpty(address)) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            } else if (password.length() < 6) {
                Toast.makeText(this, "Mật khẩu phải từ 6 ký tự trở lên", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            } else {
                registerMainAccount(fullName, email, password, phone, address);
            }
        });
    }

    private void registerMainAccount(String fullName, String email, String password, String phone, String address) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        if (firebaseUser != null) {
                            String userId = firebaseUser.getUid();
                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

                            HashMap<String, Object> userMap = new HashMap<>();
                            userMap.put("userId", userId);
                            userMap.put("fullName", fullName);
                            userMap.put("email", email);
                            userMap.put("password", password); // Có thể mã hóa nếu cần
                            userMap.put("phone", phone);
                            userMap.put("address", address);
                            userMap.put("role", "parent"); // mặc định là tài khoản chính
                            userMap.put("balance", 0); // tài khoản mới, số dư = 0

                            String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                            userMap.put("createdAt", currentTime);

                            reference.setValue(userMap).addOnCompleteListener(task1 -> {
                                pd.dismiss();
                                if (task1.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(RegisterActivity.this, "Không thể lưu dữ liệu người dùng", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } else {
                        pd.dismiss();
                        Toast.makeText(RegisterActivity.this, "Đăng ký thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
