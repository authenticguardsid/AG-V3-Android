package com.agreader.model;

/**
 * Created by Yudhistira Caraka on 12/21/2018.
 */

public class Hadiah {
    private String idHadiah,gambar,judul,totalPoint,tersisa,expired,desc,termC;

    public Hadiah() {
    }

    public Hadiah(String idHadiah,String gambar, String judul, String totalPoint, String tersisa,String expired,String desc,String termC) {
        this.idHadiah = idHadiah;
        this.gambar = gambar;
        this.judul = judul;
        this.totalPoint = totalPoint;
        this.tersisa = tersisa;
        this.expired = expired;
        this.desc = desc;
        this.termC = termC;
    }

    public String getIdHadiah() {
        return idHadiah;
    }

    public void setIdHadiah(String idHadiah) {
        this.idHadiah = idHadiah;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(String totalPoint) {
        this.totalPoint = totalPoint;
    }

    public String getTersisa() {
        return tersisa;
    }

    public void setTersisa(String tersisa) {
        this.tersisa = tersisa;
    }

    public String getExpired() {
        return expired;
    }

    public void setExpired(String expired) {
        this.expired = expired;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTermC() {
        return termC;
    }

    public void setTermC(String termC) {
        this.termC = termC;
    }
}
