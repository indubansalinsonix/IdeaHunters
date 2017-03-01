package com.ideahunters.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.ideahunters.api.ApiClient;
import com.ideahunters.model.UserRegister;
import com.ideahunters.utils.Constants;
import com.ideahunters.utils.Singleton;

import java.io.ByteArrayOutputStream;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by root on 7/2/17.
 */

public class PostCommentPresenter  implements Constants {
    private final Context context;
    private final PostCommentPresenterListener mListener;
    private final ApiClient apiClient;
    private String device_type, status;
    private String image_name;
    private String file;

    public interface PostCommentPresenterListener {


        void onSuccessfulPost();

        void showBlankCommentError();


    }


    public PostCommentPresenter(PostCommentPresenterListener listener, Context context) {
        this.mListener = listener;
        this.context = context;
        this.apiClient = new ApiClient();
    }


    public void postComment(String sug_id, String message) {

        if (onValidate(message)) {
            Singleton.getInstance().showProgressDialog(context);
            apiClient
                    .createService(BASE_URL)
                    .postComment(Singleton.getInstance().getValue(context, USER_ID),sug_id,message)
                    .enqueue(new Callback<UserRegister>() {
                        @Override
                        public void onResponse(Call<UserRegister> call, Response<UserRegister> response) {
                            Log.e("response", "success");
                            Log.e("response", ":: " + response.raw());
//                        Toast.makeText(context, response.body().getMessages().toString(), Toast.LENGTH_SHORT).show();
                            // RestResponse result = response.body();

                            if (response.body().getStatus().equals("true")) {

                                mListener.onSuccessfulPost();

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


                            } else {
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

    private boolean onValidate(String message) {

        if (TextUtils.isEmpty(message)) {
            mListener.showBlankCommentError();
            return false;
        }
        return true;
    }
}
