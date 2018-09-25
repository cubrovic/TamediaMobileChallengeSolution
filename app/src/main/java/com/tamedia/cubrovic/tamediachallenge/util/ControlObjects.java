package com.tamedia.cubrovic.tamediachallenge.util;

public class ControlObjects {

    public static com.google.gson.Gson createGson() {
        final com.google.gson.GsonBuilder gsonBuilder = new com.google.gson.GsonBuilder();
        return gsonBuilder.create();
    }
}
