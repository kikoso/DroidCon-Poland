package com.droidcon.snaphack;

import android.app.Application;
import android.os.Environment;

public class ShApplication extends Application {
    private static ShApplication instance;

    public static ShApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public String getConfiguredStorageDirectory() {
        return Environment.getExternalStorageDirectory().getPath() + "/SnapHack/";
    }
}
