package com.okunev.tinkoffexam.base;

import android.app.Application;

import com.okunev.tinkoffexam.network.Api;

public class GifApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Api.init();
    }
}
