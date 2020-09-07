package com.okunev.tinkoffexam.network;

import com.okunev.tinkoffexam.models.GifGetRes;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GifService {
    @GET("{part}/{page}?json=true")
    Call<GifGetRes> loadGifs(@Path("part") String part, @Path("page") int page);
}
