package com.example.maskday.Model;

public class NewsModel {
    public String newsTitle;
    public String newsContent;
    public String newsLink;

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsContent() {
        return newsContent;
    }

    public void setNewsContent(String newsContent) {
        this.newsContent = newsContent;
    }

    public void setNewsLink(String newsLink) { this.newsLink = newsLink; }

    public String getNewsLink() { return newsLink; }
}
