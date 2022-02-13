package com.openclassrooms.realestatemanager;

import android.app.Application;
import android.content.Context;

public class MainApplication extends Application {

    private static Application sApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
    }

    public static Application getApplication() {
        return sApplication;
    }
    public static Context getContext() {
        return sApplication.getApplicationContext();
    }
}