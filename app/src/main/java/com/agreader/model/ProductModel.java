package com.agreader.model;

import java.io.Serializable;

public class ProductModel implements Serializable {
    String id, imageProduct, nameProduct, dateProduct, merchant, point, status, brand;

    private String size, color, material, price, distributor, expiredDate, alamatBrand, logoBrand;

    public ProductModel() {
    }


    public String getAlamatBrand() {
        return alamatBrand;
    }

    public String getLogoBrand() {
        return logoBrand;
    }

    public String getSize() {
        return size;
    }

    public String getColor() {
        return color;
    }

    public String getMaterial() {
        return material;
    }

    public String getPrice() {
        return price;
    }

    public String getDistributor() {
        return distributor;
    }

    public String getExpiredDate() {
        return expiredDate;
    }


    public ProductModel(String imageProduct, String nameProduct, String brand, String dateProduct, String status, String size, String color, String material, String price, String distributor, String expiredDate, String alamatBrand, String logoBrand) {
        this.imageProduct = imageProduct;
        this.brand = brand;
        this.nameProduct = nameProduct;
        this.dateProduct = dateProduct;
        this.status = status;
        this.size = size;
        this.color = color;
        this.material = material;
        this.price = price;
        this.distributor = distributor;
        this.expiredDate = expiredDate;
        this.alamatBrand = alamatBrand;
        this.logoBrand = logoBrand;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
