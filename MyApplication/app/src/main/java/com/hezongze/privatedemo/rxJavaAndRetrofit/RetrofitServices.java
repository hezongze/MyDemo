package com.hezongze.privatedemo.rxJavaAndRetrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitServices {

    @GET("/video/category/list")
    Call<ResponseBody> getManagerData(@Query("Sortid") int id);
}
