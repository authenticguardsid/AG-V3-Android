package com.agreader.model;

public class NewsModel {
    String id, image, title, price, time, description, termCondition, agClientBrand_id, type, url;

    public NewsModel() {
    }

    public NewsModel(String id, String image, String title, String price, String time, String description, String termCondition, String agClientBrand_id, String type, String url) {
        this.id = id;
        this.image = image;
        this.title = title;
        this.price = price;
        this.time = time;
        this.description = description;
        this.termCondition = termCondition;
        this.agClientBrand_id = agClientBrand_id;
        this.type = type;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public String getTermCondition() {
        return termCondition;
    }

    public void setTermCondition(String termCondition) {
        this.termCondition = termCondition;
    }

    public String getAgClientBrand_id() {
        return agClientBrand_id;
    }

    public void setAgClientBrand_id(String agClientBrand_id) {
        this.agClientBrand_id = agClientBrand_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
