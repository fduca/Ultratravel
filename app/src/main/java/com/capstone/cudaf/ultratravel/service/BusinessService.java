package com.capstone.cudaf.ultratravel.service;

import com.capstone.cudaf.ultratravel.model.YelpResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BusinessService {


    String YELP_API_BASE_URL = "https://api.yelp.com";

    @GET("/v2/search/")
    Call<YelpResponse> listBusiness(@Query("term") String term, @Query("location") String location);
}
