package com.ideahunters.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;


import com.ideahunters.api.ApiClient;
import com.ideahunters.model.CompanyData;
import com.ideahunters.model.UserData;
import com.ideahunters.model.UserLoginModel;
import com.ideahunters.utils.Constants;
import com.ideahunters.utils.Singleton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by root on 20/12/16.
 */

public class UserLoginPresenterImpl implements Constants {


        private final Context context;
        private final UserLoginPresenterListener mListener;
        private final ApiClient apiClient;
    private static final ArrayList<UserData> UserDataList = new ArrayList<>();

    public interface UserLoginPresenterListener {


            void onLoginSuccess();

            void showEmailError();

            void showPasswordError();
        }

        public UserLoginPresenterImpl(UserLoginPresenterListener listener, Context context) {
        this.mListener = listener;
        this.context = context;
        this.apiClient = new ApiClient() ;
    }

    public void onSubmit(String email, String password)
    {
        if (onValidate(email, password)) {

            Singleton.getInstance().showProgressDialog(context);
            apiClient
                    .createService(BASE_URL)
                    .UserLoginService(email, password)
                    .enqueue(new Callback<UserLoginModel>() {
                        @Override
                        public void onResponse(Call<UserLoginModel> call, Response<UserLoginModel> response) {
                            Log.e("response", "success");
                            Log.e("response", ":: " + response.raw());
//                        Toast.makeText(context, response.body().getMessages().toString(), Toast.LENGTH_SHORT).show();
                            // RestResponse result = response.body();

                            if (response.body().getStatus().equals("true")) {
                                for (int i = 0; i < response.body().getUserdata().size(); i++) {

                                    Singleton.getInstance().saveValue(context, USER_ID, response.body().getUserdata().get(i).getId().toString());
                                    Singleton.getInstance().saveValue(context,USER_NAME,response.body().getUserdata().get(i).getName().toString());
                                    Singleton.getInstance().saveValue(context, USER_EMAIL, response.body().getUserdata().get(i).getEmail().toString());
                                    Singleton.getInstance().saveValue(context, LOGO, response.body().getUserdata().get(i).getCompany().getLogo().toString());
                                    Singleton.getInstance().saveValue(context, COMPANY_ID, response.body().getUserdata().get(i).getCompany().getId().toString());
                                    Singleton.getInstance().saveValue(context, COMPANY_NAME, response.body().getUserdata().get(i).getCompany().getCompany().toString());
                                    Singleton.getInstance().saveValue(context, ADMIN_ID, response.body().getUserdata().get(i).getCompany().getAdminId().toString());




                                }


                                mListener.onLoginSuccess();
                            } else {
                                Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                            }


                            Singleton.getInstance().dismissDialog();
                        }
                        //   if(result != null)
                        //         mListener.countriesReady(response.body().getResult());
                        // Log.e("results",result.getResult().toString());
                        //   }

                        @Override
                        public void onFailure(Call<UserLoginModel> call, Throwable t) {
                            Log.e("response", "failure");
                            Singleton.getInstance().dismissDialog();
                            try {
                                throw new InterruptedException("Erro na comunicação com o servidor!");
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });


        }


    }

    private boolean onValidate(String email, String password) {

        if (email.isEmpty()||!isValidEmail(email)) {
            mListener.showEmailError();
            return false;
        }
        else if (password.isEmpty()) {
            mListener.showPasswordError();
            return false;
        }



        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


}
