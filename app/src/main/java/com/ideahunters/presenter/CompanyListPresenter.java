package com.ideahunters.presenter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;


import com.ideahunters.api.ApiClient;
import com.ideahunters.model.CompanyData;
import com.ideahunters.model.CompanyListPojo;
import com.ideahunters.utils.Constants;
import com.ideahunters.utils.Singleton;

import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by root on 14/12/16.
 */
public class CompanyListPresenter implements Constants {

    private final Context context;
    private final ListImagesPresenterListener mListener;
    private final ApiClient apiClient;
    private AbstractQueue SliderImagesList;
    private static final ArrayList<CompanyData> companyList = new ArrayList<>();
    private ProgressDialog progressDialog;

    public interface ListImagesPresenterListener {




        void setcompanyList(ArrayList<CompanyData> sliderArrayList);


        void setCompanyListBlankMessage();
    }




    public CompanyListPresenter(ListImagesPresenterListener listener, Context context){
        this.mListener = listener;
        this.context = context;
        this.apiClient = new ApiClient() ;
    }



    public void getListImages(boolean b) {
        if(b) {
            Singleton.getInstance().showProgressDialog(context);
        }
        apiClient
                .createService(BASE_URL)
                .CompanyListService()
                .enqueue(new Callback<CompanyListPojo>() {
                    @Override
                    public void onResponse(Call<CompanyListPojo> call, Response<CompanyListPojo> response) {
                        Log.e("response", "success");
                        Log.e("response", ":: " + response.raw());
//                        Toast.makeText(context, response.body().getMessages().toString(), Toast.LENGTH_SHORT).show();
                        // RestResponse result = response.body();

                        if (response.body().getStatus().equals("true")) {
                            companyList.clear();
                            for (int i = 0; i < response.body().getData().size(); i++) {
                             CompanyData data = new CompanyData();
                                data.setCompany(response.body().getData().get(i).getCompany());
                                data.setId(response.body().getData().get(i).getId());
                                data.setCodeName(response.body().getData().get(i).getCodeName());

                               companyList.add(data);

                            }

                            mListener.setcompanyList(companyList);
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
                        else
                        {
                            mListener.setCompanyListBlankMessage();
                        }
                        Singleton.getInstance().dismissDialog();

                    }

                    @Override
                    public void onFailure(Call<CompanyListPojo> call, Throwable t) {
                        Log.e("response", "failure");
                        Singleton.getInstance().dismissDialog();
                    }

                });
    }



}
