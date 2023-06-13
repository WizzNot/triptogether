package com.example.project.server_data;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Service {
    @GET("/")
    Call<Data> fetchUser();
    @POST("/")
    Call<Data> give_date(@Body Data requestData);//интерфес для POST запроса к серверу
    @GET("/method/{method}")
    Call<VkResponse> vkRequest(@Path("method") String method, @Query("access_token") String access_token, @Query("fields") String fields, @Query("v") String v);//интерфес для GET запроса к VkApi
}
