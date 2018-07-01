package com.sanshy.buysellinventory;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import static com.sanshy.buysellinventory.MyUserStaticClass.isPaid;

public class SideBusinessWork extends AppCompatActivity {

    AdView adView1,adView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_business_work);

        adView1 = findViewById(R.id.adView);
        adView2 = findViewById(R.id.adView2);

        myAds();
    }

    private void myAds() {
        if (!isPaid()){
            adView1.loadAd(new AdRequest.Builder().build());
            adView2.loadAd(new AdRequest.Builder().build());
        }else{
            adView1.setVisibility(View.GONE);
            adView2.setVisibility(View.GONE);
        }
    }

    public void sideBExp(View view){
        startActivity(new Intent(this,SideBExp.class));
    }
    public void sideBIncome(View view){
        startActivity(new Intent(this,SideBIncome.class));
    }
    public void sideBStatement(View view){
        startActivity(new Intent(this,SideBStatement.class));
    }
    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        NetworkConnectivityCheck.connectionCheck(this);

    }
}
