package com.ideahunters.presenter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.gson.Gson;
import com.ideahunters.R;
import com.ideahunters.api.ApiClient;
import com.ideahunters.model.CategoryData;
import com.ideahunters.model.CategoryListPojo;
import com.ideahunters.model.Comment;
import com.ideahunters.model.IdeasListModel;
import com.ideahunters.model.IdeaslistData;
import com.ideahunters.model.Subcategory;
import com.ideahunters.utils.Constants;
import com.ideahunters.utils.Singleton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by root on 6/2/17.
 */

public class IdeaCategoryListPresenter implements Constants {


    private final Context context;
    private final IdeaCategoryListPresenterListener mListener;
    private final ApiClient apiClient;
    private ArrayList<CategoryData> CategoryList=new ArrayList<>() ;
    private ArrayList<Subcategory> SubCategoryList=new ArrayList<>() ;

    public interface IdeaCategoryListPresenterListener {

        void setCategoryList(ArrayList<CategoryData> categoryList);

        void setCategoryListBlankMessage();

        // void setSubCategoryList(ArrayList<Subcategory> subCategoryList);
    }

    public IdeaCategoryListPresenter(IdeaCategoryListPresenterListener listener, Context context) {
        this.mListener = listener;
        this.context = context;
        this.apiClient = new ApiClient() ;
    }

    public void getIdeaCategoryList()
    {


        Singleton.getInstance().showProgressDialog(context);
        apiClient
                .createService(BASE_URL)
                .getIdeaCategoryList(Singleton.getInstance().getValue(context,ADMIN_ID))
                .enqueue(new Callback<CategoryListPojo>() {
                    @Override
                    public void onResponse(Call<CategoryListPojo> call, Response<CategoryListPojo> response) {
                        Log.e("response", "success");
                        Log.e("response", ":: " + response.raw());
//                        Toast.makeText(context, response.body().getMessages().toString(), Toast.LENGTH_SHORT).show();
                        // RestResponse result = response.body();

                        if (response.body().getStatus().equals("true")) {
                            CategoryList.clear();
                            SubCategoryList.clear();
                            for (int i = 0; i < response.body().getData().size(); i++) {
                                CategoryData categoryList=new CategoryData();
                                categoryList.setCategory(response.body().getData().get(i).getCategory());
                                categoryList.setCategoryId(response.body().getData().get(i).getCategoryId());
                              CategoryList.add(response.body().getData().get(i));
                                Gson gson=new Gson();
                                String data=gson.toJson(response.body());
                                Singleton.getInstance().saveValue(context,CATEGORY_LIST,data);
                                setData(response.body());


                                // mListener.setSubCategoryList(SubCategoryList);


                            }




                        }


                        Singleton.getInstance().dismissDialog();
                    }
                    //   if(result != null)
                    //         mListener.countriesReady(response.body().getResult());
                    // Log.e("results",result.getResult().toString());
                    //   }

                    @Override
                    public void onFailure(Call<CategoryListPojo> call, Throwable t) {
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



    public void setData(CategoryListPojo data) {
        if (data.getStatus().equals("true")) {
            CategoryList.clear();

            for (int i = 0; i < data.getData().size(); i++) {

                CategoryList.add(data.getData().get(i));

            }
            mListener.setCategoryList(CategoryList);
        }
        else
        {
            mListener.setCategoryListBlankMessage();
        }
    }





}
