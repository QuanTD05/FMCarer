package com.example.fmcarer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fmcarer.Adapter.ChildAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference dbRef;
    private List<Child> childList;
    private ChildAdapter adapter;
    private String currentUserId;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dbRef = FirebaseDatabase.getInstance().getReference("children");

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        childList = new ArrayList<>();
        adapter = new ChildAdapter(childList, this, this::deleteChild);
        recyclerView.setAdapter(adapter);

        loadChildren();

        FloatingActionButton fab = findViewById(R.id.fabAdd);
        fab.setOnClickListener(v -> showAddDialog());

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                // Hiện MainActivity hoặc HomeFragment
                return true;

            } else if (itemId == R.id.nav_post) {
                // Chuyển sang CommunityActivity
                Intent intent = new Intent(this, PostActivity.class);
                startActivity(intent);
                return true;

            } else if (itemId == R.id.nav_account) {
//                // Chuyển sang ProfileActivity
//                Intent intent = new Intent(this, ProfileActivity.class);
//                startActivity(intent);
//                return true;
            }

            return false;
        });


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
                            edtName.getText().toString().trim(),
                            edtBirthday.getText().toString().trim(),
                            edtGender.getText().toString().trim());
                    dbRef.child(id).setValue(child);
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void deleteChild(Child child) {
        dbRef.child(child.getChildId()).removeValue();
    }
}
