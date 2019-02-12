package com.agreader.model;

import java.io.Serializable;

public class Rank implements Serializable {

    public Rank() {

    }

    public Rank(int image, String rank, String name, String point) {
        this.image = image;
        this.rank = rank;
        this.name = name;
        this.point = point;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    int image;
    String rank, name, point;
}