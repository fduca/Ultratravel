package com.capstone.cudaf.ultratravel.service;

import com.capstone.cudaf.ultratravel.deserializer.BusinessDeserializer;
import com.capstone.cudaf.ultratravel.model.Business;
import com.capstone.cudaf.ultratravel.model.BusinessType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer;
import se.akerfeldt.okhttp.signpost.SigningInterceptor;


public class BusinessServiceGenerator {

    private OkHttpClient httpClient;

    public BusinessServiceGenerator(String consumerKey, String consumerSecret, String token, String tokenSecret) {
        OkHttpOAuthConsumer consumer = new OkHttpOAuthConsumer(consumerKey, consumerSecret);
        consumer.setTokenWithSecret(token, tokenSecret);
        this.httpClient = new OkHttpClient.Builder()
                .addInterceptor(new SigningInterceptor(consumer))
                .build();
    }

    public BusinessService createAPI(BusinessType type) {

        Gson gson =
                new GsonBuilder().registerTypeAdapter(Business.class, new BusinessDeserializer(type))
                        .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getAPIBaseUrl())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(this.httpClient)

                .build();

        return retrofit.create(BusinessService.class);
    }

    private String getAPIBaseUrl() {
        return BusinessService.YELP_API_BASE_URL;
    }
}