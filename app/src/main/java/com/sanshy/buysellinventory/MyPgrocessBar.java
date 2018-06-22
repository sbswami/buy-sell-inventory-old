package com.sanshy.buysellinventory;

import android.app.ProgressDialog;
import android.content.Context;

public class MyPgrocessBar {

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
    public void ShowProgress(){mProgress.show();}
    public void HideProgress(){mProgress.hide();}
}
