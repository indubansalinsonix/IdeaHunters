package com.ideahunters.presenter;

import android.content.Context;
import android.os.Handler;

import com.ideahunters.utils.Constants;


/**
 * Created by prashant on 1/12/16.
 */

public class SplashPresenter implements Constants {


    private final Context context;
    private final SplashPresenterListener mListener;

    public interface SplashPresenterListener {

        public void onDelayComplete();

    }

    public SplashPresenter(SplashPresenterListener listener, Context context) {
        this.mListener = listener;
        this.context = context;

    }

    public void runWaitThread(int timeUnits) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
               /* if(TextUtils.isEmpty(Singleton.getInstance().getValue(context, ID))) {*/
                    mListener.onDelayComplete();
               // }
            }
        }, timeUnits);
    }



}
