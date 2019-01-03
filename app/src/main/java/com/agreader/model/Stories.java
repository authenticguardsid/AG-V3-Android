package com.agreader.model;

public class Stories {

    private String titl, short_info;

    public Stories(String titl, String short_info) {
        this.titl = titl;
        this.short_info = short_info;
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
