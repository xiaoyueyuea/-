package com.yl.yymusic.app;

import android.app.Application;

import com.yl.yymusic.storage.table.Database;

public class AppConfig extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Database.initialize(this);
    }
}
