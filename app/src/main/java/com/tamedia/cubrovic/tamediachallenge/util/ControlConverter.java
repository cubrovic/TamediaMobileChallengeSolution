package com.tamedia.cubrovic.tamediachallenge.util;

import com.tamedia.cubrovic.tamediachallenge.domain.PostsData;

public class ControlConverter {
    public static PostsData convertJsonToPostsData(String jsonPost) {
        final com.google.gson.Gson gson = ControlObjects.createGson();
        return gson.fromJson(jsonPost, PostsData.class);
    }

    public static String convertPostsDataToJson(PostsData postsData) {
        final com.google.gson.Gson gson = ControlObjects.createGson();
        return gson.toJson(postsData);
    }
}
