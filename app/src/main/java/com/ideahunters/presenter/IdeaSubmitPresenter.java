package com.ideahunters.presenter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.ideahunters.R;
import com.ideahunters.api.ApiClient;
import com.ideahunters.api.ApiEndpointInterface;
import com.ideahunters.model.UserRegister;
import com.ideahunters.utils.Constants;
import com.ideahunters.utils.Singleton;
import com.ideahunters.view.fragment.IdeaSubmitFragment;

import java.io.ByteArrayOutputStream;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by root on 6/2/17.
 */
public class IdeaSubmitPresenter implements Constants {
    private final Context context;
    private final IdeaSubmitPresenterListener mListener;
    private final ApiClient apiClient;
    private String device_type,status;
    private String image_name;
    private String file;

    public interface IdeaSubmitPresenterListener {


        void onSuccessfulSubmit();




        void showTitleBlankError();

        void showCategoryBlankError(String category_name);

        void showIdeaSubmitBlankError();

        void showKeyResultBlankError();
    }


    public IdeaSubmitPresenter(IdeaSubmitPresenterListener listener, Context context) {
        this.mListener = listener;
        this.context = context;
        this.apiClient = new ApiClient();
    }


    public void submitIdea(String category_name, String subcategory_name, String idea_submit, String idea_title, String key_result, Bitmap thumbnail, boolean UPDATE_IDEA, String sug_id, boolean b) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (thumbnail != null) {
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte data[] = byteArrayOutputStream.toByteArray();
            file = Base64.encodeToString(data, Base64.DEFAULT);
            if (!TextUtils.isEmpty(file)) {
                image_name = new Date().getTime() + ".png";
            }
        }
            if(b){
            Singleton.getInstance().showProgressDialog(context);}
            Call<UserRegister> originalCall;
        if(UPDATE_IDEA){
            originalCall =  apiClient
                    .createService(BASE_URL).updateIdea(Singleton.getInstance().getValue(context,USER_ID),idea_title,category_name,subcategory_name, idea_submit,key_result,Singleton.getInstance().getValue(context,COMPANY_ID),image_name,file,sug_id);

        }
            else
        {
            originalCall =  apiClient
                    .createService(BASE_URL).submitIdea1(Singleton.getInstance().getValue(context,USER_ID),idea_title,category_name,subcategory_name, idea_submit,key_result,Singleton.getInstance().getValue(context,COMPANY_ID),image_name,file);

        }
            originalCall.enqueue(new Callback<UserRegister>() {
                        @Override
                        public void onResponse(Call<UserRegister> call, Response<UserRegister> response) {
                            Log.e("response", "success");
//                            Log.e("update", ":: " + response.body().getMessage());
//                        Toast.makeText(context, response.body().getMessages().toString(), Toast.LENGTH_SHORT).show();
                            // RestResponse result = response.body();

                            if (response.body().getStatus().equals("true")) {
                                if(!TextUtils.isEmpty(Singleton.getInstance().getValue(context,OFFLINE_STORE_IDEA)))
                                {
                                    Singleton.getInstance().saveValue(context,OFFLINE_STORE_IDEA,"");
                                    Toast.makeText(context, "offline post", Toast.LENGTH_SHORT).show();

                                }
                                else {
                                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    mListener.onSuccessfulSubmit();
                                }
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
                                if(!TextUtils.isEmpty(Singleton.getInstance().getValue(context,OFFLINE_STORE_IDEA)))
                                {
                                    Singleton.getInstance().saveValue(context,OFFLINE_STORE_IDEA,"");
                                }
                                else {
                                    Toast.makeText(context, response.body().getMessage().toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                            Singleton.getInstance().dismissDialog();

                        }

                        @Override
                        public void onFailure(Call<UserRegister> call, Throwable t) {
                            Log.e("udate_response", "failure");
                            Log.e("error",t.getMessage());
                            Singleton.getInstance().dismissDialog();
                        }

                    });


    }

    public boolean onValidate(String category_name, String subcategory_name, String idea_submit, String idea_title, String key_result) {

      if(TextUtils.isEmpty(idea_title)){
          mListener.showTitleBlankError();
          return false;
          /*  title.setError("Title is required!");
            input_layout_name.requestFocus();*/
        } else if (TextUtils.isEmpty(category_name)) {
          mListener.showCategoryBlankError(category_name);
          return false;
            /*first_name.setError("First name is required!");
            first_name.requestFocus();*/
        }
      else if (category_name.equals(context.getResources().getString(R.string.no_category_added)))
      {
          mListener.showCategoryBlankError(category_name);
      }
      else if (TextUtils.isEmpty(idea_submit)) {
          mListener.showIdeaSubmitBlankError();
          return false;
        }
        else if(TextUtils.isEmpty(key_result))
      {
          mListener.showKeyResultBlankError();
          return false;
      }

        return true;
    }




}
