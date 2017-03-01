package com.ideahunters.application;

import android.app.Application;
import android.content.Context;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.ideahunters.BuildConfig;
import com.ideahunters.R;
import com.ideahunters.interfaces.ConnectivityReceiveListener;
import com.ideahunters.receiver.ConnectionReceiver;

import java.util.concurrent.TimeUnit;

import io.fabric.sdk.android.Fabric;


/**
 * Created by root on 8/2/17.
 */

public class IdeaHuntersApplication extends MultiDexApplication {


    private GoogleAnalytics analytics;
    private  static Tracker tracker;
    public static Boolean connection = false;
    private static IdeaHuntersApplication mInstance;


    private ConnectionReceiver receiver;
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        MultiDex.install(this);
        mInstance = this;
        receiver = new ConnectionReceiver();
     //   SystemClock.sleep(TimeUnit.SECONDS.toMillis(5));
        analytics = GoogleAnalytics.getInstance(this);
        tracker = analytics.newTracker(" UA-91939245-1");
        tracker.enableExceptionReporting(true);
        tracker.enableAdvertisingIdCollection(true);
        tracker.enableAutoActivityTracking(true);
       /* Thread.setDefaultUncaughtExceptionHandler (new Thread.UncaughtExceptionHandler()
        {
            @Override
            public void uncaughtException (Thread thread, Throwable e)
            {
                *//** Check whether it is development or release mode*//*
                if(BuildConfig.REPORT_CRASH.equals("true"))
                {
                    FirebaseCrash.report( e);
                }
            }
        });*/

    }

    public static synchronized IdeaHuntersApplication getInstance() {
        return mInstance;
    }

    public static Tracker tracker() {
        return tracker;
    }

    public void setConnectivityListener(ConnectivityReceiveListener listener) {
        ConnectionReceiver.connectivityReceiverListener = listener;
    }

    public void checkConnection(@NonNull Context context) {
        receiver.checkInternetConnection(context);
    }


}
