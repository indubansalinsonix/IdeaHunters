package com.ideahunters.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ideahunters.application.IdeaHuntersApplication;
import com.ideahunters.interfaces.ConnectivityReceiveListener;
import com.ideahunters.presenter.IdeaSubmitPresenter;
import com.ideahunters.presenter.PostSubmitData;
import com.ideahunters.utils.Singleton;

import static android.content.ContentValues.TAG;
import static com.ideahunters.utils.Constants.OFFLINE_STORE_IDEA;


public class ConnectionReceiver extends BroadcastReceiver implements IdeaSubmitPresenter.IdeaSubmitPresenterListener {
    public static ConnectivityReceiveListener connectivityReceiverListener;

    public ConnectionReceiver() {
        super();
    }

    @Override
    public void onReceive(@NonNull Context context, Intent intent) {
        Log.d("network", "Network connectivity change");

        if (intent.getExtras() != null) {
            final ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo ni = connectivityManager.getActiveNetworkInfo();

            if (ni != null && ni.isConnectedOrConnecting()) {
                Log.i(TAG, "Network " + ni.getTypeName() + " connected");
                if(!TextUtils.isEmpty(Singleton.getInstance().getValue(context,OFFLINE_STORE_IDEA)))
                {
                    IdeaSubmitPresenter ideaSubmitPresenter = new IdeaSubmitPresenter(this, context);
                    Gson gson = new Gson();
                    String json = Singleton.getInstance().getValue(context,OFFLINE_STORE_IDEA);
                    PostSubmitData data = gson.fromJson(json, PostSubmitData.class);

                    String category_name=data.getCategory_name();
                    String subcategory_name=data.getSubcategory_name();
                    String idea_submit=data.getIdea_submit();
                    String idea_title=data.getIdea_title();
                    String key_result=data.getKey_result();
                    Bitmap thumbnail=data.getThumbnail();
                    boolean UPDATE_IDEA=data.isUPDATE_IDEA();
                    String sug_id=data.getSug_id();
                    ideaSubmitPresenter.submitIdea(category_name, subcategory_name, idea_submit, idea_title, key_result, thumbnail, UPDATE_IDEA, sug_id,false);


                }

            } else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
                Log.d(TAG, "There's no network connectivity");
            }
        }
        checkInternetConnection(context);
    }

    private boolean isInternetConnected(@NonNull Context context) {
        if (isAirplaneModeOn()) {
            return false;
        } else {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            return info != null && info.isConnected();
        }
    }

    public void checkInternetConnection(@NonNull Context context) {
        if (connectivityReceiverListener != null) {
            connectivityReceiverListener.onNetworkConnectionChanged(isNetworkAvailable(context));
        }
    }

    private boolean isNetworkAvailable(@NonNull Context context) {
        return isInternetConnected(context);
    }


    @SuppressWarnings("deprecation")
    private boolean isAirplaneModeOn() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.System.getInt(IdeaHuntersApplication.getInstance().getContentResolver
                    (), Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
        } else {
            return Settings.System.getInt(IdeaHuntersApplication.getInstance().getContentResolver
                    (), Settings.System.AIRPLANE_MODE_ON, 0) != 0;
        }
    }

    @Override
    public void onSuccessfulSubmit() {

        Log.e("offline","success");
    }

    @Override
    public void showTitleBlankError() {

    }

    @Override
    public void showCategoryBlankError(String category_name) {

    }

    @Override
    public void showIdeaSubmitBlankError() {

    }

    @Override
    public void showKeyResultBlankError() {

    }
}
