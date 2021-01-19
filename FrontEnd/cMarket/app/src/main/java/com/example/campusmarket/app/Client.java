package com.example.campusmarket.app;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Client {
    @Multipart
    @POST("/upload")
    Call<ResponseBody> uploadImage(@Part("category") RequestBody requestBody,
                                   @Part MultipartBody.Part file);


    @POST("/upload")
    Call<ResponseBody> uploadImage2(@Part MultipartBody.Part file, @Part("name") RequestBody requestBody);
}
