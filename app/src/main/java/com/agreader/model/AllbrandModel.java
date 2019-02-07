package com.agreader.model;

public class AllbrandModel {
    String id,image,name,address,client;

    public AllbrandModel(String id, String image, String name, String address, String client) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.address = address;
        this.client = client;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }
}
