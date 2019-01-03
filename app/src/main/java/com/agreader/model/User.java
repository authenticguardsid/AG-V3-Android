package com.agreader.model;

import java.io.Serializable;

/**
 * Created by Yudhistira Caraka on 12/13/2018.
 */

public class User implements Serializable {
    private String idEmail,idPhone,name, email, numberPhone, gender, age, address, gambar,totalPoint;

    public User() {
    }

    public User(String idEmail,String idPhone, String name, String email, String numberPhone, String gender, String age, String address, String gambar, String totalPoint) {
        this.idEmail = idEmail;
        this.idPhone = idPhone;
        this.name = name;
        this.email = email;
        this.numberPhone = numberPhone;
        this.gender = gender;
        this.age = age;
        this.address = address;
        this.gambar = gambar;
        this.totalPoint = totalPoint;
    }

    public String getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(String totalPoint) {
        this.totalPoint = totalPoint;
    }

    public String getIdEmail() {
        return idEmail;
    }

    public void setIdEmail(String idEmail) {
        this.idEmail = idEmail;
    }

    public String getIdPhone() {
        return idPhone;
    }

    public void setIdPhone(String idPhone) {
        this.idPhone = idPhone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumberPhone() {
        return numberPhone;
    }

    public void setNumberPhone(String numberPhone) {
        this.numberPhone = numberPhone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }
}
