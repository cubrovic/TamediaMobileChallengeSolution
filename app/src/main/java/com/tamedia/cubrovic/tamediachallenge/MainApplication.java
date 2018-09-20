package com.tamedia.cubrovic.tamediachallenge;

public class MainApplication extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        com.androidnetworking.AndroidNetworking.initialize(getApplicationContext());
    }
}
