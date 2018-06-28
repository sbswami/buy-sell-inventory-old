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

import static com.sanshy.buysellinventory.NetworkConnectivityCheck.connectionCheck;

public class Payment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        AdView adView1,adView2;
        adView1 = findViewById(R.id.adView);
        adView2 = findViewById(R.id.adView2);

        adView1.loadAd(new AdRequest.Builder().build());
        adView2.loadAd(new AdRequest.Builder().build());
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
