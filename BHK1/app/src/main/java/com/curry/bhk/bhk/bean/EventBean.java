package com.curry.bhk.bhk.bean;

/**
 * This is the bean data about the event
 * Created by Curry on 2016/8/17.
 */
public class EventBean {
    private int id;
    private String author;
    private String time;
    private String description;
    private String email;
    private String photos_url;
    private String title;
    private int status; // 0:new ,1:pending ,2:on hold ,3:resolved
    private String resolvedby;

    public String getResolvedby() {
        return resolvedby;
    }

    public void setResolvedby(String resolvedby) {
        this.resolvedby = resolvedby;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotos_url() {
        return photos_url;
    }

    public void setPhotos_url(String photos_url) {
        this.photos_url = photos_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


}
