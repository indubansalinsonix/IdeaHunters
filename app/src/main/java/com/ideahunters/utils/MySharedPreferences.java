package com.ideahunters.utils;

/**
 * Created by prashant on 17/10/16.
 */
import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreferences {
    private SharedPreferences prefs;
    private Context context;
    public static final String FIREBASE_CLOUD_MESSAGING = "fcm";
    public static final String SET_NOTIFY = "set_notify";
    public MySharedPreferences(Context context){
        this.context = context;
        prefs = context.getSharedPreferences(FIREBASE_CLOUD_MESSAGING, Context.MODE_PRIVATE);
    }
    public void saveNotificationSubscription(boolean value, String token){
        SharedPreferences.Editor edits = prefs.edit();
        edits.putBoolean(SET_NOTIFY, value);
        edits.putString("token",token);
        edits.apply();
    }
    public  boolean hasUserSubscribeToNotification(){
        return prefs.getBoolean(SET_NOTIFY, false);
    }
    public  String getToken(){
        return prefs.getString("token","");
    }
    public void clearAllSubscriptions(){
        prefs.edit().clear().apply();
    }

}