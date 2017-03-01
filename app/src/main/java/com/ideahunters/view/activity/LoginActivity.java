package com.ideahunters.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.ideahunters.R;
import com.ideahunters.application.IdeaHuntersApplication;
import com.ideahunters.interfaces.ConnectivityReceiveListener;
import com.ideahunters.presenter.UserLoginPresenterImpl;
import com.ideahunters.utils.Singleton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by root on 2/2/17.
 */
public class LoginActivity extends AppCompatActivity implements UserLoginPresenterImpl.UserLoginPresenterListener, ConnectivityReceiveListener {

    @BindView(R.id.input_password)
    EditText inputPassword;
    @BindView(R.id.input_layout_password)
    TextInputLayout inputLayoutPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.signup_now)
    TextView signupNow;
    @BindView(R.id.input_email)
    EditText inputEmail;
    private UserLoginPresenterImpl userLoginPresenterImpl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!IdeaHuntersApplication.connection)
            IdeaHuntersApplication.getInstance().checkConnection(this);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        IdeaHuntersApplication.tracker().send(new HitBuilders.EventBuilder("ui", "open")
                .setLabel("Login Activity")
                .build());
        userLoginPresenterImpl = new UserLoginPresenterImpl(this, this);

    }

    @OnClick({R.id.btn_login, R.id.signup_now})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_login:
                if (!Singleton.getInstance().isConnectingToInternet(getApplicationContext())) {
                    showSnackAlert(inputEmail, getString(R.string.dialog_message_no_internet));

                }
                else {
                    userLoginPresenterImpl.onSubmit(inputEmail.getText().toString().trim(), inputPassword.getText().toString().trim());

                }
                break;

            case R.id.signup_now:
                Intent intent1 = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent1);
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        IdeaHuntersApplication.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onLoginSuccess() {
      getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
        finish();
    }

    @Override
    public void showEmailError() {
        inputEmail.setError(getResources().getString(R.string.err_msg_email));
        inputEmail.requestFocus();
    }

    @Override
    public void showPasswordError() {
        inputPassword.setError(getResources().getString(R.string.err_msg_password));
        inputPassword.requestFocus();

    }

    private void showSnackAlert(@NonNull View view, @NonNull String message) {
        Snackbar snack = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        ViewGroup group = (ViewGroup) snack.getView();
        group.setBackgroundColor(Color.RED);
        TextView tv = (TextView) group.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        snack.show();
    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        getConnection(isConnected);
        IdeaHuntersApplication.connection = isConnected;
    }

    private void getConnection(Boolean isConnected) {
        if (isConnected)
            showSnackAlert(inputEmail, getString(R.string.dialog_message_internet));
        else
            showSnackAlert(inputEmail, getString(R.string.dialog_message_no_internet));
    }


}
