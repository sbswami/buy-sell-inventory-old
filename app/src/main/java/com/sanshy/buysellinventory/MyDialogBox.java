package com.sanshy.buysellinventory;

import android.app.AlertDialog;
import android.content.Context;

public class MyDialogBox {
    public static void ShowDialog(Context context, String text){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage(text)
                .setPositiveButton("OK",null);

        builder.create().show();
    }
}
