package com.tamedia.cubrovic.tamediachallenge;

public class ConstBusinessLogic {

    public static final String getUsersApiPath() {
        return "https://jsonplaceholder.typicode.com/users";
    }

    public static final String getUsersApiEmailParamName() {
        return "email";
    }

    public static final String getPostsApiPath() {
        return "https://jsonplaceholder.typicode.com/posts";
    }

    public static final String getPostsApiUseridParamName() {
        return "userId";
    }

    public static final String getSharedPrefLastEmailKey() {
        return "lastemail";
    }

    public static final String getSharedPrefLastDataKey() {
        return "lastdata";
    }

    public static final String getSavedDataKey() {
        return "postsData";
    }

}
