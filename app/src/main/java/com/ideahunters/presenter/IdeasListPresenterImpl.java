package com.ideahunters.presenter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ideahunters.R;
import com.ideahunters.api.ApiClient;
import com.ideahunters.model.CategoryData;
import com.ideahunters.model.Comment;
import com.ideahunters.model.IdeasListModel;
import com.ideahunters.model.IdeaslistData;
import com.ideahunters.model.UserData;
import com.ideahunters.model.UserLoginModel;
import com.ideahunters.utils.Constants;
import com.ideahunters.utils.Singleton;
import com.ideahunters.utils.Utilities;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by root on 20/12/16.
 */

public class IdeasListPresenterImpl implements Constants {


        private final Context context;
        private final IdeasListPresenterListener mListener;
        private final ApiClient apiClient;
    private ArrayList<IdeaslistData> IdeasList=new ArrayList<>() ;
    private ArrayList<Comment> CommentList=new ArrayList<>() ;
    int i;

    public interface IdeasListPresenterListener {


            void setIdeasList(ArrayList<IdeaslistData> ideasList);


    }

        public IdeasListPresenterImpl(IdeasListPresenterListener listener, Context context) {
        this.mListener = listener;
        this.context = context;
        this.apiClient = new ApiClient() ;
    }

    public void getIdeasList(final int i, boolean showProgress)
    {
        this.i=i;

        String MAIN_URL,PREFIX_URL;
      /*  if(showProgress)
        {
           Singleton.getInstance().showProgressDialog(context);
        }*/

      /*  if(myList)
        {
            PREFIX_URL=MY_IDEA;
        }
        else
        {*/

        //}

        if (i==2)
            PREFIX_URL =MOST_LIKED_IDEA;
        else if (i==1)
            PREFIX_URL=MY_IDEA;
        else if(i==3)
            PREFIX_URL=LIKED_BY_ME;
            else
        {
            PREFIX_URL =GET_ALL_IDEA;
        }





      MAIN_URL=PREFIX_URL+USERID+Singleton.getInstance().getValue(context,USER_ID)+"&"+COMPANYID+Singleton.getInstance().getValue(context,COMPANY_ID);
            apiClient
                    .createService(BASE_URL)
                    .IdeasListService(MAIN_URL)
                    .enqueue(new Callback<IdeasListModel>() {
                        @Override
                        public void onResponse(Call<IdeasListModel> call, Response<IdeasListModel> response) {
                            Singleton.getInstance().count++;
                            Log.e("response", "success");
                            Log.e("count", String.valueOf(Singleton.getInstance().count));
                            Log.e("response", ":: " + response.raw());
//                        Toast.makeText(context, ideasList.getMessages().toString(), Toast.LENGTH_SHORT).show();
                            // RestResponse result = ideasList;

                                Gson gson=new Gson();
                            String data=gson.toJson(response.body());
                           if(i==1)
                           {
                                Singleton.getInstance().saveValue(context,MY_IDEA_LIST,data);

                            }
                           else if (i==2){
                               Singleton.getInstance().saveValue(context,MOST_LIKED_IDEA_LIST,data);
                           }
                           else if (i==3){
                               Singleton.getInstance().saveValue(context,LIKED_BY_ME_LIST,data);
                           }
                           else {
                              Singleton.getInstance().saveValue(context, IDEA_LIST, data);
                            }

                         //  IdeasListModel mResponseObject = new Gson().toJson(response.body(),IdeasListModel.class);
                            setData(response.body());
                        /* Type collectionType = new TypeToken<Collection<IdeasListModel>>(){}.getType();
                            Collection<IdeasListModel> ideasList = new Gson().toJson(response.body().toString(), collectionType);
                             */
                          // setData((IdeasListModel) ideasList);
                            /*} else {
                                new AlertDialog.Builder(context)
                                        .setIcon(R.drawable.ic_warning_black_24dp)
                                        .setTitle("Ideas List")
                                        .setMessage("No Idea Found!")
                                        .setNeutralButton("Ok", new DialogInterface.OnClickListener()
                                        {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                              dialog.dismiss();


                                            }

                                        })

                                        .show();
                            }*/


                        }
                        //   if(result != null)
                        //         mListener.countriesReady(ideasList.getResult());
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

    public void setData(IdeasListModel data) {
        if (data.getStatus().equals("true")) {
            IdeasList.clear();

            for (int i = 0; i < data.getData().size(); i++) {
                IdeaslistData ideasList = new IdeaslistData();
                ideasList.setId(data.getData().get(i).getId());
                ideasList.setImage(data.getData().get(i).getImage());
                ideasList.setCatId(data.getData().get(i).getCatId());
                ideasList.setCompanyId(data.getData().get(i).getCompanyId());
                ideasList.setIdeaTitle(data.getData().get(i).getIdeaTitle());
                ideasList.setExplainIdea(data.getData().get(i).getExplainIdea());
                ideasList.setImage(data.getData().get(i).getImage());
                ideasList.setSubcatId(data.getData().get(i).getSubcatId());
                ideasList.setUserId(data.getData().get(i).getUserId());
                ideasList.setKeyResult(data.getData().get(i).getKeyResult());
                ideasList.setSubmittedDate(data.getData().get(i).getSubmittedDate());
                ideasList.setLikes(data.getData().get(i).getLikes());
                ideasList.setLikesCount(data.getData().get(i).getLikesCount());
                ideasList.setPostedBy(data.getData().get(i).getPostedBy());
                ideasList.setReport(data.getData().get(i).getReport());
                IdeasList.add(ideasList);

            }
            if(i==0)
            {
                Singleton.getInstance().filterideaList=IdeasList;
            }
            else
            if(i==1)
            {
                Singleton.getInstance().filterideaList1=IdeasList;

            }
            else
            if(i==2)
            {
                Singleton.getInstance().filterideaList2=IdeasList;

            }

            else
            if(i==3)
            {
                Singleton.getInstance().filterideaList3=IdeasList;

            }


            mListener.setIdeasList(IdeasList);
        }
    }

}
