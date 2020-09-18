package com.hezongze.privatedemo.rxJavaAndRetrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private Retrofit mRetrofit;
    private static final String BASE_URL = "http://192.168.80.93:8080/biss-app";
    private RetrofitServices mService;

    //构造方法
    public RetrofitClient() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mService = mRetrofit.create(RetrofitServices.class);
    }

    public RetrofitServices getRectService() {
        if (mService != null) {
            return mService;
        }
        return null;
    }
}
