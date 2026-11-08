package com.q17pd.lostfound;

import android.content.Context;
import android.content.SharedPreferences;

public class Session {
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_PHONE = "phone";
    private static SharedPreferences prefs(Context c) {
        return c.getSharedPreferences("session", Context.MODE_PRIVATE);
    }
    public static boolean isLoggedIn(Context c) {
        return getUserId(c) != -1;
    }
    public static int getUserId(Context c) {
        return prefs(c).getInt(KEY_USER_ID, -1);
    }
    public static String getPhone(Context c) {
        return prefs(c).getString(KEY_PHONE, "");
    }
    public static void login(Context c, int userId, String phone) {
        prefs(c).edit().putInt(KEY_USER_ID, userId).putString(KEY_PHONE, phone).apply();
    }
    public static void logout(Context c) {
        prefs(c).edit().clear().apply();
    }
}