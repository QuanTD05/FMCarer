package com.example.fmcarer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fmcarer.Post;
import com.example.fmcarer.R;
import com.squareup.picasso.Picasso;


import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> postList;
    private Context context;

    public PostAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.txtContent.setText(post.getContent());
        holder.txtVisibility.setText("Phạm vi: " + post.getVisibility());

        if (post.getImageUrl() != null && !post.getImageUrl().isEmpty()) {
            Picasso.get().load(post.getImageUrl()).into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView txtContent;
        TextView txtVisibility;
        ImageView imageView;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            txtContent = itemView.findViewById(R.id.textViewContent);
            txtVisibility = itemView.findViewById(R.id.textViewVisibility);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
