package com.agreader.model;

import java.io.Serializable;

public class ProductModel implements Serializable {
    String id, imageProduct, nameProduct, dateProduct, merchant, point;

    public ProductModel() {
    }

    public ProductModel(String id, String imageProduct, String nameProduct, String dateProduct, String merchant, String point) {
        this.id = id;
        this.imageProduct = imageProduct;
        this.nameProduct = nameProduct;
        this.dateProduct = dateProduct;
        this.merchant = merchant;
        this.point = point;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageProduct() {
        return imageProduct;
    }

    public void setImageProduct(String imageProduct) {
        this.imageProduct = imageProduct;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public String getDateProduct() {
        return dateProduct;
    }

    public void setDateProduct(String dateProduct) {
        this.dateProduct = dateProduct;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }
}
