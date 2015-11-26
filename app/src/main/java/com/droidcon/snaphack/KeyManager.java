package com.droidcon.snaphack;

import android.content.Context;
import android.content.SharedPreferences;

public class KeyManager {
    private static final String KEY_PREFS = "sdfsdf";
    private final SharedPreferences prefs;
    private static final String PREFS = "prefs";

    public KeyManager(Context context) {
        this.prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public void save(String key) {
        prefs.edit().putString(KEY_PREFS, key).apply();
    }

    public String read() {
        return prefs.getString(KEY_PREFS, "");
    }

}
