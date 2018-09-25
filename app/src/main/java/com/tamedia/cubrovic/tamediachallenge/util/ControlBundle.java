package com.tamedia.cubrovic.tamediachallenge.util;

import android.os.Bundle;

public class ControlBundle {
    public static void writeToBundle(Bundle outState, String state) {
        outState.putString(ConstData.getSavedDataKey(), state);
    }

    public static String getFromBundle(Bundle savedInstanceState) {
        return savedInstanceState.getString(ConstData.getSavedDataKey());
    }
}
