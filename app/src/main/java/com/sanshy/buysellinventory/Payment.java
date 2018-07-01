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
import static com.sanshy.buysellinventory.NetworkConnectivityCheck.connectionCheck;
import static com.sanshy.buysellinventory.NetworkConnectivityCheck.isDeviceOnline;

public class Payment extends AppCompatActivity {


    AdView adView1,adView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

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

    public void PayToSupplier(View view)
    {
        startActivity(new Intent(this,payToSupplier.class));
    }
    public void PayByCustomer(View view)
    {
        startActivity(new Intent(this, payByCustomer.class));
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        connectionCheck(this);

    }

}
