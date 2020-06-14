package com.example.maskday.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.maskday.Activity.DetailActivity;
import com.example.maskday.Activity.NewsDetailActivity;
import com.example.maskday.Adapter.NewsAdapter;
import com.example.maskday.BaseCrawler;
import com.example.maskday.Model.NewsModel;
import com.example.maskday.R;
import com.example.maskday.RecyclerClickListener;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment {

    private RecyclerView newsRecyclerView;
    private NewsAdapter newsAdapter;
    RecyclerView.LayoutManager layoutManager;
    private List<NewsModel> newsModelList;
    private SwipeRefreshLayout swipeRefreshLayout;


    public NewsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        init(view);
        String search = "제주";

        AsyncCrawl asyncCrawl = new AsyncCrawl();
        asyncCrawl.execute(search);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                AsyncCrawl asyncCrawl = new AsyncCrawl();
                asyncCrawl.execute(search);
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        return view;
    }

    private void init(View view) {
        newsRecyclerView = (RecyclerView) view.findViewById(R.id.news_recycler_view);
        newsRecyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), 1));
        layoutManager = new LinearLayoutManager(getActivity());
        newsRecyclerView.setLayoutManager(layoutManager);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.news_swipe_layout);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
    }

    private class AsyncCrawl extends AsyncTask<String, Void, Elements> {
        @Override
        protected Elements doInBackground(String... strings) {
            if (strings.length != 1) {
                throw new IllegalArgumentException();
            }
            String term = strings[0];
            Elements items = BaseCrawler.News(term);

            if (items != null) {
                newsModelList = new ArrayList<>();
                for (Element item : items) {
                    NewsModel newsModel = new NewsModel();

                    String title = item.select("title").text();
                    String link = item.select("originallink").text();
                    String pubDate = item.select("pubDate").text();

                    newsModel.setNewsTitle(title);
                    newsModel.setNewsContent(pubDate);
                    newsModel.setNewsLink(link);

                    Log.d("!!!", newsModel.getNewsLink());

                    newsModelList.add(newsModel);
                }

//                newsAdapter = new NewsAdapter(newsModelList);
//                newsRecyclerView.setAdapter(newsAdapter);
            }

            return items;
        }

        @Override
        protected void onPostExecute(Elements elements) {
            super.onPostExecute(elements);

            newsAdapter = new NewsAdapter(newsModelList);
            newsRecyclerView.setAdapter(newsAdapter);

        }
    }

}
