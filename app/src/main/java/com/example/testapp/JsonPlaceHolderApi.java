package com.example.testapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface JsonPlaceHolderApi {

    @GET("comments?")
    Call<List<Comment>> getComments(@Query(value="_start") String start, @Query(value="_limit") String end);
}
