package com.example.mohamedabdelaziz.geo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateAPiServices {
    public static ApiServices createInterface() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2/graduation/webservices/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit.create(ApiServices.class);
    }

    public static ApiServices createInterfaceForImage() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2/graduation/ecommerce/admin/uploads/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit.create(ApiServices.class);
    }
}
