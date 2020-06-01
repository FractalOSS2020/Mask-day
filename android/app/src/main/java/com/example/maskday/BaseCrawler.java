package com.example.maskday;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class BaseCrawler {

    public static Elements News() {
        Document doc = null;
        Elements articles = null;
        String baseUrl = "https://news.google.com/rss/";
        String term = "search?q=%EC%BD%94%EB%A1%9C%EB%82%98%20%2B%20";

        try {
           doc = Jsoup.connect(baseUrl + term).get();
            articles = doc.select("item");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return articles;
    }
}
