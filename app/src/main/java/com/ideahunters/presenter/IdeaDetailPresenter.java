package com.ideahunters.presenter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.ideahunters.R;
import com.ideahunters.api.ApiClient;
import com.ideahunters.model.Comment;
import com.ideahunters.model.IdeasListModel;
import com.ideahunters.model.IdeaslistData;
import com.ideahunters.utils.Constants;
import com.ideahunters.utils.Singleton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by root on 9/2/17.
 */

public class IdeaDetailPresenter  implements Constants {


    private final Context context;
    private final IdeaDetailListener mListener;
    private final ApiClient apiClient;
    private ArrayList<IdeaslistData> IdeasList=new ArrayList<>() ;

    public interface IdeaDetailListener {


        void setIdeaDetails(IdeaslistData ideasList);



    }

    public IdeaDetailPresenter (IdeaDetailListener listener, Context context) {
        this.mListener = listener;
        this.context = context;
        this.apiClient = new ApiClient() ;
    }

    public void getIdeaDetails(String sug_id)
    {


        Singleton.getInstance().showProgressDialog(context);
        apiClient
                .createService(BASE_URL)
                .IdeasDetailsService(sug_id)
                .enqueue(new Callback<IdeasListModel>() {
                    @Override
                    public void onResponse(Call<IdeasListModel> call, Response<IdeasListModel> response) {
                        Log.e("response", "success");
                        Log.e("response", ":: " + response.raw());
//                        Toast.makeText(context, response.body().getMessages().toString(), Toast.LENGTH_SHORT).show();
                        // RestResponse result = response.body();

                        if (response.body().getStatus().equals("true")) {
                            IdeasList.clear();
                            for (int i = 0; i < response.body().getData().size(); i++) {
                                IdeaslistData ideasList=new IdeaslistData();
                                ideasList.setId(response.body().getData().get(i).getId());
                                ideasList.setImage(response.body().getData().get(i).getImage());
                                ideasList.setCatId(response.body().getData().get(i).getCatId());
                                ideasList.setCompanyId(response.body().getData().get(i).getCompanyId());
                                ideasList.setIdeaTitle(response.body().getData().get(i).getIdeaTitle());
                                ideasList.setExplainIdea(response.body().getData().get(i).getExplainIdea());
                                ideasList.setImage(response.body().getData().get(i).getImage());
                                ideasList.setSubcatId(response.body().getData().get(i).getSubcatId());
                                ideasList.setUserId(response.body().getData().get(i).getUserId());
                                ideasList.setKeyResult(response.body().getData().get(i).getKeyResult());
                                ideasList.setSubmittedDate(response.body().getData().get(i).getSubmittedDate());
                                ideasList.setLikes(response.body().getData().get(i).getLikes());
                                ideasList.setLikesCount(response.body().getData().get(i).getLikesCount());
                                IdeasList.add(ideasList);

                                    /*for (int j = 0; j < response.body().getData().get(i).getComment().size(); j++) {

                                        Comment List=new Comment();
                                        List.setComments(response.body().getData().get(i).getComment().get(j).getComments());
                                        List.setCreatedAt(response.body().getData().get(i).getComment().get(j).getCreatedAt());
                                        List.setSubmittedBy(response.body().getData().get(i).getComment().get(j).getSubmittedBy());

                                       CommentList.add(List);

                                    }
                                    mListener.getCommentList(CommentList);*/
                                mListener.setIdeaDetails(ideasList);

                            }




                        }


                        Singleton.getInstance().dismissDialog();
                    }
                    //   if(result != null)
                    //         mListener.countriesReady(response.body().getResult());
                    // Log.e("results",result.getResult().toString());
                    //   }

                    @Override
                    public void onFailure(Call<IdeasListModel> call, Throwable t) {
                        Log.e("response", "failure");
                        Singleton.getInstance().dismissDialog();
                        try {
                            throw new InterruptedException(t.getMessage());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });


    }







}
