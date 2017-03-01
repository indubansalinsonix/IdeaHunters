package com.ideahunters.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.ideahunters.api.ApiClient;
import com.ideahunters.model.Comment;
import com.ideahunters.model.CommentListPojo;
import com.ideahunters.model.CompanyData;
import com.ideahunters.model.CompanyListPojo;
import com.ideahunters.utils.Constants;
import com.ideahunters.utils.Singleton;

import java.util.AbstractQueue;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by root on 7/2/17.
 */

public class CommentListPresenter  implements Constants {

    private final Context context;
    private final CommentListPresenterListener mListener;
    private final ApiClient apiClient;
    private AbstractQueue SliderImagesList;
    private ProgressDialog progressDialog;
    private ArrayList<Comment> CommentList=new ArrayList<>() ;

    public interface CommentListPresenterListener {






        void setCommentList(ArrayList<Comment> commentList);

        void onFailureResponse();
    }




    public CommentListPresenter(CommentListPresenterListener listener, Context context){
        this.mListener = listener;
        this.context = context;
        this.apiClient = new ApiClient() ;
    }



    public void getCommentsList(String sug_id) {
       /* if() {
            progressDialog = new ProgressDialog(context);

            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }*/
        apiClient
                .createService(BASE_URL)
                .CommentListService(sug_id, Singleton.getInstance().getValue(context,USER_ID))
                .enqueue(new Callback<CommentListPojo>() {
                    @Override
                    public void onResponse(Call<CommentListPojo> call, Response<CommentListPojo> response) {
                        Log.e("response", "success");
                        Log.e("response", ":: " + response.raw());
//                        Toast.makeText(context, response.body().getMessages().toString(), Toast.LENGTH_SHORT).show();
                        // RestResponse result = response.body();

                        if (response.body().getStatus().equals("true")) {
                            CommentList.clear();
                            for (int j = 0; j < response.body().getData().size(); j++) {

                                Comment List=new Comment();
                                List.setComments(response.body().getData().get(j).getComments());
                                List.setCreatedAt(response.body().getData().get(j).getCreatedAt());
                                List.setSubmittedBy(response.body().getData().get(j).getSubmittedBy());
                                List.setCommentId(response.body().getData().get(j).getCommentId());
                                List.setReport(response.body().getData().get(j).getReport());
                                List.setCommentedBy(response.body().getData().get(j).getCommentedBy());
                                CommentList.add(List);

                            }
                            mListener.setCommentList(CommentList);
                          //  progressDialog.dismiss();
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

                    }

                    @Override
                    public void onFailure(Call<CommentListPojo> call, Throwable t) {
                        Log.e("response", "failure");
                        mListener.onFailureResponse();
                       // progressDialog.dismiss();
                    }

                });
    }



}
