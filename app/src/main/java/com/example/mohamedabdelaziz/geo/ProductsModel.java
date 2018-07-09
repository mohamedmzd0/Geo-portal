package com.example.mohamedabdelaziz.geo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductsModel {
    @SerializedName("success")

    private Integer success;
    @SerializedName("message")

    private String message;
    @SerializedName("products")

    private List<Product> products = null;

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

}


