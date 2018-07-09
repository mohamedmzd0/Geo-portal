package com.example.mohamedabdelaziz.geo;

import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("id")

    private String id;
    @SerializedName("medcine_name")

    private String medcineName;
    @SerializedName("price")

    private String price;
    @SerializedName("description")

    private String description;
    @SerializedName("precautions")

    private String precautions;
    @SerializedName("image")

    private String image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMedcineName() {
        return medcineName;
    }

    public void setMedcineName(String medcineName) {
        this.medcineName = medcineName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrecautions() {
        return precautions;
    }

    public void setPrecautions(String precautions) {
        this.precautions = precautions;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
