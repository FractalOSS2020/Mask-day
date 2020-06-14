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

public class  CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private List<CommentModel> commentAdapterList;

    public CommentAdapter(List<CommentModel> commentAdapterList) {
        this.commentAdapterList = commentAdapterList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView comment_tv, comment_user;

        public ViewHolder(View v) {
            super(v);
            comment_tv = v.findViewById(R.id.comment_content);
            comment_user = v.findViewById(R.id.comment_user);
        }
    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.comment_user.setText(commentAdapterList.get(position).getCommentUser());
        holder.comment_tv.setText(commentAdapterList.get(position).getCommentContent());
    }

    @Override
    public int getItemCount() {
        return commentAdapterList.size();
    }
}
