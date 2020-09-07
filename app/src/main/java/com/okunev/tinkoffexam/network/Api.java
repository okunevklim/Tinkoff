package com.okunev.tinkoffexam.network;

import com.okunev.tinkoffexam.BuildConfig;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {
    private static final String SERVER_URL = "https://developerslife.ru/";

    private static GifService serviceGif;

    public static void init() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.level(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        serviceGif = retrofit.create(GifService.class);
    }

    public static GifService getService() {
        return serviceGif;
    }
}
