package com.example.mohamedabdelaziz.geo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SalesResponse {


    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("products")
    @Expose
    private List<SalesProduct> products = null;

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

    public List<SalesProduct> getSalesProducts() {
        return products;
    }

    public void setSalesProducts(List<SalesProduct> products) {
        this.products = products;
    }


    public static class SalesProduct {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("sales")
        @Expose
        private String sales;
        @SerializedName("title")
        @Expose
        private String title;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSales() {
            return sales;
        }

        public void setSales(String sales) {
            this.sales = sales;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

    }
}
