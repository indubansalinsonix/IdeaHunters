package com.ideahunters.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ideahunters.R;
import com.ideahunters.application.IdeaHuntersApplication;
import com.ideahunters.interfaces.ConnectivityReceiveListener;
import com.ideahunters.presenter.SplashPresenter;
import com.ideahunters.utils.Constants;
import com.ideahunters.utils.Singleton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SplashScreen extends AppCompatActivity implements SplashPresenter.SplashPresenterListener, Constants, ConnectivityReceiveListener {


    @BindView(R.id.logo)
    ImageView logo;
    @BindView(R.id.lin_lay)
    LinearLayout linLay;
    @BindView(R.id.mainLayout)
    LinearLayout mainLayout;
    private SplashPresenter splashPresenter;


    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IdeaHuntersApplication.getInstance().checkConnection(this);
        setContentView(R.layout.splash);
        ButterKnife.bind(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        StartAnimations();


        splashPresenter = new SplashPresenter(this, this);
        splashPresenter.runWaitThread(3000);


    }

    @Override
    protected void onResume() {
        super.onResume();
        IdeaHuntersApplication.getInstance().setConnectivityListener(this);
    }


    private void StartAnimations() {
        Log.e("value", "anim");

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
        anim.reset();
        LinearLayout l = (LinearLayout) findViewById(R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.anim_translate);
        anim.reset();
        ImageView iv = (ImageView) findViewById(R.id.logo);
        iv.clearAnimation();
        iv.startAnimation(anim);


    }

    @Override
    public void onDelayComplete() {
        if (!TextUtils.isEmpty(Singleton.getInstance().getValue(getApplicationContext(), USER_ID))) {
            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected)
            showSnackAlert(mainLayout, getString(R.string.dialog_message_internet));
        else
            showSnackAlert(mainLayout, getString(R.string.dialog_message_no_internet));
    }

    private void showSnackAlert(@NonNull View view, @NonNull String message) {
        Snackbar snack = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        ViewGroup group = (ViewGroup) snack.getView();
        group.setBackgroundColor(Color.RED);
        TextView tv = (TextView) group.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        snack.show();
    }


}