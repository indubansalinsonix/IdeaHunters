package com.ideahunters.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ideahunters.R;
import com.ideahunters.adapter.CompanyListAdapter;
import com.ideahunters.application.IdeaHuntersApplication;
import com.ideahunters.customwidgets.MaterialBetterSpinner.MaterialBetterSpinner;
import com.ideahunters.interfaces.ConnectivityReceiveListener;
import com.ideahunters.model.CompanyData;
import com.ideahunters.presenter.CompanyListPresenter;
import com.ideahunters.presenter.RegisterPresenter;
import com.ideahunters.utils.MySharedPreferences;
import com.ideahunters.utils.Singleton;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by root on 2/2/17.
 */
public class SignUpActivity extends AppCompatActivity implements CompanyListPresenter.ListImagesPresenterListener, RegisterPresenter.RegisterPresenterListener,ConnectivityReceiveListener {


    @BindView(R.id.input_name)
    EditText inputName;
    @BindView(R.id.input_layout_name)
    TextInputLayout inputLayoutName;
    @BindView(R.id.input_email)
    EditText inputEmail;
    @BindView(R.id.input_layout_email)
    TextInputLayout inputLayoutEmail;
    @BindView(R.id.input_password)
    EditText inputPassword;
    @BindView(R.id.input_layout_password)
    TextInputLayout inputLayoutPassword;
    @BindView(R.id.input_repeatpassword)
    EditText inputRepeatpassword;
    @BindView(R.id.input_layout_repeatpassword)
    TextInputLayout inputLayoutRepeatpassword;
    @BindView(R.id.input_Phone)
    EditText inputPhone;
    @BindView(R.id.input_layout_phone_no)
    TextInputLayout inputLayoutPhoneNo;
    @BindView(R.id.btn_signup)
    Button btnSignup;
    @BindView(R.id.company_spinner)
    MaterialBetterSpinner companySpinner;
    @BindView(R.id.input_layout_empCode)
    TextInputLayout inputLayoutEmpCode;
    @BindView(R.id.input_emp_code)
    EditText inputEmpCode;
    private CompanyListPresenter comapnyListPresenter;
    private ProgressDialog progressDialog;
    private String company_name = "";
    private RegisterPresenter registerPresenter;
    private MySharedPreferences mySharedPreference;
    CompanyListAdapter companyListAdapter;
    ArrayList<CompanyData> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!IdeaHuntersApplication.connection)
            IdeaHuntersApplication.getInstance().checkConnection(this);

        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        list = new ArrayList<>();
        if (!IdeaHuntersApplication.connection) {
            showSnackAlert(inputName, getString(R.string.dialog_message_no_internet));
        }
        else {
            comapnyListPresenter = new CompanyListPresenter(this, this);
            comapnyListPresenter.getListImages(true);
        }
        //companySpinner.setMetHintTextColor(android.R.color.darker_gray);
        companySpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(company_name)) {
                    companySpinner.setEnabled(true);
                } else if (company_name.equals(getString(R.string.no_company_added))) {
                    Toast.makeText(SignUpActivity.this, getString(R.string.no_company_added), Toast.LENGTH_LONG).show();
                }

            }
        });
        companySpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {

                    CompanyData data = (CompanyData) companySpinner.getAdapter().getItem(i);
                    company_name = data.getCompany();
                    if (!TextUtils.isEmpty(data.getCodeName()))
                        inputLayoutEmpCode.setHint(data.getCodeName());
                    inputLayoutEmpCode.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();
       IdeaHuntersApplication.getInstance().setConnectivityListener(this);

    }

    @OnClick({R.id.btn_signup})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_signup:
                if (!IdeaHuntersApplication.connection) {
                   showSnackAlert(inputName, getString(R.string.dialog_message_no_internet));
                }
                    else {
                    registerPresenter = new RegisterPresenter(this, this);
                    String username = inputName.getText().toString().trim();
                    String email = inputEmail.getText().toString().trim();
                    String password = inputPassword.getText().toString().trim();
                    String repeatpassword = inputRepeatpassword.getText().toString().trim();
                    String phone = inputPhone.getText().toString().trim();
                    String android_id = Settings.Secure.getString(getContentResolver(),
                            Settings.Secure.ANDROID_ID);
                    mySharedPreference = new MySharedPreferences(getApplicationContext());
                    String token = mySharedPreference.getToken();
                    String code_name = inputEmpCode.getText().toString().trim();
                    Log.e("token", token);
                    registerPresenter.registerUser(username, email, password, repeatpassword, phone, company_name, android_id, token, code_name);

                }
                break;
        }
    }


    @Override
    public void onSuccessfulRegistration() {
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    public void showEmailIdError() {
        inputEmail.setError(getResources().getString(R.string.err_msg_email));
        requestFocus(inputEmail);
        inputEmail.requestFocus();
        inputLayoutEmail.requestFocus();
    }

    @Override
    public void showPasswordError() {
        inputPassword.setError(getResources().getString(R.string.err_msg_password));
        requestFocus(inputPassword);
        inputPassword.requestFocus();
        inputLayoutPassword.requestFocus();
    }

    @Override
    public void showUserNameError() {
        inputName.setError(getResources().getString(R.string.err_msg_name));
        requestFocus(inputName);
        inputName.requestFocus();
        inputLayoutName.requestFocus();

    }

    @Override
    public void showComanyNameError() {
        if (TextUtils.isEmpty(company_name)) {
            companySpinner.setError(getResources().getString(R.string.err_msg_company));
            requestFocus(companySpinner);
            companySpinner.requestFocus();

        } else if (company_name.equals(getString(R.string.no_company_added))) {
            Toast.makeText(this, getString(R.string.no_company_added), Toast.LENGTH_LONG).show();
            companySpinner.requestFocus();


        }

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
    public void showPhoneNumberError() {
        inputPhone.setError(getResources().getString(R.string.err_msg_phone));
        requestFocus(inputPhone);
        inputPhone.requestFocus();
        inputLayoutPhoneNo.requestFocus();
    }

    @Override
    public void showRepeatPasswordError() {
        inputRepeatpassword.setError(getResources().getString(R.string.err_msg_repeat_password));
        inputRepeatpassword.requestFocus();
        inputLayoutRepeatpassword.requestFocus();
    }

    @Override
    public void showPasswordNotMatchError() {
        inputRepeatpassword.setError(getResources().getString(R.string.err_msg__mismatch_password));
        requestFocus(inputRepeatpassword);
        inputRepeatpassword.requestFocus();
        inputLayoutRepeatpassword.requestFocus();
    }

    @Override
    public void showPasswordLengthError() {
        inputPassword.setError(getResources().getString(R.string.err_msg_password_length));
        requestFocus(inputPassword);

        inputPassword.requestFocus();
        inputLayoutPassword.requestFocus();

    }

    @Override
    public void showEmpCodeBlankError() {
        inputEmpCode.setError(getResources().getString(R.string.err_msg_empcode_blank));
        requestFocus(inputEmpCode);

        inputEmpCode.requestFocus();
        inputLayoutEmpCode.requestFocus();
    }

    @Override
    public void setcompanyList(ArrayList<CompanyData> list) {
        if (list.size() > 0) {
            String[] values = new String[list.size()];
            String[] ids = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                values[i] = list.get(i).getCompany();
                ids[i] = list.get(i).getCodeName();
            }
            ArrayAdapter<CompanyData> adapter2 = new ArrayAdapter<CompanyData>(getApplicationContext(), R.layout.spinner_item, list);
            companySpinner.setAdapter(adapter2);

        }

    }

    @Override
    public void setCompanyListBlankMessage() {
        company_name = getString(R.string.no_company_added);
    }


   /* private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }*/


    public void setEmpCodeId(String codeName, String company) {
        Log.e("codename", codeName);
        inputLayoutEmpCode.setHint(codeName);
        company_name = company;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
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


