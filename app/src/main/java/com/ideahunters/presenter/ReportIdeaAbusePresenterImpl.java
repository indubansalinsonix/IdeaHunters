package com.ideahunters.presenter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.ideahunters.api.ApiClient;
import com.ideahunters.model.UserRegister;
import com.ideahunters.utils.Constants;
import com.ideahunters.utils.Singleton;
import com.ideahunters.view.fragment.IdeaDetailFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by root on 21/2/17.
 */
public class ReportIdeaAbusePresenterImpl implements Constants {
    private final Context context;
    private final ReportIdeaAbusePresenterListener mListener;
    private final ApiClient apiClient;
    private String device_type, status;
    private String image_name;
    private String file;

    public interface ReportIdeaAbusePresenterListener {



        void onSuccessfulReportIdea(String message);

        void onUnSuccessfulReportIdea(String message);
    }


    public ReportIdeaAbusePresenterImpl(ReportIdeaAbusePresenterListener listener, Context context) {
        this.mListener = listener;
        this.context = context;
        this.apiClient = new ApiClient();
    }


    public void reportIdeaAbuse(String sug_id) {

        //   if (onValidate(message)) {
        apiClient
                .createService(BASE_URL)
                .reportIdea(Singleton.getInstance().getValue(context, USER_ID),sug_id)
                .enqueue(new Callback<UserRegister>() {
                    @Override
                    public void onResponse(Call<UserRegister> call, Response<UserRegister> response) {
                        Log.e("response", "success");
                        Log.e("response", ":: " + response.raw());
//                        Toast.makeText(context, response.body().getMessages().toString(), Toast.LENGTH_SHORT).show();
                        // RestResponse result = response.body();

                        if (response.body().getStatus().equals("true")) {

                            mListener.onSuccessfulReportIdea(response.body().getMessage());


                        } else {
                            mListener.onUnSuccessfulReportIdea(response.body().getMessage().toString());
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

