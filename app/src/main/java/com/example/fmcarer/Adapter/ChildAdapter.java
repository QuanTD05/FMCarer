package com.example.fmcarer.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fmcarer.Child;
import com.example.fmcarer.R;

import java.util.List;

public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ViewHolder> {

    private List<Child> childList;
    private OnChildEditListener editListener;
    private OnChildDeleteListener deleteListener;

    public interface OnChildEditListener {
        void onEdit(Child child);
    }

    public interface OnChildDeleteListener {
        void onDelete(Child child);
    }

    public ChildAdapter(List<Child> list, OnChildEditListener edit, OnChildDeleteListener delete) {
        this.childList = list;
        this.editListener = edit;
        this.deleteListener = delete;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_child, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Child child = childList.get(position);
        holder.txtName.setText(child.getName());
        holder.txtEmail.setText(child.getBirthday());

        holder.itemView.setOnClickListener(v -> editListener.onEdit(child));
        holder.btnDelete.setOnClickListener(v -> deleteListener.onDelete(child));
    }

    @Override
    public int getItemCount() {
        return childList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtEmail;
        ImageButton btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtEmail = itemView.findViewById(R.id.txtBirthday);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}