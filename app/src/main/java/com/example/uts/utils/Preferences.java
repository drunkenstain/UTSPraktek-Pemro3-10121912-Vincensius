package com.example.uts.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.uts.login.UserModel;

class Preferences {

    private static final String PREF_SESSION = "com.example.uts.login.session";

    private final static String REGISTER_USERNAME = "REGISTER_USERNAME";
    private final static String REGISTER_PASSWORD = "REGISTER_PASSWORD";
    private final static String LOGIN_STATUS = "LOGIN_STATUS";

    private Context context;

    public Preferences(Context context) {
        this.context = context;
    }

    public static void setUserPreferences(Context context, UserModel userModel) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_SESSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(REGISTER_USERNAME, userModel.getUsername());
        editor.putString(REGISTER_PASSWORD, userModel.getPassword());
        editor.apply();
    }

    public static String getRegisterPassword(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_SESSION, Context.MODE_PRIVATE);
        return preferences.getString(REGISTER_PASSWORD, UtilStatic.DEFAULT_STRING);
    }

    public static void setLoginStatus(Context context, boolean statusLogin) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_SESSION, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putBoolean(LOGIN_STATUS, statusLogin);
        editor.apply();
    }

    public  static boolean getLoggedInStatus(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_SESSION, Context.MODE_PRIVATE);
        return preferences.getBoolean(LOGIN_STATUS, UtilStatic.DEFAULT_BOOLEAN);
    }

    public  static void setLogout(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_SESSION, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(LOGIN_STATUS);
        editor.apply();
    }
}
