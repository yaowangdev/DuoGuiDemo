package com.appdev.duoguidemo;

import android.app.Application;

import com.appdev.framework.db.AMDatabase;

public class MyApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        AMDatabase.init(getApplicationContext());


    }
}
