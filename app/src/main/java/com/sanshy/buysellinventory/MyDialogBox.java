package com.sanshy.buysellinventory;

import android.app.AlertDialog;
import android.content.Context;

public class MyDialogBox {
    public static void ShowDialog(Context context, String text){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage(text)
                .setPositiveButton(context.getString(R.string.ok_),null);

        builder.create().show();
    }
    public static void DateRequestDialog(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.choose_date)
                .setMessage(context.getString(R.string.please_choose_any_date))
                .setPositiveButton(context.getString(R.string.ok_),null)
                .create()
                .show();
    }
    public static void ErrorFeedbackDialog(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.error_)
                .setMessage(context.getString(R.string.something_is_wrong_)+"\n" +
                        context.getString(R.string.try_algain_later_or_send_feedback_))
                .setPositiveButton(context.getString(R.string.ok_),null)
                .create().show();
    }
}
