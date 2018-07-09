package com.example.mohamedabdelaziz.geo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PharmacyResponse {
    @SerializedName("success")
    private Integer success;
    @SerializedName("message")
    private String message;
    @SerializedName("products")
    private List<PharmacyProduct> products = null;

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

    public List<PharmacyProduct> getPharmacyProducts() {
        return products;
    }

    public void setPharmacyProducts(List<PharmacyProduct> products) {
        this.products = products;
    }


    public static class PharmacyProduct {

        @SerializedName("id")
        private String id;
        @SerializedName("name")
        private String name;
        @SerializedName("country_id")
        private String countryId;
        @SerializedName("lat")
        private String lat;
        @SerializedName("lang")
        private String lang;
        @SerializedName("open")
        private String open;
        @SerializedName("close")
        private String close;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCountryId() {
            return countryId;
        }

        public void setCountryId(String countryId) {
            this.countryId = countryId;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLang() {
            return lang;
        }

        public void setLang(String lang) {
            this.lang = lang;
        }

        public String getOpen() {
            return open;
        }

        public void setOpen(String open) {
            this.open = open;
        }

        public String getClose() {
            return close;
        }

        public void setClose(String close) {
            this.close = close;
        }
    }
}
