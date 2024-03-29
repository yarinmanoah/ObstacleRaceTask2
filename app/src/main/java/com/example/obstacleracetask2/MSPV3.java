package com.example.obstacleracetask2;

import android.content.Context;
import android.content.SharedPreferences;

public class MSPV3 {
    private final String SP_FILE = "SP_FILE";
    private static MSPV3 me;
    private final SharedPreferences sharedPreferences;
    public static MSPV3 getMe() {
        return me;
    }
    private MSPV3(Context context) {
        sharedPreferences = context.getApplicationContext().getSharedPreferences(SP_FILE, Context.MODE_PRIVATE);
    }
    public static MSPV3 initHelper(Context context) {
        if (me == null)
            me = new MSPV3(context);
        return me;
    }
    public String getString(String KEY, String defValue) {
        return sharedPreferences.getString(KEY, defValue);
    }
    public void putString(String KEY, String value) {
        sharedPreferences.edit().putString(KEY, value).apply();
    }
}
