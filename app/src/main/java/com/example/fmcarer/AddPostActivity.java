package com.example.fmcarer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.provider.MediaStore;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

public class AddPostActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText edtContent;
    private RadioGroup radioGroupVisibility;
    private ImageView imageView;
    private Button btnAddPost;

    private Uri imageUri;

    private DatabaseReference dbPosts;
    private StorageReference storageReference;

    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        edtContent = findViewById(R.id.edtContent);
        radioGroupVisibility = findViewById(R.id.radioGroupVisibility);
        imageView = findViewById(R.id.imageView);
        btnAddPost = findViewById(R.id.btnAddPost);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        dbPosts = FirebaseDatabase.getInstance().getReference("posts");

        storageReference = FirebaseStorage.getInstance().getReference("post_images");

        imageView.setOnClickListener(v -> selectImage());

        btnAddPost.setOnClickListener(v -> addPost());
    }

    private void selectImage(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void addPost(){
        final String content = edtContent.getText().toString().trim();

        int selectedId = radioGroupVisibility.getCheckedRadioButtonId();
        RadioButton radio = findViewById(selectedId);
        final String visibility = radio == null ? "Gia đình" : radio.getText().toString();

        if (imageUri == null) {
            // nếu chưa có ảnh
            String id = dbPosts.push().getKey();
            Post post = new Post(id, currentUserId, content, "", visibility);
            dbPosts.child(id).setValue(post);
            finish();

        } else {
            // nếu có ảnh
            StorageReference ref = storageReference.child(UUID.randomUUID().toString());

            ref.putFile(imageUri).addOnSuccessListener(taskSnapshot ->
                    ref.getDownloadUrl().addOnSuccessListener(url -> {
                        String id = dbPosts.push().getKey();
                        Post post = new Post(id, currentUserId, content, url.toString(), visibility);
                        dbPosts.child(id).setValue(post);
                        finish();

                    })
            ).addOnFailureListener(e -> {
                // xử lý nếu có lỗi
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE_REQUEST && data != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }
}
