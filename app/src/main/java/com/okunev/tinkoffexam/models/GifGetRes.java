package com.okunev.tinkoffexam.models;

import java.io.Serializable;
import java.util.ArrayList;

public class GifGetRes implements Serializable {
    private ArrayList<GifReference> result;

    public ArrayList<GifReference> getResult() {
        return result;
    }
}
