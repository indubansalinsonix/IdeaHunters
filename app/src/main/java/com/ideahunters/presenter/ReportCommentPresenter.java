package com.ideahunters.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ideahunters.api.ApiClient;
import com.ideahunters.model.Likes;
import com.ideahunters.model.UserRegister;
import com.ideahunters.utils.Constants;
import com.ideahunters.utils.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by root on 20/2/17.
 */

public class ReportCommentPresenter implements Constants {
    private final Context context;
    private final ReportCommentPresenterListener mListener;
    private final ApiClient apiClient;
    private String device_type, status;
    private String image_name;
    private String file;

    public interface ReportCommentPresenterListener {


        void onSuccessfulReport(String message, TextView report_spam_comment, ProgressBar progressBar);

        void onUnSuccessfulReport(String message, TextView report_spam_comment, ProgressBar progressBar);
    }


    public ReportCommentPresenter(ReportCommentPresenterListener listener, Context context) {
        this.mListener = listener;
        this.context = context;
        this.apiClient = new ApiClient();
    }


    public void reportComment(String sug_id, String comment_id, final TextView report_spam_comment, final ProgressBar progressBar) {

        //   if (onValidate(message)) {
        apiClient
                .createService(BASE_URL)
                .reportComment(Singleton.getInstance().getValue(context, USER_ID),sug_id,comment_id)
                .enqueue(new Callback<UserRegister>() {
                    @Override
                    public void onResponse(Call<UserRegister> call, Response<UserRegister> response) {
                        Log.e("response", "success");
                        Log.e("response", ":: " + response.raw());
//                        Toast.makeText(context, response.body().getMessages().toString(), Toast.LENGTH_SHORT).show();
                        // RestResponse result = response.body();

                        if (response.body().getStatus().equals("true")) {

                            mListener.onSuccessfulReport(response.body().getMessage(),report_spam_comment,progressBar);

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
                            mListener.onUnSuccessfulReport(response.body().getMessage().toString(),report_spam_comment,progressBar);
                        }

                    }

                    @Override
                    public void onFailure(Call<UserRegister> call, Throwable t) {
                        Log.e("response", "failure");
                    }

                });
        //   }

    }


}