package com.bizarrecoding.example.moviepop.Objects;

/**
 * Created by Herik on 30/9/2017.
 */

public class Review {
    private String author;
    private String content;
    private String url;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Review (String author,String content,String url){
        this.author = author;
        this.content = content;
        this.url = url;
    }
}
