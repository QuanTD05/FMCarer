package com.example.fmcarer;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class activity_child_detail extends AppCompatActivity {

    private TextView tvChildName;
    private LinearLayout btnReminder, btnDiary, btnEdit;
    private Child child;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_detail);

        tvChildName = findViewById(R.id.tvChildName);
        btnReminder = findViewById(R.id.btnReminder);
        btnDiary = findViewById(R.id.btnDiary);
        btnEdit = findViewById(R.id.btnEdit);

        dbRef = FirebaseDatabase.getInstance().getReference("children");

        // Nhận dữ liệu từ Intent
        child = (Child) getIntent().getSerializableExtra("CHILD_OBJECT");

        if (child != null) {
            tvChildName.setText(child.getName());
        }

        // Sự kiện Lịch nhắc
        btnReminder.setOnClickListener(v -> {
            Intent intent = new Intent(this, ReminderActivity.class);
            intent.putExtra("CHILD_ID", child.getChildId());
            startActivity(intent);
        });

        // Sự kiện Nhật ký chăm sóc
        btnDiary.setOnClickListener(v -> {
            Intent intent = new Intent(this, CareLogActivity.class);
            intent.putExtra("CHILD_ID", child.getChildId());
            startActivity(intent);
        });

        // Sự kiện Chỉnh sửa
        btnEdit.setOnClickListener(v -> showEditDialog());
    }

    private void showEditDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_child, null);
        EditText edtName = view.findViewById(R.id.edtName);
        EditText edtBirthday = view.findViewById(R.id.edtBirthday);
        EditText edtGender = view.findViewById(R.id.edtGender);

        edtName.setText(child.getName());
        edtBirthday.setText(child.getBirthday());
        edtGender.setText(child.getGender());

        new AlertDialog.Builder(this)
                .setTitle("Chỉnh sửa thông tin")
                .setView(view)
                .setPositiveButton("Cập nhật", (dialog, which) -> {
                    child.setName(edtName.getText().toString());
                    child.setBirthday(edtBirthday.getText().toString());
                    child.setGender(edtGender.getText().toString());
                    dbRef.child(child.getChildId()).setValue(child);

                    tvChildName.setText(child.getName()); // cập nhật tên trên UI
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
