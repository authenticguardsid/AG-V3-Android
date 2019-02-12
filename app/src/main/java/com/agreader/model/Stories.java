package com.agreader.model;

public class Stories {

    private String titl, short_info,image,url;

    public Stories(String titl, String short_info,String image,String url) {
        this.titl = titl;
        this.short_info = short_info;
        this.image = image;
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitl() {
        return titl;
    }

    public void setTitl(String titl) {
        this.titl = titl;
    }

    public String getShort_info() {
        return short_info;
    }

    public void setShort_info(String short_info) {
        this.short_info = short_info;
    }

}
