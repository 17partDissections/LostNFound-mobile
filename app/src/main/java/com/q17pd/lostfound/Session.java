package com.q17pd.lostfound;

import android.content.Context;
import android.content.SharedPreferences;

public class Session {

    private static SharedPreferences prefs(Context c) {
        return c.getSharedPreferences("session", Context.MODE_PRIVATE);
    }

    public static boolean isLoggedIn(Context c) {
        return prefs(c).getBoolean("logged", false);
    }

    public static String getName(Context c) {
        return prefs(c).getString("name", "");
    }

    public static void login(Context c, String name) {
        prefs(c).edit().putBoolean("logged", true).putString("name", name).apply();
    }

    public static void logout(Context c) {
        prefs(c).edit().clear().apply();
    }
}
