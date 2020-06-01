package com.example.maskday.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.maskday.Model.NewsModel;
import com.example.maskday.R;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private List<NewsModel> newsModelList;

    public NewsAdapter(List<NewsModel> newsModelList) {
        this.newsModelList = newsModelList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView, contentTextView;

        public ViewHolder(View v) {
            super(v);
            titleTextView = v.findViewById(R.id.item_news_title);
            contentTextView = v.findViewById(R.id.item_news_content);
        }
    }

    @NonNull
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_board, parent, false);
        NewsAdapter.ViewHolder vh = new NewsAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.ViewHolder holder, int position) {
        holder.titleTextView.setText(newsModelList.get(position).getNewsTitle());
        holder.contentTextView.setText(newsModelList.get(position).getNewsContent());
    }

    @Override
    public int getItemCount() {
        return newsModelList.size();
    }
}

