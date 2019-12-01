package com.example.tikitaka;

import java.io.Serializable;

public class NewsData implements Serializable {
    private String title;
    private String urlToImage;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

}
