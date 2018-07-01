package com.sanshy.buysellinventory;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import static com.sanshy.buysellinventory.MyDialogBox.ErrorFeedbackDialog;
import static com.sanshy.buysellinventory.MyUserStaticClass.isPaid;

public class fiveItemLister extends AppCompatActivity {

    AdView adView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_five_item_lister);

        adView1 = findViewById(R.id.adView);

        myAds();
        try{

            Intent intent = getIntent();
            String C1[] = intent.getStringArrayExtra("c1");
            String C2[] = intent.getStringArrayExtra("c2");
            String C3[] = intent.getStringArrayExtra("c3");
            String C4[] = intent.getStringArrayExtra("c4");
            String C5[] = intent.getStringArrayExtra("c5");

            String total = intent.getStringExtra("total");

            String c1 = intent.getStringExtra("col1");
            String c2 = intent.getStringExtra("col2");
            String c3 = intent.getStringExtra("col3");
            String c4 = intent.getStringExtra("col4");
            String c5 = intent.getStringExtra("col5");

            TextView col1,col2,col3,col4,col5,remainingAmount;
            ListView listView;
            col1 = findViewById(R.id.col1);
            col2 = findViewById(R.id.col2);
            col3 = findViewById(R.id.col3);
            col4 = findViewById(R.id.col4);
            col5 = findViewById(R.id.col5);
            listView = findViewById(R.id.listView);
            remainingAmount = findViewById(R.id.remainAmount);

            col1.setText(c1);
            col2.setText(c2);
            col3.setText(c3);
            col4.setText(c4);
            col5.setText(c5);

            remainingAmount.setText(total);

            mySellListAdapter historyPayList = new mySellListAdapter(fiveItemLister.this,C1,C2,C3,C4,C5);
            listView.setAdapter(historyPayList);

        }catch (Exception ex){
            ErrorFeedbackDialog(this);
        }



    }

    private void myAds() {
        if (!isPaid()){
            adView1.loadAd(new AdRequest.Builder().build());
        }else{
            adView1.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

}
