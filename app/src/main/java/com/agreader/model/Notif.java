package com.agreader.model;

public class Notif {

    private String id;
    private String title;
    private String message;
    private String image;
    private String date;

    public Notif() {
    }

    public Notif(String id, String title, String message, String image, String date) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.image = image;
        this.date = date;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
