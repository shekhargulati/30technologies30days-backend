package com.shekhar.challenge.domain;

import java.util.Date;

public class Blog {
    
    private String id;
    private String title;
    private String url;
    private Date publishedOn = new Date();
    
    public Blog() {
    }

    public Blog(String id , String title, String url, Date publishedOn) {
        super();
        this.title = title;
        this.url = url;
        this.id = id;
        this.publishedOn = publishedOn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getPublishedOn() {
        return publishedOn;
    }

    public void setPublishedOn(Date publishedOn) {
        this.publishedOn = publishedOn;
    }
    
}
