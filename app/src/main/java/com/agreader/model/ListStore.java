package com.agreader.model;

public class ListStore {

    private int image;
    private String brand_name;
    private Double latitude;
    private Double longitude;

    public ListStore(int image, String brand_name, Double latitude, Double longitude) {
        this.image = image;
        this.brand_name = brand_name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public ListStore() {

    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
