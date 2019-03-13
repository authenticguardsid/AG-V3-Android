package com.agreader.model;

public class ListStore {

    private String id, image, brand_name, brand_address;

    public ListStore(String id, String image, String brand_name, String brand_address) {
        this.id = id;
        this.image = image;
        this.brand_name = brand_name;
        this.brand_address = brand_address;
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

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getBrand_address() {
        return brand_address;
    }

    public void setBrand_address(String brand_address) {
        this.brand_address = brand_address;
    }
}
