package com.example.mohamedabdelaziz.geo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrdersResponse {

    @SerializedName("success")
    private Integer success;
    @SerializedName("message")
    private String message;
    @SerializedName("products")
    private List<OrderProduct> products = null;

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

    public List<OrderProduct> getProducts() {
        return products;
    }

    public void setProducts(List<OrderProduct> products) {
        this.products = products;
    }


    public static class OrderProduct {

        @SerializedName("id")
        private String id;
        @SerializedName("date")
        private String date;
        @SerializedName("quantity")
        private String quantity;
        @SerializedName("medcine_name")
        private String medcineName;
        @SerializedName("user_name")
        private String userName;
        @SerializedName("price")
        private String price;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getMedcineName() {
            return medcineName;
        }

        public void setMedcineName(String medcineName) {
            this.medcineName = medcineName;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }
}