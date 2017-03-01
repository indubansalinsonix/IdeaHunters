package com.ideahunters.fcm;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.ideahunters.utils.Constants;
import com.ideahunters.utils.MySharedPreferences;


/**
 * Created by prashant on 17/10/16.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService implements Constants {

    private static final String TAG = "MyFirebaseIIDService";
    private String url;
    private String UId;
    private MySharedPreferences mySharedPreference;
    public static String refreshedToken;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    public void onTokenRefresh() {
        mySharedPreference = new MySharedPreferences(getApplicationContext());
        //Getting registration token
        refreshedToken = FirebaseInstanceId.getInstance().getToken();
        preferences= PreferenceManager.getDefaultSharedPreferences(MyFirebaseInstanceIDService.this);
        editor=preferences.edit();
        //Displaying token in logcat
        Log.e(TAG, "Refreshed token: " + refreshedToken);
       // sendRegistrationToServer(refreshedToken);
        mySharedPreference.saveNotificationSubscription(true,refreshedToken);
        editor.putString("on", "0");
        editor.commit();
    }

   /* private void sendRegistrationToServer(final String token) {
        //You can implement this method to store the token on your server
        //Not required for current project
        UId = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        url = BaseUrl +"save_cred.php?u_id="+UId+"&device_id="+token;
        Log.d("Url", "url" + url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.d("res:", "resss" + jsonObject.toString());
                try{
                    String status=jsonObject.getString("status");
                    if(status.equalsIgnoreCase("true")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("response");
                        String token=jsonObject1.getString("device_id");
                        String u_id=jsonObject1.getString("u_id");
                       // Toast.makeText(getApplicationContext(), "Token successfully send to server", Toast.LENGTH_LONG).show();

                        mySharedPreference.saveNotificationSubscription(true,token,u_id);

                      *//*  Intent intent = new Intent(MyFirebaseInstanceIDService.this, IdeaListActivity.class);
                        startActivity(intent);*//*
                      //  finish();


                    }else{
                     //   Toast.makeText(getApplicationContext(), "Can't send a token to the server", Toast.LENGTH_LONG).show();
                        mySharedPreference.saveNotificationSubscription(false, "", "");
                        //String message=jsonObject.getString("message");
                       // new ShowMsg().createDialog(MyFirebaseInstanceIDService.this, message);
                        // no_found.setVisibility(View.VISIBLE);
                    }


                }catch(Exception e){
                    Log.d("eeee:", "eee" + e);
                }
                //progressDialog.dismiss();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("volleyError:","volleyError"+volleyError);
//                progressDialog.dismiss();
            }


        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, "tag_json");

    }*/
}




