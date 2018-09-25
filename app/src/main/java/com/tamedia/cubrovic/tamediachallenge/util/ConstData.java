package com.tamedia.cubrovic.tamediachallenge.util;

import java.util.regex.Pattern;

public class ConstData {

    public static String getUsersApiPath() {
        return "https://jsonplaceholder.typicode.com/users";
    }

    public static String getUsersApiEmailParamName() {
        return "email";
    }

    public static String getPostsApiPath() {
        return "https://jsonplaceholder.typicode.com/posts";
    }

    public static String getPostsApiUseridParamName() {
        return "userId";
    }

    public static String getSharedPrefLastEmailKey() {
        return "lastemail";
    }

    public static String getSharedPrefLastDataKey() {
        return "lastdata";
    }

    public static String getSavedDataKey() {
        return "postsData";
    }


    public static final Pattern EMAIL_ADDRESS
            = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );
}
