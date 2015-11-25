package com.droidcon.snaphack;

import android.app.Application;

public class ShApplication extends Application {
    private DropboxManager dropboxManager;

    public DropboxManager getDropboxManager() {
        return dropboxManager;
    }

    private static ShApplication instance;

    public static ShApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        dropboxManager = new DropboxManager(getApplicationContext());
    }
}
