package com.example.fmcarer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.fmcarer.Adapter.ChildAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference dbRef;
    private List<Child> childList;
    private ChildAdapter adapter;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // chứa RecyclerView + FloatingActionButton

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dbRef = FirebaseDatabase.getInstance().getReference("children");

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        childList = new ArrayList<>();
        adapter = new ChildAdapter(childList, this::showEditDialog, this::deleteChild);
        recyclerView.setAdapter(adapter);

        loadChildren();

        FloatingActionButton fab = findViewById(R.id.fabAdd);
        fab.setOnClickListener(v -> showAddDialog());
    }

    private void loadChildren() {
        dbRef.orderByChild("userId").equalTo(currentUserId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        childList.clear();
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            Child child = snap.getValue(Child.class);
                            if (child != null) {
                                childList.add(child);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
    }

    private void showAddDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_child, null);
        EditText edtName = view.findViewById(R.id.edtName);
        EditText edtBirthday = view.findViewById(R.id.edtBirthday);
        EditText edtGender = view.findViewById(R.id.edtGender);

        new AlertDialog.Builder(this)
                .setTitle("Thêm trẻ")
                .setView(view)
                .setPositiveButton("Lưu", (dialog, which) -> {
                    String id = dbRef.push().getKey();
                    Child child = new Child(id, currentUserId,
                            edtName.getText().toString(),
                            edtBirthday.getText().toString(),
                            edtGender.getText().toString());
                    dbRef.child(id).setValue(child);
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void showEditDialog(Child child) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_child, null);
        EditText edtName = view.findViewById(R.id.edtName);
        EditText edtBirthday = view.findViewById(R.id.edtBirthday);
        EditText edtGender = view.findViewById(R.id.edtGender);

        edtName.setText(child.getName());
        edtBirthday.setText(child.getBirthday());
        edtGender.setText(child.getGender());

        new AlertDialog.Builder(this)
                .setTitle("Chỉnh sửa")
                .setView(view)
                .setPositiveButton("Cập nhật", (dialog, which) -> {
                    child.setName(edtName.getText().toString());
                    child.setBirthday(edtBirthday.getText().toString());
                    child.setGender(edtGender.getText().toString());
                    dbRef.child(child.getChildId()).setValue(child);
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void deleteChild(Child child) {
        dbRef.child(child.getChildId()).removeValue();
    }
}