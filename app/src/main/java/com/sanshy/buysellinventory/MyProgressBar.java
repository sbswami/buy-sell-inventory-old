package com.sanshy.buysellinventory;

import android.app.ProgressDialog;
import android.content.Context;

public class MyProgressBar {

    static ProgressDialog mProgress;
    static String progressText;
    static Context myContext;

    public static void MyProgress(String progressText, Context myContext) {
        mProgress = new ProgressDialog(myContext);
        mProgress.setMessage(progressText);
    }
    public static void MyProgress(Context myContext){
        mProgress = new ProgressDialog(myContext);
        mProgress.setMessage("Loading Data...");
    }
    public static void ShowProgress(){mProgress.show();}
    public static void HideProgress(){mProgress.hide();}

    public static void ShowProgress(Context myContext){
        mProgress = new ProgressDialog(myContext);
        mProgress.setMessage("Loading Data...");

        mProgress.show();
    }


}
