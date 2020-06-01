package com.example.maskday.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maskday.R;
import com.example.maskday.Model.UserModel;

import java.util.List;
import java.util.Map;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.ViewHolder> {
    private List<UserModel> userModelList;
    private Map<String, UserModel> userModelMap;

    public BoardAdapter(List<UserModel> userModelsList) {
        this.userModelList = userModelsList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView, contentTextView, userTextView;
        public ViewHolder(View v) {
            super(v);
            titleTextView = v.findViewById(R.id.item_title);
            contentTextView = v.findViewById(R.id.item_content);
            userTextView = v.findViewById(R.id.item_user_email);
        }
    }

    @NonNull
    @Override
    public BoardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_board, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.titleTextView.setText(userModelList.get(position).getTitle());
        holder.contentTextView.setText(userModelList.get(position).getContent());
        holder.userTextView.setText(userModelList.get(position).getUserEmail());
    }

    @Override
    public int getItemCount() {
        return userModelList.size();
    }


}
