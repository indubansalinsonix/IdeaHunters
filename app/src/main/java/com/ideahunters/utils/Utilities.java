package com.ideahunters.utils;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import com.ideahunters.R;

public class Utilities {

    private static Utilities singleton = new Utilities();

    public synchronized static Utilities getInstance() {
        return singleton;
    }

    private Dialog alertDialog;

    public void showProgressDialog(Context activity) {
        alertDialog = new Dialog(activity);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.progress_dialog_layout);
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    public void dismissProgressDialog() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }


}