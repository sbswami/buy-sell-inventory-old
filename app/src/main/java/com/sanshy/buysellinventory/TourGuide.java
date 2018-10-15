package com.sanshy.buysellinventory;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import co.mobiwise.materialintro.shape.Focus;
import co.mobiwise.materialintro.shape.FocusGravity;
import co.mobiwise.materialintro.shape.ShapeType;
import co.mobiwise.materialintro.view.MaterialIntroView;

public class TourGuide {

    public static void guide(Activity activity, String GuideId, String Description, View view,int ShowingTime){

        new MaterialIntroView.Builder(activity)
                .enableDotAnimation(false)
                .enableIcon(false)
                .setFocusGravity(FocusGravity.CENTER)
                .setFocusType(Focus.NORMAL)
                .setDelayMillis(0)
                .enableFadeAnimation(true)
                .performClick(true)
                .setInfoText(Description)
                .setShape(ShapeType.RECTANGLE)
                .setTarget(view)
                .setUsageId(GuideId+ShowingTime) //THIS SHOULD BE UNIQUE ID
                .show();
    }
    public static void cicularGuide(Activity activity, String GuideId, String Description, View view,int ShowingTime){
        new MaterialIntroView.Builder(activity)
                .enableDotAnimation(false)
                .enableIcon(false)
                .setFocusGravity(FocusGravity.CENTER)
                .setFocusType(Focus.NORMAL)
                .setDelayMillis(0)
                .enableFadeAnimation(true)
                .performClick(true)
                .setInfoText(Description)
                .setShape(ShapeType.CIRCLE)
                .setTarget(view)
                .setUsageId(GuideId+ShowingTime) //THIS SHOULD BE UNIQUE ID
                .show();
    }

}
