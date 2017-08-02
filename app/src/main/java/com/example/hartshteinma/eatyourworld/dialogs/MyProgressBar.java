package com.example.hartshteinma.eatyourworld.dialogs;


import android.app.ProgressDialog;
import android.content.Context;


/**
 * Created by hartshteinma on 26/07/2017.
 */

public class MyProgressBar {


    private ProgressDialog progressDialog;
    private Context context;

    public MyProgressBar(Context context) {
        this.context=context;
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading");
        progressDialog.setIndeterminate(true);
    }

    public MyProgressBar() {

    }

    public void showProgressDialog() {
//        if (progressDialog == null) {
//            progressDialog = new ProgressDialog(context);
//            progressDialog.setMessage(""+R.string.loading);
//            progressDialog.setIndeterminate(true);
//        }

        progressDialog.show();
    }

    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }


}
