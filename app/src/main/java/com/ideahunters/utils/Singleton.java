package com.ideahunters.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;


import com.ideahunters.R;
import com.ideahunters.model.CategoryData;
import com.ideahunters.model.Comment;
import com.ideahunters.model.IdeaslistData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Singleton implements Constants {

    private static Singleton singleton = new Singleton();
        public ProgressDialog dialog;
        public SharedPreferences preferences;
        public SharedPreferences.Editor editor ;
        public String mypreference = "mypref";
    boolean MyList=false;
    public String store_id;
    public String branch_id;
    public String mycount;
    Set<String>family_photo_list = new HashSet<String>();
    public ArrayList<IdeaslistData> ideaList=new ArrayList<>();
    public ArrayList<IdeaslistData> filterideaList=new ArrayList<>();
    public ArrayList<IdeaslistData> filterideaList1=new ArrayList<>();
    public ArrayList<IdeaslistData> filterideaList2=new ArrayList<>();
    public ArrayList<IdeaslistData> filterideaList3=new ArrayList<>();
    public ArrayList<IdeaslistData> mainfilterideaList=new ArrayList<>();

    public ArrayList<Comment> commentList;
    public String like_count;
    public ArrayList<CategoryData> categoryList=new ArrayList<>();
    public boolean myList=false;
   public int selectTab=0;
    public int count=0;

    public synchronized static Singleton getInstance()
        {
            return singleton;
        }


        public  void saveValue(Context context, String key, String value)
        {
            preferences=context.getSharedPreferences(mypreference, 0);
            editor=preferences.edit();
            editor.putString(key,value);
            editor.commit();
        }
        public String getValue(Context context, String key)

        {
            preferences=context.getSharedPreferences(mypreference, 0);
            String value=preferences.getString(key,"");
            return value;
        }

    public  void saveSetValue(Context context, String key, List<String> value)
    {
        preferences=context.getSharedPreferences(mypreference, 0);
        editor=preferences.edit();
        family_photo_list.addAll(value);
        editor.putStringSet(key,family_photo_list);
        editor.commit();
    }
    public List<String> getSetValue(Context context, String key)

    {
        preferences=context.getSharedPreferences(mypreference, 0);

         family_photo_list =preferences.getStringSet(key,null);
        List<String> list = new ArrayList<String>(family_photo_list);

        return list;
    }



    public void clearAllData(Context context)
        {
            preferences =context.getSharedPreferences(mypreference,0);
            editor = preferences.edit();
            editor.clear();
            editor.commit();
        }


    public void setAnimation(Context activity, View v, int animation)
    {
        Animation fadeIn= AnimationUtils.loadAnimation(activity,animation);
        v.startAnimation(fadeIn);
    }






    private Dialog alertDialog;
    public void showProgressDialog(Context context) {

        alertDialog = new Dialog(context);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.progress_dialog_layout);
        alertDialog.setCancelable(false);
        alertDialog.show();

    }

    public void dismissDialog() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }







    public void showSnackAlert(ViewGroup viewGroup, String message) {
        Log.e("log","snack");
        Snackbar snack = Snackbar.make(viewGroup, message, Snackbar.LENGTH_LONG);
        ViewGroup group = (ViewGroup) snack.getView();
        group.setBackgroundColor(Color.RED);
        TextView tv = (TextView) group.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        snack.show();
    }


    public boolean isConnectingToInternet(Context _context){
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;
    }







    /**
     * @param activity
     * @description This Method will check the gps is active or not
     */
    public boolean locationEnabled(Activity activity) {
        LocationManager lm;
        boolean gps_enabled = false;
        try {
            lm = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
            gps_enabled = false;
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        if (gps_enabled) {
            return true;
        }
        return false;
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context)
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    android.support.v7.app.AlertDialog.Builder alertBuilder = new android.support.v7.app.AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    android.support.v7.app.AlertDialog alert = alertBuilder.create();
                    alert.show();

                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public String parseDateToddMMyyyy(Context context,String time) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "dd MMM, yyyy HH:mm";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }


}
