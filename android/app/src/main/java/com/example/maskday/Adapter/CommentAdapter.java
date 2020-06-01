package com.example.maskday.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maskday.Model.CommentModel;
import com.example.maskday.R;

import java.util.List;
import java.util.Map;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private List<CommentModel> commentAdapterList;
    private Map<String, CommentAdapter> commentAdapterMap;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView comment_tv, comment_user;

        public ViewHolder(View v) {
            super(v);
            comment_tv = v.findViewById(R.id.comment_content);
            comment_user = v.findViewById(R.id.comment_user);
        }
    }

    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false);
        CommentAdapter.ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    public CommentAdapter(List<CommentModel> commentAdapterList) {
        this.commentAdapterList = commentAdapterList;
    }


    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {

        holder.comment_user.setText(commentAdapterList.get(position).commentUser);
        holder.comment_tv.setText(commentAdapterList.get(position).commentContent);
    }

    @Override
    public int getItemCount() {
        return commentAdapterList.size();
    }
}
