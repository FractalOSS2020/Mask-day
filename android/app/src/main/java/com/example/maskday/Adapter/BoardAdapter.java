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

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.ViewHolder> {
    private List<UserModel> userModelList;

    public BoardAdapter(List<UserModel> userModelsList) {
        this.userModelList = userModelsList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView, timeTextView, contentTextView;
        public ViewHolder(View v) {
            super(v);
            titleTextView = v.findViewById(R.id.item_title);
            timeTextView = v.findViewById(R.id.item_timestamp);
            contentTextView = v.findViewById(R.id.item_content);
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
        holder.timeTextView.setText(userModelList.get(position).getTimestamp());
        holder.contentTextView.setText(userModelList.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return userModelList.size();
    }


}
