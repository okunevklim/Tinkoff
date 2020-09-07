package com.okunev.tinkoffexam.models;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class GifReference implements Serializable {
    private String description;
    private String gifURL;

    public String getDescription() {
        return description;
    }

    public String getGifURL() {
        return gifURL;
    }



    @NotNull
    @Override
    public String toString() {
        return "GifReference{" +
                ", description='" + description + '\'' +
                ", gifURL='" + gifURL  +
                '}';
    }
}
