package com.ideahunters.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.ideahunters.api.ApiClient;
import com.ideahunters.model.UserRegister;
import com.ideahunters.utils.Constants;
import com.ideahunters.utils.Singleton;

import java.net.URLEncoder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by root on 3/2/17.
 */

public class RegisterPresenter implements Constants {

    private final Context context;
    private final RegisterPresenterListener mListener;
    private final ApiClient apiClient;
    private String device_type,status;

    public interface RegisterPresenterListener {


        void onSuccessfulRegistration();


        void showEmailIdError();

        void showPasswordError();

        void showUserNameError();

        void showComanyNameError();

        void showPhoneNumberError();

        void showRepeatPasswordError();

        void showPasswordNotMatchError();

        void showPasswordLengthError();

        void showEmpCodeBlankError();
    }


    public RegisterPresenter(RegisterPresenterListener listener, Context context) {
        this.mListener = listener;
        this.context = context;
        this.apiClient = new ApiClient();
    }


    public void registerUser(String username, String email1, String password, String repeatpassword, String phone, String company, String android_id, String token,String emp_code) {
       device_type="1";
        status="0";
        if (onValidate(username,email1, password,repeatpassword,phone,company,emp_code)) {
            Singleton.getInstance().showProgressDialog(context);
            apiClient
                    .createService(BASE_URL)
                    .registerUser(username,email1,password,phone, company,android_id,token,status,device_type,emp_code)
                    .enqueue(new Callback<UserRegister>() {
                        @Override
                        public void onResponse(Call<UserRegister> call, Response<UserRegister> response) {
                            Log.e("response", "success");
                            Log.e("response", ":: " + response.raw());
//                        Toast.makeText(context, response.body().getMessages().toString(), Toast.LENGTH_SHORT).show();
                            // RestResponse result = response.body();

                            if (response.body().getStatus().equals("true")) {
                                Toast.makeText(context, response.body().getMessage().toString(), Toast.LENGTH_SHORT).show();

                                mListener.onSuccessfulRegistration();


                          /* sliderArrayList.clear();
                            bannerArrayList.clear();
                            pageArrayList.clear();
                            for (int i = 0; i < response.body().getResponse().getSlider().size(); i++) {
                              Slider slider = new Slider();
                                slider.setName(response.body().getResponse().getSlider().get(i).getName());
                                slider.setUrl(response.body().getResponse().getSlider().get(i).getUrl());
                                url_maps.put(slider.getName(),slider.getUrl());
                              //  sliderArrayList.add(slider);

                            }

                            mListener.setSliderImages(url_maps);



                               Banner banner = new Banner();
                                banner.setName(response.body().getResponse().getBanner().getName());
                                banner.setUrl(response.body().getResponse().getBanner().getUrl());
                                mListener.setBannerImage(banner);

                            for (int i = 0; i < response.body().getResponse().getPage().size(); i++) {
                                Page page = new Page();
                                page.setName(response.body().getResponse().getPage().get(i).getName());
                              page.setUrl(response.body().getResponse().getPage().get(i).getUrl());

                                pageArrayList.add(page);
                            }

                            mListener.setPageImages(pageArrayList);*/


                            }
                            else {
                                Toast.makeText(context, response.body().getMessage().toString(), Toast.LENGTH_SHORT).show();

                            }
                            Singleton.getInstance().dismissDialog();

                        }

                        @Override
                        public void onFailure(Call<UserRegister> call, Throwable t) {
                            Log.e("response", "failure");
                            Singleton.getInstance().dismissDialog();
                        }

                    });
        }

    }


    private boolean onValidate(String username, String email1, String password, String repeatpassword, String phone, String company, String emp_code) {
      if (username.isEmpty()) {
            mListener.showUserNameError();
            return false;
        }

        else if (email1.isEmpty()|| !isValidEmail(email1)) {
            mListener.showEmailIdError();
            return false;
        }
        else if (password.isEmpty()) {
            mListener.showPasswordError();
            return false;
        }
        else if(password.length()<4)
        {
          mListener.showPasswordLengthError();
            return false;
        }
        else if (repeatpassword.isEmpty()) {
            mListener.showRepeatPasswordError();
            return false;
        }
        else if(!TextUtils.equals(password,repeatpassword))
        {
            mListener.showPasswordNotMatchError();
            return false;
        }
        else if (phone.isEmpty()) {
            mListener.showPhoneNumberError();
            return false;
        }

        else if (company.isEmpty()) {
            mListener.showComanyNameError();
            return false;
        }
        else if(TextUtils.isEmpty(emp_code))
      {
          mListener.showEmpCodeBlankError();
          return false;
      }

        return true;
    }
    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}