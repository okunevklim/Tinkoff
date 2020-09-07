package com.okunev.tinkoffexam.models;

import java.io.Serializable;

public enum GifType implements Serializable {
    LATEST("latest"), TOP("top"), HOT("hot");

    private String urlPart;

    GifType(String urlPart) {
        this.urlPart = urlPart;
    }

    public String getUrlPart() {
        return urlPart;
    }
}
