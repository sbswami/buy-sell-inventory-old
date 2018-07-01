package com.sanshy.buysellinventory;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyUserStaticClass {
    public static DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    public static FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public static FirebaseUser userStatic = mAuth.getCurrentUser();

    public static String userIdMainStatic = userStatic.getUid();

    public static boolean paid = false;

    public static RewardedVideoAd r1,r2,r3,r4;
    public static InterstitialAd i1,i2,i3,i4,i5,i6;

    public static void loadAds(Context context){
        i1 = new InterstitialAd(context);
        i1.setAdUnitId(context.getString(R.string.i1));
        i1.loadAd(new AdRequest.Builder().build());

        i2 = new InterstitialAd(context);
        i2.setAdUnitId(context.getString(R.string.i2));
        i2.loadAd(new AdRequest.Builder().build());

        i3 = new InterstitialAd(context);
        i3.setAdUnitId(context.getString(R.string.i3));
        i3.loadAd(new AdRequest.Builder().build());

        i4 = new InterstitialAd(context);
        i4.setAdUnitId(context.getString(R.string.i4));
        i4.loadAd(new AdRequest.Builder().build());

        i5 = new InterstitialAd(context);
        i5.setAdUnitId(context.getString(R.string.i5));
        i5.loadAd(new AdRequest.Builder().build());

        i6 = new InterstitialAd(context);
        i6.setAdUnitId(context.getString(R.string.i6));
        i6.loadAd(new AdRequest.Builder().build());

//        r1.loadAd(context.getString(R.string.r1),new AdRequest.Builder().build());
//        r2.loadAd(context.getString(R.string.r2),new AdRequest.Builder().build());
//        r3.loadAd(context.getString(R.string.r3),new AdRequest.Builder().build());
//        r4.loadAd(context.getString(R.string.r4),new AdRequest.Builder().build());

    }

    public static void showAds(){
        if (i1.isLoaded()){
            i1.show();
            i1.loadAd(new AdRequest.Builder().build());
        }else if (i2.isLoaded()){
            i2.show();
            i2.loadAd(new AdRequest.Builder().build());
        }else if (i3.isLoaded()){
            i3.show();
            i3.loadAd(new AdRequest.Builder().build());
        }else if (i4.isLoaded()){
            i4.show();
            i4.loadAd(new AdRequest.Builder().build());
        }else if (i5.isLoaded()){
            i5.show();
            i5.loadAd(new AdRequest.Builder().build());
        }else if (i6.isLoaded()){
            i6.show();
            i6.loadAd(new AdRequest.Builder().build());
        }else {
            i1.loadAd(new AdRequest.Builder().build());
            i2.loadAd(new AdRequest.Builder().build());
            i3.loadAd(new AdRequest.Builder().build());
            i4.loadAd(new AdRequest.Builder().build());
            i5.loadAd(new AdRequest.Builder().build());
            i6.loadAd(new AdRequest.Builder().build());
        }
    }


    public static boolean isPaid() {
        return paid;
    }

    public static void setPaid(boolean paid) {
        MyUserStaticClass.paid = paid;
    }

    public static String getUserIdMainStatic() {
        return userIdMainStatic;
    }

    public static void setUserIdMainStatic(String userIdMainStatic) {
        MyUserStaticClass.userIdMainStatic = userIdMainStatic;
    }
}
