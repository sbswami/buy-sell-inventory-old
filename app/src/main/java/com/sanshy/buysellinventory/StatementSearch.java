package com.sanshy.buysellinventory;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class StatementSearch extends AppCompatActivity {

    ArrayList<String> tCashBuy = new ArrayList<>(),tOnHoldBuy = new ArrayList<>();
    ArrayList<String> tSell = new ArrayList<>(),tCashSell = new ArrayList<>(),tOnHoldSell = new ArrayList<>(),tHoldPaymentSellPay = new ArrayList<>(),tOnHoldPaymentAfterPaySell = new ArrayList<>();
    ArrayList<String> tGrossProfit = new ArrayList<>(),tGrossProfitCash = new ArrayList<>(),tGrossProfitOnHold = new ArrayList<>(),tGrossProfitWithHoldPay = new ArrayList<>(),tGrossProfitRemainAfterPay = new ArrayList<>();
    ArrayList<String> tExp = new ArrayList<>(),tNetProfit = new ArrayList<>(),tNetProfitCash = new ArrayList<>(),tNetProfitOnHold = new ArrayList<>(),tNetProfitWithHoldPay = new ArrayList<>(),tNetProfitRemainAfterPay = new ArrayList<>();

    ArrayList<String> tBuy =new ArrayList<>();
    ArrayList<String> tHoldPaymentBuyPay = new ArrayList<>();
    ArrayList<String> tOnHoldPaymentAfterPayBuy = new ArrayList<>();
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();

    ArrayList<String> sellProductList = new ArrayList<>();
    ArrayList<String> sellQuantityList = new ArrayList<>();
    ArrayList<String> sellPriceListSell = new ArrayList<>();
    ArrayList<String> buyPriceListSell = new ArrayList<>();

    ArrayList<String> sQuantityList = new ArrayList<>();
    ArrayList<String> sellPriceListS = new ArrayList<>();
    ArrayList<String> buyPriceListS = new ArrayList<>();

    ArrayList<String> sQuantityListoh = new ArrayList<>();
    ArrayList<String> sellPriceListSoh = new ArrayList<>();
    ArrayList<String> buyPriceListSoh = new ArrayList<>();
    TextView totalBuy,totalCashBuy, totalOnHoldBuy,totalHoldPaymentBuyPay,totalOnHoldPaymentAfterPayBuy;
    TextView totalSell,totalCashSell,totalOnHoldSell,totalHoldPaymentSellPay,totalOnHoldPaymentAfterPaySell;
    TextView totalGrossProfit,totalGrossProfitCash,totalGrossProfitOnHold,totalGrossProfitWithHoldPay,totalGrossProfitRemainAfterPay;
    TextView totalExp,totalNetProfit,totalNetProfitCash,totalNetProfitOnHold,totalNetProfitWithHoldPay,totalNetProfitRemainAfterPay;

    ArrayList<String> datesList = new ArrayList<>();

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //TODO UnComment it
//        tBuy.clear();
//        tCashBuy.clear();
//        tOnHoldBuy.clear();
//        tHoldPaymentBuyPay.clear();
//        tOnHoldPaymentAfterPayBuy.clear();
//        tSell.clear();
//        tCashSell.clear();
//        tOnHoldSell.clear();
//        tHoldPaymentSellPay.clear();
//        tOnHoldPaymentAfterPaySell.clear();
//        tGrossProfit.clear();
//        tGrossProfitCash.clear();
//        tGrossProfitOnHold.clear();
//        tGrossProfitWithHoldPay.clear();
//        tGrossProfitRemainAfterPay.clear();
//        tExp.clear();
//        tNetProfit.clear();
//        tNetProfitCash.clear();
//        tNetProfitOnHold.clear();
//        tNetProfitWithHoldPay.clear();
////        tNetProfitRemainAfterPay.clear();
//        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    protected void onPause() {
        super.onPause();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    Button tbb,tcbb,tohbb,tppbsb,trpbsb;
    Button tsb,tcsb,tohsb,tppbcb,trpbcb;
    Button tgpb,tcgpb,tohgpb,tgppbcb,trgpcb;
    Button tnpb,tcnpb,tohnpb;

    int checktnp = 0;
    int checktcnp = 0;
    int checktohnp = 0;

    double exp = 0;
    double onHoldMoneySupplier = 0;
    double onHoldMoneyCustomer = 0;
    int checkOnHoldButtonBuy = 0;
    int checkOnHoldButtonSell = 0;
    double grossProfitOnHold = 0;
    double grossProfitTotal = 0;
    double grossProfitCash = 0;
    DatabaseReference mBuyRef;
    DatabaseReference mPayToSupplier;
    DatabaseReference mSellRef;
    DatabaseReference mPayByCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statement_search);
        mBuyRef = mRootRef.child(user.getUid()+"/buy");
        mPayToSupplier = mRootRef.child(user.getUid()+"/payToSupplier");

        mSellRef = mRootRef.child(user.getUid()+"/sell");
        mPayByCustomer = mRootRef.child(user.getUid()+"/payByCustomer");

        tbb = findViewById(R.id.tsb2);
        tcbb = findViewById(R.id.tcsb2);
        tohbb = findViewById(R.id.tohsb2);
        tppbsb = findViewById(R.id.ppbcb2);
        trpbsb = findViewById(R.id.rpfcb2);

        tsb = findViewById(R.id.tsb);
        tcsb = findViewById(R.id.tcsb);
        tohsb = findViewById(R.id.tohsb);
        tppbcb = findViewById(R.id.ppbcb);
        trpbcb = findViewById(R.id.rpfcb);

        tgpb = findViewById(R.id.tsb3);
        tcgpb = findViewById(R.id.tcsb3);
        tohgpb = findViewById(R.id.tohsb3);
        tgppbcb = findViewById(R.id.ppbcb3);
        trgpcb = findViewById(R.id.rpfcb3);

        tnpb = findViewById(R.id.tsb4);
        tcnpb = findViewById(R.id.tcsb4);
        tohnpb = findViewById(R.id.tohsb4);

        {
            totalBuy = findViewById(R.id.totalBuy);
            totalCashBuy = findViewById(R.id.totalCashBuy);
            totalOnHoldBuy = findViewById(R.id.totalOnHOldBuy);
            totalHoldPaymentBuyPay = findViewById(R.id.totalHoldPaymentBuyPay);
            totalOnHoldPaymentAfterPayBuy = findViewById(R.id.totalOnHoldPaymentAfterPayBuy);
            totalSell = findViewById(R.id.totalSell);
            totalCashSell = findViewById(R.id.totalCashSell);
            totalOnHoldSell = findViewById(R.id.totalOnHoldSell);
            totalHoldPaymentSellPay = findViewById(R.id.totalHoldPaymentSellPay);
            totalOnHoldPaymentAfterPaySell = findViewById(R.id.totalOnHoldPaymentAfterPaySell);
            totalGrossProfit = findViewById(R.id.totalGrossProfit);
            totalGrossProfitCash = findViewById(R.id.totalGrossProfitCash);
            totalGrossProfitOnHold = findViewById(R.id.totalGrossProfitOnHold);
            totalGrossProfitWithHoldPay = findViewById(R.id.totalGrossProfitWithHoldPay);
            totalGrossProfitRemainAfterPay = findViewById(R.id.totalGrossProfitRemainAfterPay);
            totalExp  = findViewById(R.id.totalExp);
            totalNetProfit = findViewById(R.id.totalNetProfit);
            totalNetProfitCash = findViewById(R.id.totalNetProfitCash);
            totalNetProfitOnHold = findViewById(R.id.totalNetProfitOnHold);
            totalNetProfitWithHoldPay = findViewById(R.id.totalNetProfitWithHoldPay);
            totalNetProfitRemainAfterPay = findViewById(R.id.totalNetProfitRemainAfterPay);
        }

        Intent intent = getIntent();
        String dates[] = intent.getStringArrayExtra("dates");

        for (int i = 0; i < dates.length ; i++)
        {
            datesList.add(dates[i]);
        }
        System.out.println(datesList);

        AdView adView1,adView2,adView3,adView4,adView5;
        adView1 = findViewById(R.id.adView);
        adView2 = findViewById(R.id.adView2);
        adView3 = findViewById(R.id.adView3);
        adView4 = findViewById(R.id.adView4);
        adView5 = findViewById(R.id.adView5);

        adView1.loadAd(new AdRequest.Builder().build());
        adView2.loadAd(new AdRequest.Builder().build());
        adView3.loadAd(new AdRequest.Builder().build());
        adView4.loadAd(new AdRequest.Builder().build());
        adView5.loadAd(new AdRequest.Builder().build());

    }
    public void alertDialog(String title, String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(msg)
                .setPositiveButton("OK",null)
                .create().show();
    }

    public void ts(View view){
        checktnp = 1;
        tsb.setVisibility(View.GONE);
        totalSell.setVisibility(View.VISIBLE);
        tgpb.setVisibility(View.GONE);
        totalGrossProfit.setVisibility(View.VISIBLE);
        for (int h = 0; h < datesList.size(); h++) {
            String date = datesList.get(h);

            Query totalSellQuery = mSellRef.orderByChild("date").equalTo(date);
            totalSellQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    double priceTotal = 0;
                    sellProductList.clear();
                    sellQuantityList.clear();
                    sellPriceListSell.clear();
                    buyPriceListSell.clear();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                    {
                        try
                        {
                            priceTotal += Double.parseDouble(dataSnapshot1.child("price").getValue(String.class));
                            sellQuantityList.add(dataSnapshot1.child("quantity").getValue(String.class));
                            sellPriceListSell.add(dataSnapshot1.child("productSellPrice").getValue(String.class));
                            buyPriceListSell.add(dataSnapshot1.child("productBuyPrice").getValue(String.class));
                        }catch (Exception ex){}
                    }
                    try {
                        String st = priceTotal+"";
                        if (st.isEmpty())
                        {

                        }
                        else {
                            tSell.add(priceTotal+"");
                            double sum = sumAll(tSell);
                            totalSell.setText("Total Sell : "+sum);
                        }
                    }catch (Exception ex){}
                    //totalSell.setText("Total Sell : "+priceTotal);

                    String SellQuantityList[] = new String[sellQuantityList.size()];
                    String SellPriceListSell[] = new String[sellPriceListSell.size()];
                    String BuyPriceListSell[] = new String[buyPriceListSell.size()];

                    double totalSellMoney = 0;
                    double totalBuyMoney = 0;

                    for (int i = 0; i < sellQuantityList.size(); i++)
                    {
                        SellQuantityList[i] = sellQuantityList.get(i);
                        SellPriceListSell[i] = sellPriceListSell.get(i);
                        BuyPriceListSell[i] = buyPriceListSell.get(i);

                        try{
                            int quan = (int)Double.parseDouble(SellQuantityList[i]);

                            double SellMoney = quan * Double.parseDouble(SellPriceListSell[i]);
                            double BuyMoney = quan * Double.parseDouble(BuyPriceListSell[i]);

                            totalSellMoney += SellMoney;
                            totalBuyMoney += BuyMoney;

                        }catch (Exception ex){}
                    }
                    final double grossProfit = totalSellMoney - totalBuyMoney;
                    try {
                        String st = grossProfit+"";
                        if (st.isEmpty())
                        {

                        }
                        else {
                            tGrossProfit.add(grossProfit+"");
                            double sum = sumAll(tGrossProfit);
                            grossProfitTotal = sum;
                            totalGrossProfit.setText("Gross Profit : "+sum);
                        }
                    }catch (Exception ex){}
                    //totalGrossProfit.setText("Gross Profit : "+grossProfit);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

                }
                /*
        Query totalSellQuery = mSellRef.orderByChild("date").equalTo(date);
        totalSellQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double priceTotal = 0;
                sellProductList.clear();
                sellQuantityList.clear();
                sellPriceListSell.clear();
                buyPriceListSell.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    try
                    {
                        priceTotal += Double.parseDouble(dataSnapshot1.child("price").getValue(String.class));
                        sellQuantityList.add(dataSnapshot1.child("quantity").getValue(String.class));
                        sellPriceListSell.add(dataSnapshot1.child("productSellPrice").getValue(String.class));
                        buyPriceListSell.add(dataSnapshot1.child("productBuyPrice").getValue(String.class));
                    }catch (Exception ex){}
                }

                String totalSellText = getResources().getString(R.string.total_sell_text);
                totalSell.setText(totalSellText+priceTotal);


                String SellQuantityList[] = new String[sellQuantityList.size()];
                String SellPriceListSell[] = new String[sellPriceListSell.size()];
                String BuyPriceListSell[] = new String[buyPriceListSell.size()];

                double totalSellMoney = 0;
                double totalBuyMoney = 0;

                for (int i = 0; i < sellQuantityList.size(); i++)
                {
                    SellQuantityList[i] = sellQuantityList.get(i);
                    SellPriceListSell[i] = sellPriceListSell.get(i);
                    BuyPriceListSell[i] = buyPriceListSell.get(i);

                    try{
                        int quan = (int)Double.parseDouble(SellQuantityList[i]);

                        double SellMoney = quan * Double.parseDouble(SellPriceListSell[i]);
                        double BuyMoney = quan * Double.parseDouble(BuyPriceListSell[i]);

                        totalSellMoney += SellMoney;
                        totalBuyMoney += BuyMoney;

                    }catch (Exception ex){}
                }
                double grossProfit = totalSellMoney - totalBuyMoney;
                grossProfitTotal = grossProfit;
                String grossProfitText = getResources().getString(R.string.gross_profit_text);
                totalGrossProfit.setText(grossProfitText+grossProfit);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        */
    }
    public void tcs(View view){

        checktcnp = 1;
        tcsb.setVisibility(View.GONE);
        totalCashSell.setVisibility(View.VISIBLE);
        tcgpb.setVisibility(View.GONE);
        totalGrossProfitCash.setVisibility(View.VISIBLE);

        for (int h = 0; h < datesList.size(); h++) {
            String date = datesList.get(h);
            Query totalCashSellQuery = mSellRef.orderByChild("date_mode").equalTo(date+"_Cash");
            totalCashSellQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    double totalMoney = 0;
                    ArrayList<String> sQuantityList = new ArrayList<>();
                    ArrayList<String> sellPriceListS = new ArrayList<>();
                    ArrayList<String> buyPriceListS = new ArrayList<>();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                    {
                        try{
                            totalMoney += Double.parseDouble(dataSnapshot1.child("price").getValue(String.class));
                            sQuantityList.add(dataSnapshot1.child("quantity").getValue(String.class));
                            sellPriceListS.add(dataSnapshot1.child("productSellPrice").getValue(String.class));
                            buyPriceListS.add(dataSnapshot1.child("productBuyPrice").getValue(String.class));
                        }catch (Exception ex){}
                    }
                    try {
                        String st = totalMoney+"";
                        if (st.isEmpty())
                        {

                        }
                        else {
                            tCashSell.add(totalMoney+"");
                            double sum = sumAll(tCashSell);
                            totalCashSell.setText("Total Cash Sell : "+sum);
                        }
                    }catch (Exception ex){}
                    //totalCashSell.setText("Total Cash Sell : "+totalMoney);

                    String SellQuantityList[] = new String[sQuantityList.size()];
                    String SellPriceListSell[] = new String[sellPriceListS.size()];
                    String BuyPriceListSell[] = new String[buyPriceListS.size()];

                    double totalSellMoney = 0;
                    double totalBuyMoney = 0;

                    for (int i = 0; i < sQuantityList.size(); i++)
                    {
                        SellQuantityList[i] = sQuantityList.get(i);
                        SellPriceListSell[i] = sellPriceListS.get(i);
                        BuyPriceListSell[i] = buyPriceListS.get(i);

                        try{
                            int quan = (int)Double.parseDouble(SellQuantityList[i]);

                            double SellMoney = quan * Double.parseDouble(SellPriceListSell[i]);
                            double BuyMoney = quan * Double.parseDouble(BuyPriceListSell[i]);

                            totalSellMoney += SellMoney;
                            totalBuyMoney += BuyMoney;

                        }catch (Exception ex){}
                    }
                    final double grossProfit = totalSellMoney - totalBuyMoney;
                    try {
                        String st = grossProfit+"";
                        if (st.isEmpty())
                        {

                        }
                        else {
                            tGrossProfitCash.add(grossProfit+"");
                            double sum = sumAll(tGrossProfitCash);
                            grossProfitCash = sum;
                            totalGrossProfitCash.setText("Gross Profit Cash : "+sum);
                        }
                    }catch (Exception ex){}
                    //totalGrossProfitCash.setText("Gross Profit Cash : "+grossProfit);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
                }
                /*
            Query totalCashSellQuery = mSellRef.orderByChild("date_mode").equalTo(date+"_Cash");
            totalCashSellQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double totalMoney = 0;
                sQuantityList.clear();
                sellPriceListS.clear();
                buyPriceListS.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    try{
                        totalMoney += Double.parseDouble(dataSnapshot1.child("price").getValue(String.class));
                        sQuantityList.add(dataSnapshot1.child("quantity").getValue(String.class));
                        sellPriceListS.add(dataSnapshot1.child("productSellPrice").getValue(String.class));
                        buyPriceListS.add(dataSnapshot1.child("productBuyPrice").getValue(String.class));
                    }catch (Exception ex){}
                }
                String totalCashSellText = getResources().getString(R.string.total_cash_sell_text);
                totalCashSell.setText(totalCashSellText+totalMoney);

                String SellQuantityList[] = new String[sQuantityList.size()];
                String SellPriceListSell[] = new String[sellPriceListS.size()];
                String BuyPriceListSell[] = new String[buyPriceListS.size()];

                double totalSellMoney = 0;
                double totalBuyMoney = 0;

                for (int i = 0; i < sQuantityList.size(); i++)
                {
                    SellQuantityList[i] = sQuantityList.get(i);
                    SellPriceListSell[i] = sellPriceListS.get(i);
                    BuyPriceListSell[i] = buyPriceListS.get(i);

                    try{
                        int quan = (int)Double.parseDouble(SellQuantityList[i]);

                        double SellMoney = quan * Double.parseDouble(SellPriceListSell[i]);
                        double BuyMoney = quan * Double.parseDouble(BuyPriceListSell[i]);

                        totalSellMoney += SellMoney;
                        totalBuyMoney += BuyMoney;

                    }catch (Exception ex){}
                }
                final double grossProfit = totalSellMoney - totalBuyMoney;
                grossProfitCash = grossProfit;
                String grossProfitCashText = getResources().getString(R.string.gross_profit_cash_text);
                totalGrossProfitCash.setText(grossProfitCashText+grossProfit);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
*/
    }
    public void tohs(View view){
        tohgpb.setVisibility(View.GONE);
        totalGrossProfitOnHold.setVisibility(View.VISIBLE);
        tohsb.setVisibility(View.GONE);
        totalOnHoldSell.setVisibility(View.VISIBLE);

        for (int h = 0; h < datesList.size(); h++) {
            String date = datesList.get(h);
        Query totalOnHoldSellQuery = mSellRef.orderByChild("date_mode").equalTo(date+"_On Hold");
        totalOnHoldSellQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double totalMoney1 = 0;
                ArrayList<String> sQuantityList = new ArrayList<>();
                ArrayList<String> sellPriceListS = new ArrayList<>();
                ArrayList<String> buyPriceListS = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    try {
                        totalMoney1 += Double.parseDouble(dataSnapshot1.child("price").getValue(String.class));
                        sQuantityList.add(dataSnapshot1.child("quantity").getValue(String.class));
                        sellPriceListS.add(dataSnapshot1.child("productSellPrice").getValue(String.class));
                        buyPriceListS.add(dataSnapshot1.child("productBuyPrice").getValue(String.class));
                    } catch (Exception ex) {
                    }
                }
                try {
                    String st = totalMoney1 + "";
                    if (st.isEmpty()) {

                    } else {
                        tOnHoldSell.add(totalMoney1 + "");
                        double sum = sumAll(tOnHoldSell);
                        onHoldMoneyCustomer = sum;
                        checktohnp = 1;
                        checkOnHoldButtonSell = 1;
                        totalOnHoldSell.setText("Total On Hold Sell : " + sum);
                    }
                } catch (Exception ex) {
                }
                //totalOnHoldSell.setText("Total On Hold Sell : "+totalMoney1);

                String SellQuantityList[] = new String[sQuantityList.size()];
                String SellPriceListSell[] = new String[sellPriceListS.size()];
                String BuyPriceListSell[] = new String[buyPriceListS.size()];

                double totalSellMoney = 0;
                double totalBuyMoney = 0;

                for (int i = 0; i < sQuantityList.size(); i++) {
                    SellQuantityList[i] = sQuantityList.get(i);
                    SellPriceListSell[i] = sellPriceListS.get(i);
                    BuyPriceListSell[i] = buyPriceListS.get(i);

                    try {
                        int quan = (int) Double.parseDouble(SellQuantityList[i]);

                        double SellMoney = quan * Double.parseDouble(SellPriceListSell[i]);
                        double BuyMoney = quan * Double.parseDouble(BuyPriceListSell[i]);

                        totalSellMoney += SellMoney;
                        totalBuyMoney += BuyMoney;

                    } catch (Exception ex) {
                    }
                }
                final double grossProfit = totalSellMoney - totalBuyMoney;
                try {
                    String st = grossProfit + "";
                    if (st.isEmpty()) {

                    } else {
                        tGrossProfitOnHold.add(grossProfit + "");
                        double sum = sumAll(tGrossProfitOnHold);
                        grossProfitOnHold = sum;
                        totalGrossProfitOnHold.setText("Gross Profit On Hold : " + sum);
                    }
                } catch (Exception ex) {
                }
                //totalGrossProfitOnHold.setText("Gross Profit On Hold : "+grossProfit);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
            }

            /*
        Query totalOnHoldSellQuery = mSellRef.orderByChild("date_mode").equalTo(date+"_On Hold");
        totalOnHoldSellQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double totalMoney1 = 0;
                sQuantityListoh.clear();
                sellPriceListSoh.clear();
                buyPriceListSoh.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    try{
                        totalMoney1 += Double.parseDouble(dataSnapshot1.child("price").getValue(String.class));
                        sQuantityListoh.add(dataSnapshot1.child("quantity").getValue(String.class));
                        sellPriceListSoh.add(dataSnapshot1.child("productSellPrice").getValue(String.class));
                        buyPriceListSoh.add(dataSnapshot1.child("productBuyPrice").getValue(String.class));
                    }catch (Exception ex){}
                }
                onHoldMoneyCustomer = totalMoney1;
                String totalOnHoldSellText = getResources().getString(R.string.total_on_hold_sell_text);
                totalOnHoldSell.setText(totalOnHoldSellText+totalMoney1);
                String SellQuantityList[] = new String[sQuantityListoh.size()];
                String SellPriceListSell[] = new String[sellPriceListSoh.size()];
                String BuyPriceListSell[] = new String[buyPriceListSoh.size()];

                double totalSellMoney = 0;
                double totalBuyMoney = 0;

                for (int i = 0; i < sQuantityListoh.size(); i++)
                {
                    SellQuantityList[i] = sQuantityListoh.get(i);
                    SellPriceListSell[i] = sellPriceListSoh.get(i);
                    BuyPriceListSell[i] = buyPriceListSoh.get(i);

                    try{
                        int quan = (int)Double.parseDouble(SellQuantityList[i]);

                        double SellMoney = quan * Double.parseDouble(SellPriceListSell[i]);
                        double BuyMoney = quan * Double.parseDouble(BuyPriceListSell[i]);

                        totalSellMoney += SellMoney;
                        totalBuyMoney += BuyMoney;

                    }catch (Exception ex){}
                }
                double grossProfit = totalSellMoney - totalBuyMoney;
                grossProfitOnHold = grossProfit;
                String grossProfitOnHoldText = getResources().getString(R.string.gross_profit_on_hold_text);
                totalGrossProfitOnHold.setText(grossProfitOnHoldText+grossProfit);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
*/
    }
    public void ppbc(View view){
        if (checkOnHoldButtonSell == 0){
            alertDialog("Click Upper Button","Before Click It You Need To Click On Hold Sell Button!!");
        }
        else if (totalOnHoldSell.equals("Loading.....")){
            alertDialog("Wait","Wait Some Time To Load");
        }
        else {

            tgppbcb.setVisibility(View.GONE);
            totalGrossProfitWithHoldPay.setVisibility(View.VISIBLE);
            trgpcb.setVisibility(View.GONE);
            totalGrossProfitRemainAfterPay.setVisibility(View.VISIBLE);

            tppbcb.setVisibility(View.GONE);
            totalHoldPaymentSellPay.setVisibility(View.VISIBLE);
            trpbcb.setVisibility(View.GONE);
            totalOnHoldPaymentAfterPaySell.setVisibility(View.VISIBLE);

            for (int h = 0; h < datesList.size(); h++) {
                String date = datesList.get(h);
            Query totalHoldPaymentSellPayQuery = mPayByCustomer.orderByChild("date").equalTo(date);
            final double finalTotalMoney = onHoldMoneyCustomer;
            totalHoldPaymentSellPayQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    double totalMoney2 = 0;
                    double totalProfit = 0;
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        try {
                            totalMoney2 += Double.parseDouble(dataSnapshot1.child("money").getValue(String.class));
                            totalProfit += Double.parseDouble(dataSnapshot1.child("grossProfit").getValue(String.class));
                        } catch (Exception ex) {
                        }
                    }


                    try {
                        String st = totalProfit + "";
                        if (st.isEmpty()) {

                        } else {
                            tGrossProfitWithHoldPay.add(totalProfit + "");
                            double sum = sumAll(tGrossProfitWithHoldPay);
                            totalGrossProfitWithHoldPay.setText("Gross Profit On Hold Customer Pay : " + sum);
                        }
                    } catch (Exception ex) {
                    }
                    //totalGrossProfitWithHoldPay.setText("Gross Profit On Hold Customer Pay : "+totalProfit);
                    try {
                        String st = totalMoney2 + "";
                        if (st.isEmpty()) {

                        } else {
                            tHoldPaymentSellPay.add(totalMoney2 + "");
                            double sum = sumAll(tHoldPaymentSellPay);
                            totalHoldPaymentSellPay.setText("Hold Payment Paid By Customer : " + sum);
                        }
                    } catch (Exception ex) {
                    }
                    //totalHoldPaymentSellPay.setText("Hold Payment Paid By Customer : "+totalMoney2);
                    double result = finalTotalMoney - totalMoney2;
                    final double resultGross = grossProfitOnHold - totalProfit;
                    try {
                        String st = result + "";
                        if (st.isEmpty()) {

                        } else {
                            tOnHoldPaymentAfterPaySell.add(result + "");
                            double sum = sumAll(tOnHoldPaymentAfterPaySell);
                            totalOnHoldPaymentAfterPaySell.setText("Remaining Payment From Customer : " + sum);
                        }
                    } catch (Exception ex) {
                    }
                    //totalOnHoldPaymentAfterPaySell.setText("Remaining Payment From Customer : "+result);
                    try {
                        String st = resultGross + "";
                        if (st.isEmpty()) {

                        } else {
                            tGrossProfitRemainAfterPay.add(resultGross + "");
                            double sum = sumAll(tGrossProfitRemainAfterPay);
                            totalGrossProfitRemainAfterPay.setText("Remaining Profit From Customer : " + sum);
                        }
                    } catch (Exception ex) {
                    }
                    //totalGrossProfitRemainAfterPay.setText("Remaining Profit From Customer : "+resultGross);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
                }
                /*
                    Query totalHoldPaymentSellPayQuery = mPayByCustomer.orderByChild("date").equalTo(date);
            final double finalTotalMoney = onHoldMoneyCustomer;
            totalHoldPaymentSellPayQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    double totalMoney2 = 0;
                    double totalProfit = 0;
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                    {
                        try{
                            totalMoney2+=Double.parseDouble(dataSnapshot1.child("money").getValue(String.class));
                            totalProfit+=Double.parseDouble(dataSnapshot1.child("grossProfit").getValue(String.class));
                        }catch (Exception ex){}
                    }
                    String holdPaymentPaidByCustomerText = getResources().getString(R.string.hold_payment_paid_by_customer_text);
                    totalHoldPaymentSellPay.setText(holdPaymentPaidByCustomerText+totalMoney2);
                    double result = finalTotalMoney - totalMoney2;
                    String remainingPaymentFromCustomerText = getResources().getString(R.string.remaining_payment_from_customer_text);
                    totalOnHoldPaymentAfterPaySell.setText(remainingPaymentFromCustomerText+result);

                    String grossProfitOnHoldCustomerPayText = getResources().getString(R.string.gross_profit_on_hold_customer_pay_text);
                    totalGrossProfitWithHoldPay.setText(grossProfitOnHoldCustomerPayText+totalProfit);
                    final double resultGross = grossProfitOnHold - totalProfit;
                    String remainingProfitFromCustomerText = getResources().getString(R.string.remaining_profit_from_customer_text);
                    totalGrossProfitRemainAfterPay.setText(remainingProfitFromCustomerText+resultGross);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            */
        }
    }
    public void rpfc(View view){
        if (checkOnHoldButtonSell == 0){
            alertDialog("Click Upper Button","Before Click It You Need To Click On Hold Sell Button!!");
        }
        else if (totalOnHoldSell.equals("Loading.....")){
            alertDialog("Wait","Wait Some Time To Load");
        }
        else {

            tgppbcb.setVisibility(View.GONE);
            totalGrossProfitWithHoldPay.setVisibility(View.VISIBLE);
            trgpcb.setVisibility(View.GONE);
            totalGrossProfitRemainAfterPay.setVisibility(View.VISIBLE);

            tppbcb.setVisibility(View.GONE);
            totalHoldPaymentSellPay.setVisibility(View.VISIBLE);
            trpbcb.setVisibility(View.GONE);
            totalOnHoldPaymentAfterPaySell.setVisibility(View.VISIBLE);

            for (int h = 0; h < datesList.size(); h++) {
                String date = datesList.get(h);
                Query totalHoldPaymentSellPayQuery = mPayByCustomer.orderByChild("date").equalTo(date);
                final double finalTotalMoney = onHoldMoneyCustomer;
                totalHoldPaymentSellPayQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        double totalMoney2 = 0;
                        double totalProfit = 0;
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            try {
                                totalMoney2 += Double.parseDouble(dataSnapshot1.child("money").getValue(String.class));
                                totalProfit += Double.parseDouble(dataSnapshot1.child("grossProfit").getValue(String.class));
                            } catch (Exception ex) {
                            }
                        }


                        try {
                            String st = totalProfit + "";
                            if (st.isEmpty()) {

                            } else {
                                tGrossProfitWithHoldPay.add(totalProfit + "");
                                double sum = sumAll(tGrossProfitWithHoldPay);
                                totalGrossProfitWithHoldPay.setText("Gross Profit On Hold Customer Pay : " + sum);
                            }
                        } catch (Exception ex) {
                        }
                        //totalGrossProfitWithHoldPay.setText("Gross Profit On Hold Customer Pay : "+totalProfit);
                        try {
                            String st = totalMoney2 + "";
                            if (st.isEmpty()) {

                            } else {
                                tHoldPaymentSellPay.add(totalMoney2 + "");
                                double sum = sumAll(tHoldPaymentSellPay);
                                totalHoldPaymentSellPay.setText("Hold Payment Paid By Customer : " + sum);
                            }
                        } catch (Exception ex) {
                        }
                        //totalHoldPaymentSellPay.setText("Hold Payment Paid By Customer : "+totalMoney2);
                        double result = finalTotalMoney - totalMoney2;
                        final double resultGross = grossProfitOnHold - totalProfit;
                        try {
                            String st = result + "";
                            if (st.isEmpty()) {

                            } else {
                                tOnHoldPaymentAfterPaySell.add(result + "");
                                double sum = sumAll(tOnHoldPaymentAfterPaySell);
                                totalOnHoldPaymentAfterPaySell.setText("Remaining Payment From Customer : " + sum);
                            }
                        } catch (Exception ex) {
                        }
                        //totalOnHoldPaymentAfterPaySell.setText("Remaining Payment From Customer : "+result);
                        try {
                            String st = resultGross + "";
                            if (st.isEmpty()) {

                            } else {
                                tGrossProfitRemainAfterPay.add(resultGross + "");
                                double sum = sumAll(tGrossProfitRemainAfterPay);
                                totalGrossProfitRemainAfterPay.setText("Remaining Profit From Customer : " + sum);
                            }
                        } catch (Exception ex) {
                        }
                        //totalGrossProfitRemainAfterPay.setText("Remaining Profit From Customer : "+resultGross);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }/*
            Query totalHoldPaymentSellPayQuery = mPayByCustomer.orderByChild("date").equalTo(date);
            final double finalTotalMoney = onHoldMoneyCustomer;
            totalHoldPaymentSellPayQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    double totalMoney2 = 0;
                    double totalProfit = 0;
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                    {
                        try{
                            totalMoney2+=Double.parseDouble(dataSnapshot1.child("money").getValue(String.class));
                            totalProfit+=Double.parseDouble(dataSnapshot1.child("grossProfit").getValue(String.class));
                        }catch (Exception ex){}
                    }
                    String holdPaymentPaidByCustomerText = getResources().getString(R.string.hold_payment_paid_by_customer_text);
                    totalHoldPaymentSellPay.setText(holdPaymentPaidByCustomerText+totalMoney2);
                    double result = finalTotalMoney - totalMoney2;
                    String remainingPaymentFromCustomerText = getResources().getString(R.string.remaining_payment_from_customer_text);
                    totalOnHoldPaymentAfterPaySell.setText(remainingPaymentFromCustomerText+result);

                    String grossProfitOnHoldCustomerPayText = getResources().getString(R.string.gross_profit_on_hold_customer_pay_text);
                    totalGrossProfitWithHoldPay.setText(grossProfitOnHoldCustomerPayText+totalProfit);
                    final double resultGross = grossProfitOnHold - totalProfit;
                    String remainingProfitFromCustomerText = getResources().getString(R.string.remaining_profit_from_customer_text);
                    totalGrossProfitRemainAfterPay.setText(remainingProfitFromCustomerText+resultGross);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            */
        }
    }

    ValueEventListener singleValue1;
    public void tb(View view){

        for (int h = 0; h < datesList.size(); h++){
            String date = datesList.get(h);
            Query totalBuyQuery = mBuyRef.orderByChild("date").equalTo(date);
            singleValue1 = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    double priceTotal = 0;
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                    {
                        try
                        {
                            priceTotal += Double.parseDouble(dataSnapshot1.child("price").getValue(String.class));
                        }catch (Exception ex){}
                    }
                    try {
                        String st = priceTotal+"";
                        if (st.isEmpty())
                        {
                            double sum = sumAll(tBuy);
                            totalBuy.setText("Total Buy : "+sum);
                        }
                        else {
                            tBuy.add(priceTotal+"");
                            double sum = sumAll(tBuy);
                            totalBuy.setText("Total Buy : "+sum);
                        }
                    }catch (Exception ex){}
                    //totalBuy.setText("Total Buy : "+priceTotal);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            totalBuyQuery.addListenerForSingleValueEvent(singleValue1);
        }

        tbb.setVisibility(View.GONE);
        totalBuy.setVisibility(View.VISIBLE);

    }
    ValueEventListener singleValue2;
    public void tcb(View view){
        tcbb.setVisibility(View.GONE);
        totalCashBuy.setVisibility(View.VISIBLE);
        for (int h = 0; h < datesList.size(); h++) {
            String date = datesList.get(h);
            Query totalCashBuyQuery = mBuyRef.orderByChild("date_mode").equalTo(date + "_Cash");
            singleValue2 = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    double totalMoney = 0;
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        try {
                            totalMoney += Double.parseDouble(dataSnapshot1.child("price").getValue(String.class));
                        } catch (Exception ex) {
                        }
                    }
                    double sum = 0.0;
                    try {
                        String st = totalMoney + "";
                        if (st.isEmpty()) {
                            sum = sumAll(tCashBuy);
                            totalCashBuy.setText("Total Cash Buy : " + sum);
                        } else {
                            tCashBuy.add(totalMoney + "");
                            sum = sumAll(tCashBuy);
                            totalCashBuy.setText("Total Cash Buy : " + sum);
                        }
                    } catch (Exception ex) {
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            totalCashBuyQuery.addListenerForSingleValueEvent(singleValue2);
        }
    }
    ValueEventListener singleValue3;
    public void tohb(View view){
        tohbb.setVisibility(View.GONE);
        totalOnHoldBuy.setVisibility(View.VISIBLE);
        for (int h = 0; h < datesList.size(); h++) {
            String date = datesList.get(h);

            Query totalOnHoldBuyQuery = mBuyRef.orderByChild("date_mode").equalTo(date+"_On Hold");
            singleValue3 = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    double totalMoney1 = 0;
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                    {
                        try{
                            totalMoney1 += Double.parseDouble(dataSnapshot1.child("price").getValue(String.class));
                        }catch (Exception ex){}
                    }
                    try {
                        String st = totalMoney1+"";
                        if (st.isEmpty())
                        {
                            double sum = sumAll(tOnHoldBuy);
                            totalOnHoldBuy.setText("Total On Hold Buy : "+sum);
                        }
                        else {
                            tOnHoldBuy.add(totalMoney1+"");
                            double sum = sumAll(tOnHoldBuy);
                            onHoldMoneySupplier = sum;
                            checkOnHoldButtonBuy =1;
                            totalOnHoldBuy.setText("Total On Hold Buy : "+sum);
                        }
                    }catch (Exception ex){}
                    //totalOnHoldBuy.setText("Total On Hold Buy : "+totalMoney1);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            totalOnHoldBuyQuery.addListenerForSingleValueEvent(singleValue3);

                }
    }
    ValueEventListener singleValue4;
    public void ppbs(View view){
        if (checkOnHoldButtonBuy == 0){
            alertDialog("Click Upper Button","Before Click It You Need To Click On Hold Buy Button!!");
        }
        else{
            tppbsb.setVisibility(View.GONE);
            totalHoldPaymentBuyPay.setVisibility(View.VISIBLE);
            trpbsb.setVisibility(View.GONE);
            totalOnHoldPaymentAfterPayBuy.setVisibility(View.VISIBLE);
            for (int h = 0; h < datesList.size(); h++) {
                String date = datesList.get(h);

                Query totalHoldPaymentBuyPayQuery = mPayToSupplier.orderByChild("date").equalTo(date);
                final double finalTotalMoney = onHoldMoneySupplier;
                singleValue4  = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        double totalMoney2 = 0;
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            try {
                                totalMoney2 += Double.parseDouble(dataSnapshot1.child("money").getValue(String.class));
                            } catch (Exception ex) {
                            }
                        }
                        try {
                            String st = totalMoney2 + "";
                            if (st.isEmpty()) {
                                double sum = sumAll(tHoldPaymentBuyPay);
                                totalHoldPaymentBuyPay.setText("Hold Payment Paid To Suppliers : " + sum);
                            } else {
                                tHoldPaymentBuyPay.add(totalMoney2 + "");
                                double sum = sumAll(tHoldPaymentBuyPay);
                                totalHoldPaymentBuyPay.setText("Hold Payment Paid To Suppliers : " + sum);
                            }
                        } catch (Exception ex) {
                        }

                        //totalHoldPaymentBuyPay.setText("Hold Payment Paid To Suppliers : "+totalMoney2);

                        double result = finalTotalMoney - totalMoney2;

                        try {
                            String st = result + "";
                            if (st.isEmpty()) {
                                double sum = sumAll(tOnHoldPaymentAfterPayBuy);
                                totalOnHoldPaymentAfterPayBuy.setText("Remaining Payment For Supplier : " + sum);
                            } else {
                                tOnHoldPaymentAfterPayBuy.add(result + "");
                                double sum = sumAll(tOnHoldPaymentAfterPayBuy);
                                totalOnHoldPaymentAfterPayBuy.setText("Remaining Payment For Supplier : " + sum);
                            }
                        } catch (Exception ex) {
                        }
                        //totalOnHoldPaymentAfterPayBuy.setText("Remaining Payment For Supplier : "+result);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
                totalHoldPaymentBuyPayQuery.addListenerForSingleValueEvent(singleValue4);
            }
            /*
            Query totalHoldPaymentBuyPayQuery = mPayToSupplier.orderByChild("date").equalTo(date);
            final double finalTotalMoney = onHoldMoneySupplier;
            totalHoldPaymentBuyPayQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    double totalMoney2 = 0;
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                    {
                        try{
                            totalMoney2+=Double.parseDouble(dataSnapshot1.child("money").getValue(String.class));
                        }catch (Exception ex){}
                    }
                    String holdPaymentPaidToSupplierText = getResources().getString(R.string.hold_payment_paid_to_supplier_text);
                    String remainingPaymentForSupplierText = getResources().getString(R.string.remaining_payment_for_supplier_text);
                    totalHoldPaymentBuyPay.setText(holdPaymentPaidToSupplierText+totalMoney2);
                    double result = finalTotalMoney - totalMoney2;
                    totalOnHoldPaymentAfterPayBuy.setText(remainingPaymentForSupplierText+result);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            */
        }

    }
    public void rpfs(View view){
        if (checkOnHoldButtonBuy == 0){
            alertDialog("Click Upper Button","Before Click It You Need To Click On Hold Buy Button!!");
        }
        else{
            tppbsb.setVisibility(View.GONE);
            totalHoldPaymentBuyPay.setVisibility(View.VISIBLE);
            trpbsb.setVisibility(View.GONE);
            totalOnHoldPaymentAfterPayBuy.setVisibility(View.VISIBLE);

            for (int h = 0; h < datesList.size(); h++) {
                String date = datesList.get(h);

                Query totalHoldPaymentBuyPayQuery = mPayToSupplier.orderByChild("date").equalTo(date);
                final double finalTotalMoney = onHoldMoneySupplier;
                totalHoldPaymentBuyPayQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        double totalMoney2 = 0;
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            try {
                                totalMoney2 += Double.parseDouble(dataSnapshot1.child("money").getValue(String.class));
                            } catch (Exception ex) {
                            }
                        }
                        try {
                            String st = totalMoney2 + "";
                            if (st.isEmpty()) {
                                double sum = sumAll(tHoldPaymentBuyPay);
                                totalHoldPaymentBuyPay.setText("Hold Payment Paid To Suppliers : " + sum);
                            } else {
                                tHoldPaymentBuyPay.add(totalMoney2 + "");
                                double sum = sumAll(tHoldPaymentBuyPay);
                                totalHoldPaymentBuyPay.setText("Hold Payment Paid To Suppliers : " + sum);
                            }
                        } catch (Exception ex) {
                        }

                        //totalHoldPaymentBuyPay.setText("Hold Payment Paid To Suppliers : "+totalMoney2);

                        double result = finalTotalMoney - totalMoney2;

                        try {
                            String st = result + "";
                            if (st.isEmpty()) {
                                double sum = sumAll(tOnHoldPaymentAfterPayBuy);
                                totalOnHoldPaymentAfterPayBuy.setText("Remaining Payment For Supplier : " + sum);
                            } else {
                                tOnHoldPaymentAfterPayBuy.add(result + "");
                                double sum = sumAll(tOnHoldPaymentAfterPayBuy);
                                totalOnHoldPaymentAfterPayBuy.setText("Remaining Payment For Supplier : " + sum);
                            }
                        } catch (Exception ex) {
                        }
                        //totalOnHoldPaymentAfterPayBuy.setText("Remaining Payment For Supplier : "+result);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
/*
            Query totalHoldPaymentBuyPayQuery = mPayToSupplier.orderByChild("date").equalTo(date);
            final double finalTotalMoney = onHoldMoneySupplier;
            totalHoldPaymentBuyPayQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    double totalMoney2 = 0;
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                    {
                        try{
                            totalMoney2+=Double.parseDouble(dataSnapshot1.child("money").getValue(String.class));
                        }catch (Exception ex){}
                    }
                    String holdPaymentPaidToSupplierText = getResources().getString(R.string.hold_payment_paid_to_supplier_text);
                    String remainingPaymentForSupplierText = getResources().getString(R.string.remaining_payment_for_supplier_text);
                    totalHoldPaymentBuyPay.setText(holdPaymentPaidToSupplierText+totalMoney2);
                    double result = finalTotalMoney - totalMoney2;
                    totalOnHoldPaymentAfterPayBuy.setText(remainingPaymentForSupplierText+result);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            */
        }


    }

    public void tgp(View view){
        checktnp = 1;
        tsb.setVisibility(View.GONE);
        totalSell.setVisibility(View.VISIBLE);
        tgpb.setVisibility(View.GONE);
        totalGrossProfit.setVisibility(View.VISIBLE);
        for (int h = 0; h < datesList.size(); h++) {
            String date = datesList.get(h);

            Query totalSellQuery = mSellRef.orderByChild("date").equalTo(date);
            totalSellQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    double priceTotal = 0;
                    sellProductList.clear();
                    sellQuantityList.clear();
                    sellPriceListSell.clear();
                    buyPriceListSell.clear();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                    {
                        try
                        {
                            priceTotal += Double.parseDouble(dataSnapshot1.child("price").getValue(String.class));
                            sellQuantityList.add(dataSnapshot1.child("quantity").getValue(String.class));
                            sellPriceListSell.add(dataSnapshot1.child("productSellPrice").getValue(String.class));
                            buyPriceListSell.add(dataSnapshot1.child("productBuyPrice").getValue(String.class));
                        }catch (Exception ex){}
                    }
                    try {
                        String st = priceTotal+"";
                        if (st.isEmpty())
                        {

                        }
                        else {
                            tSell.add(priceTotal+"");
                            double sum = sumAll(tSell);
                            totalSell.setText("Total Sell : "+sum);
                        }
                    }catch (Exception ex){}
                    //totalSell.setText("Total Sell : "+priceTotal);

                    String SellQuantityList[] = new String[sellQuantityList.size()];
                    String SellPriceListSell[] = new String[sellPriceListSell.size()];
                    String BuyPriceListSell[] = new String[buyPriceListSell.size()];

                    double totalSellMoney = 0;
                    double totalBuyMoney = 0;

                    for (int i = 0; i < sellQuantityList.size(); i++)
                    {
                        SellQuantityList[i] = sellQuantityList.get(i);
                        SellPriceListSell[i] = sellPriceListSell.get(i);
                        BuyPriceListSell[i] = buyPriceListSell.get(i);

                        try{
                            int quan = (int)Double.parseDouble(SellQuantityList[i]);

                            double SellMoney = quan * Double.parseDouble(SellPriceListSell[i]);
                            double BuyMoney = quan * Double.parseDouble(BuyPriceListSell[i]);

                            totalSellMoney += SellMoney;
                            totalBuyMoney += BuyMoney;

                        }catch (Exception ex){}
                    }
                    final double grossProfit = totalSellMoney - totalBuyMoney;
                    try {
                        String st = grossProfit+"";
                        if (st.isEmpty())
                        {

                        }
                        else {
                            tGrossProfit.add(grossProfit+"");
                            double sum = sumAll(tGrossProfit);
                            grossProfitTotal = sum;
                            totalGrossProfit.setText("Gross Profit : "+sum);
                        }
                    }catch (Exception ex){}
                    //totalGrossProfit.setText("Gross Profit : "+grossProfit);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
                /*
        Query totalSellQuery = mSellRef.orderByChild("date").equalTo(date);
        totalSellQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double priceTotal = 0;
                sellProductList.clear();
                sellQuantityList.clear();
                sellPriceListSell.clear();
                buyPriceListSell.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    try
                    {
                        priceTotal += Double.parseDouble(dataSnapshot1.child("price").getValue(String.class));
                        sellQuantityList.add(dataSnapshot1.child("quantity").getValue(String.class));
                        sellPriceListSell.add(dataSnapshot1.child("productSellPrice").getValue(String.class));
                        buyPriceListSell.add(dataSnapshot1.child("productBuyPrice").getValue(String.class));
                    }catch (Exception ex){}
                }

                String totalSellText = getResources().getString(R.string.total_sell_text);
                totalSell.setText(totalSellText+priceTotal);


                String SellQuantityList[] = new String[sellQuantityList.size()];
                String SellPriceListSell[] = new String[sellPriceListSell.size()];
                String BuyPriceListSell[] = new String[buyPriceListSell.size()];

                double totalSellMoney = 0;
                double totalBuyMoney = 0;

                for (int i = 0; i < sellQuantityList.size(); i++)
                {
                    SellQuantityList[i] = sellQuantityList.get(i);
                    SellPriceListSell[i] = sellPriceListSell.get(i);
                    BuyPriceListSell[i] = buyPriceListSell.get(i);

                    try{
                        int quan = (int)Double.parseDouble(SellQuantityList[i]);

                        double SellMoney = quan * Double.parseDouble(SellPriceListSell[i]);
                        double BuyMoney = quan * Double.parseDouble(BuyPriceListSell[i]);

                        totalSellMoney += SellMoney;
                        totalBuyMoney += BuyMoney;

                    }catch (Exception ex){}
                }
                double grossProfit = totalSellMoney - totalBuyMoney;
                grossProfitTotal = grossProfit;
                String grossProfitText = getResources().getString(R.string.gross_profit_text);
                totalGrossProfit.setText(grossProfitText+grossProfit);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        */
    }
    public void tcgp(View view){

        checktcnp = 1;
        tcsb.setVisibility(View.GONE);
        totalCashSell.setVisibility(View.VISIBLE);
        tcgpb.setVisibility(View.GONE);
        totalGrossProfitCash.setVisibility(View.VISIBLE);

        for (int h = 0; h < datesList.size(); h++) {
            String date = datesList.get(h);
            Query totalCashSellQuery = mSellRef.orderByChild("date_mode").equalTo(date+"_Cash");
            totalCashSellQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    double totalMoney = 0;
                    ArrayList<String> sQuantityList = new ArrayList<>();
                    ArrayList<String> sellPriceListS = new ArrayList<>();
                    ArrayList<String> buyPriceListS = new ArrayList<>();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                    {
                        try{
                            totalMoney += Double.parseDouble(dataSnapshot1.child("price").getValue(String.class));
                            sQuantityList.add(dataSnapshot1.child("quantity").getValue(String.class));
                            sellPriceListS.add(dataSnapshot1.child("productSellPrice").getValue(String.class));
                            buyPriceListS.add(dataSnapshot1.child("productBuyPrice").getValue(String.class));
                        }catch (Exception ex){}
                    }
                    try {
                        String st = totalMoney+"";
                        if (st.isEmpty())
                        {

                        }
                        else {
                            tCashSell.add(totalMoney+"");
                            double sum = sumAll(tCashSell);
                            totalCashSell.setText("Total Cash Sell : "+sum);
                        }
                    }catch (Exception ex){}
                    //totalCashSell.setText("Total Cash Sell : "+totalMoney);

                    String SellQuantityList[] = new String[sQuantityList.size()];
                    String SellPriceListSell[] = new String[sellPriceListS.size()];
                    String BuyPriceListSell[] = new String[buyPriceListS.size()];

                    double totalSellMoney = 0;
                    double totalBuyMoney = 0;

                    for (int i = 0; i < sQuantityList.size(); i++)
                    {
                        SellQuantityList[i] = sQuantityList.get(i);
                        SellPriceListSell[i] = sellPriceListS.get(i);
                        BuyPriceListSell[i] = buyPriceListS.get(i);

                        try{
                            int quan = (int)Double.parseDouble(SellQuantityList[i]);

                            double SellMoney = quan * Double.parseDouble(SellPriceListSell[i]);
                            double BuyMoney = quan * Double.parseDouble(BuyPriceListSell[i]);

                            totalSellMoney += SellMoney;
                            totalBuyMoney += BuyMoney;

                        }catch (Exception ex){}
                    }
                    final double grossProfit = totalSellMoney - totalBuyMoney;
                    try {
                        String st = grossProfit+"";
                        if (st.isEmpty())
                        {

                        }
                        else {
                            tGrossProfitCash.add(grossProfit+"");
                            double sum = sumAll(tGrossProfitCash);
                            grossProfitCash = sum;
                            totalGrossProfitCash.setText("Gross Profit Cash : "+sum);
                        }
                    }catch (Exception ex){}
                    //totalGrossProfitCash.setText("Gross Profit Cash : "+grossProfit);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
                /*
            Query totalCashSellQuery = mSellRef.orderByChild("date_mode").equalTo(date+"_Cash");
            totalCashSellQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double totalMoney = 0;
                sQuantityList.clear();
                sellPriceListS.clear();
                buyPriceListS.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    try{
                        totalMoney += Double.parseDouble(dataSnapshot1.child("price").getValue(String.class));
                        sQuantityList.add(dataSnapshot1.child("quantity").getValue(String.class));
                        sellPriceListS.add(dataSnapshot1.child("productSellPrice").getValue(String.class));
                        buyPriceListS.add(dataSnapshot1.child("productBuyPrice").getValue(String.class));
                    }catch (Exception ex){}
                }
                String totalCashSellText = getResources().getString(R.string.total_cash_sell_text);
                totalCashSell.setText(totalCashSellText+totalMoney);

                String SellQuantityList[] = new String[sQuantityList.size()];
                String SellPriceListSell[] = new String[sellPriceListS.size()];
                String BuyPriceListSell[] = new String[buyPriceListS.size()];

                double totalSellMoney = 0;
                double totalBuyMoney = 0;

                for (int i = 0; i < sQuantityList.size(); i++)
                {
                    SellQuantityList[i] = sQuantityList.get(i);
                    SellPriceListSell[i] = sellPriceListS.get(i);
                    BuyPriceListSell[i] = buyPriceListS.get(i);

                    try{
                        int quan = (int)Double.parseDouble(SellQuantityList[i]);

                        double SellMoney = quan * Double.parseDouble(SellPriceListSell[i]);
                        double BuyMoney = quan * Double.parseDouble(BuyPriceListSell[i]);

                        totalSellMoney += SellMoney;
                        totalBuyMoney += BuyMoney;

                    }catch (Exception ex){}
                }
                final double grossProfit = totalSellMoney - totalBuyMoney;
                grossProfitCash = grossProfit;
                String grossProfitCashText = getResources().getString(R.string.gross_profit_cash_text);
                totalGrossProfitCash.setText(grossProfitCashText+grossProfit);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
*/
    }
    public void tohgp(View view){
        tohgpb.setVisibility(View.GONE);
        totalGrossProfitOnHold.setVisibility(View.VISIBLE);
        tohsb.setVisibility(View.GONE);
        totalOnHoldSell.setVisibility(View.VISIBLE);

        for (int h = 0; h < datesList.size(); h++) {
            String date = datesList.get(h);
            Query totalOnHoldSellQuery = mSellRef.orderByChild("date_mode").equalTo(date+"_On Hold");
            totalOnHoldSellQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    double totalMoney1 = 0;
                    ArrayList<String> sQuantityList = new ArrayList<>();
                    ArrayList<String> sellPriceListS = new ArrayList<>();
                    ArrayList<String> buyPriceListS = new ArrayList<>();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        try {
                            totalMoney1 += Double.parseDouble(dataSnapshot1.child("price").getValue(String.class));
                            sQuantityList.add(dataSnapshot1.child("quantity").getValue(String.class));
                            sellPriceListS.add(dataSnapshot1.child("productSellPrice").getValue(String.class));
                            buyPriceListS.add(dataSnapshot1.child("productBuyPrice").getValue(String.class));
                        } catch (Exception ex) {
                        }
                    }
                    try {
                        String st = totalMoney1 + "";
                        if (st.isEmpty()) {

                        } else {
                            tOnHoldSell.add(totalMoney1 + "");
                            double sum = sumAll(tOnHoldSell);
                            onHoldMoneyCustomer = sum;
                            checktohnp = 1;
                            checkOnHoldButtonSell = 1;
                            totalOnHoldSell.setText("Total On Hold Sell : " + sum);
                        }
                    } catch (Exception ex) {
                    }
                    //totalOnHoldSell.setText("Total On Hold Sell : "+totalMoney1);

                    String SellQuantityList[] = new String[sQuantityList.size()];
                    String SellPriceListSell[] = new String[sellPriceListS.size()];
                    String BuyPriceListSell[] = new String[buyPriceListS.size()];

                    double totalSellMoney = 0;
                    double totalBuyMoney = 0;

                    for (int i = 0; i < sQuantityList.size(); i++) {
                        SellQuantityList[i] = sQuantityList.get(i);
                        SellPriceListSell[i] = sellPriceListS.get(i);
                        BuyPriceListSell[i] = buyPriceListS.get(i);

                        try {
                            int quan = (int) Double.parseDouble(SellQuantityList[i]);

                            double SellMoney = quan * Double.parseDouble(SellPriceListSell[i]);
                            double BuyMoney = quan * Double.parseDouble(BuyPriceListSell[i]);

                            totalSellMoney += SellMoney;
                            totalBuyMoney += BuyMoney;

                        } catch (Exception ex) {
                        }
                    }
                    final double grossProfit = totalSellMoney - totalBuyMoney;
                    try {
                        String st = grossProfit + "";
                        if (st.isEmpty()) {

                        } else {
                            tGrossProfitOnHold.add(grossProfit + "");
                            double sum = sumAll(tGrossProfitOnHold);
                            grossProfitOnHold = sum;
                            totalGrossProfitOnHold.setText("Gross Profit On Hold : " + sum);
                        }
                    } catch (Exception ex) {
                    }
                    //totalGrossProfitOnHold.setText("Gross Profit On Hold : "+grossProfit);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

            /*
        Query totalOnHoldSellQuery = mSellRef.orderByChild("date_mode").equalTo(date+"_On Hold");
        totalOnHoldSellQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double totalMoney1 = 0;
                sQuantityListoh.clear();
                sellPriceListSoh.clear();
                buyPriceListSoh.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    try{
                        totalMoney1 += Double.parseDouble(dataSnapshot1.child("price").getValue(String.class));
                        sQuantityListoh.add(dataSnapshot1.child("quantity").getValue(String.class));
                        sellPriceListSoh.add(dataSnapshot1.child("productSellPrice").getValue(String.class));
                        buyPriceListSoh.add(dataSnapshot1.child("productBuyPrice").getValue(String.class));
                    }catch (Exception ex){}
                }
                onHoldMoneyCustomer = totalMoney1;
                String totalOnHoldSellText = getResources().getString(R.string.total_on_hold_sell_text);
                totalOnHoldSell.setText(totalOnHoldSellText+totalMoney1);
                String SellQuantityList[] = new String[sQuantityListoh.size()];
                String SellPriceListSell[] = new String[sellPriceListSoh.size()];
                String BuyPriceListSell[] = new String[buyPriceListSoh.size()];

                double totalSellMoney = 0;
                double totalBuyMoney = 0;

                for (int i = 0; i < sQuantityListoh.size(); i++)
                {
                    SellQuantityList[i] = sQuantityListoh.get(i);
                    SellPriceListSell[i] = sellPriceListSoh.get(i);
                    BuyPriceListSell[i] = buyPriceListSoh.get(i);

                    try{
                        int quan = (int)Double.parseDouble(SellQuantityList[i]);

                        double SellMoney = quan * Double.parseDouble(SellPriceListSell[i]);
                        double BuyMoney = quan * Double.parseDouble(BuyPriceListSell[i]);

                        totalSellMoney += SellMoney;
                        totalBuyMoney += BuyMoney;

                    }catch (Exception ex){}
                }
                double grossProfit = totalSellMoney - totalBuyMoney;
                grossProfitOnHold = grossProfit;
                String grossProfitOnHoldText = getResources().getString(R.string.gross_profit_on_hold_text);
                totalGrossProfitOnHold.setText(grossProfitOnHoldText+grossProfit);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
*/
    }
    public void gppbc(View view){
        if (checkOnHoldButtonSell == 0){
            alertDialog("Click Upper Button","Before Click It You Need To Click On Hold Sell Button!!");
        }
        else if (totalOnHoldSell.equals("Loading.....")){
            alertDialog("Wait","Wait Some Time To Load");
        }
        else {

            tgppbcb.setVisibility(View.GONE);
            totalGrossProfitWithHoldPay.setVisibility(View.VISIBLE);
            trgpcb.setVisibility(View.GONE);
            totalGrossProfitRemainAfterPay.setVisibility(View.VISIBLE);

            tppbcb.setVisibility(View.GONE);
            totalHoldPaymentSellPay.setVisibility(View.VISIBLE);
            trpbcb.setVisibility(View.GONE);
            totalOnHoldPaymentAfterPaySell.setVisibility(View.VISIBLE);

            for (int h = 0; h < datesList.size(); h++) {
                String date = datesList.get(h);
                Query totalHoldPaymentSellPayQuery = mPayByCustomer.orderByChild("date").equalTo(date);
                final double finalTotalMoney = onHoldMoneyCustomer;
                totalHoldPaymentSellPayQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        double totalMoney2 = 0;
                        double totalProfit = 0;
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            try {
                                totalMoney2 += Double.parseDouble(dataSnapshot1.child("money").getValue(String.class));
                                totalProfit += Double.parseDouble(dataSnapshot1.child("grossProfit").getValue(String.class));
                            } catch (Exception ex) {
                            }
                        }


                        try {
                            String st = totalProfit + "";
                            if (st.isEmpty()) {

                            } else {
                                tGrossProfitWithHoldPay.add(totalProfit + "");
                                double sum = sumAll(tGrossProfitWithHoldPay);
                                totalGrossProfitWithHoldPay.setText("Gross Profit On Hold Customer Pay : " + sum);
                            }
                        } catch (Exception ex) {
                        }
                        //totalGrossProfitWithHoldPay.setText("Gross Profit On Hold Customer Pay : "+totalProfit);
                        try {
                            String st = totalMoney2 + "";
                            if (st.isEmpty()) {

                            } else {
                                tHoldPaymentSellPay.add(totalMoney2 + "");
                                double sum = sumAll(tHoldPaymentSellPay);
                                totalHoldPaymentSellPay.setText("Hold Payment Paid By Customer : " + sum);
                            }
                        } catch (Exception ex) {
                        }
                        //totalHoldPaymentSellPay.setText("Hold Payment Paid By Customer : "+totalMoney2);
                        double result = finalTotalMoney - totalMoney2;
                        final double resultGross = grossProfitOnHold - totalProfit;
                        try {
                            String st = result + "";
                            if (st.isEmpty()) {

                            } else {
                                tOnHoldPaymentAfterPaySell.add(result + "");
                                double sum = sumAll(tOnHoldPaymentAfterPaySell);
                                totalOnHoldPaymentAfterPaySell.setText("Remaining Payment From Customer : " + sum);
                            }
                        } catch (Exception ex) {
                        }
                        //totalOnHoldPaymentAfterPaySell.setText("Remaining Payment From Customer : "+result);
                        try {
                            String st = resultGross + "";
                            if (st.isEmpty()) {

                            } else {
                                tGrossProfitRemainAfterPay.add(resultGross + "");
                                double sum = sumAll(tGrossProfitRemainAfterPay);
                                totalGrossProfitRemainAfterPay.setText("Remaining Profit From Customer : " + sum);
                            }
                        } catch (Exception ex) {
                        }
                        //totalGrossProfitRemainAfterPay.setText("Remaining Profit From Customer : "+resultGross);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
                /*
                    Query totalHoldPaymentSellPayQuery = mPayByCustomer.orderByChild("date").equalTo(date);
            final double finalTotalMoney = onHoldMoneyCustomer;
            totalHoldPaymentSellPayQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    double totalMoney2 = 0;
                    double totalProfit = 0;
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                    {
                        try{
                            totalMoney2+=Double.parseDouble(dataSnapshot1.child("money").getValue(String.class));
                            totalProfit+=Double.parseDouble(dataSnapshot1.child("grossProfit").getValue(String.class));
                        }catch (Exception ex){}
                    }
                    String holdPaymentPaidByCustomerText = getResources().getString(R.string.hold_payment_paid_by_customer_text);
                    totalHoldPaymentSellPay.setText(holdPaymentPaidByCustomerText+totalMoney2);
                    double result = finalTotalMoney - totalMoney2;
                    String remainingPaymentFromCustomerText = getResources().getString(R.string.remaining_payment_from_customer_text);
                    totalOnHoldPaymentAfterPaySell.setText(remainingPaymentFromCustomerText+result);

                    String grossProfitOnHoldCustomerPayText = getResources().getString(R.string.gross_profit_on_hold_customer_pay_text);
                    totalGrossProfitWithHoldPay.setText(grossProfitOnHoldCustomerPayText+totalProfit);
                    final double resultGross = grossProfitOnHold - totalProfit;
                    String remainingProfitFromCustomerText = getResources().getString(R.string.remaining_profit_from_customer_text);
                    totalGrossProfitRemainAfterPay.setText(remainingProfitFromCustomerText+resultGross);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            */
        }
    }
    public void rgpfc(View view){
        if (checkOnHoldButtonSell == 0){
            alertDialog("Click Upper Button","Before Click It You Need To Click On Hold Sell Button!!");
        }
        else if (totalOnHoldSell.equals("Loading.....")){
            alertDialog("Wait","Wait Some Time To Load");
        }
        else {

            tgppbcb.setVisibility(View.GONE);
            totalGrossProfitWithHoldPay.setVisibility(View.VISIBLE);
            trgpcb.setVisibility(View.GONE);
            totalGrossProfitRemainAfterPay.setVisibility(View.VISIBLE);

            tppbcb.setVisibility(View.GONE);
            totalHoldPaymentSellPay.setVisibility(View.VISIBLE);
            trpbcb.setVisibility(View.GONE);
            totalOnHoldPaymentAfterPaySell.setVisibility(View.VISIBLE);

            for (int h = 0; h < datesList.size(); h++) {
                String date = datesList.get(h);
                Query totalHoldPaymentSellPayQuery = mPayByCustomer.orderByChild("date").equalTo(date);
                final double finalTotalMoney = onHoldMoneyCustomer;
                totalHoldPaymentSellPayQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        double totalMoney2 = 0;
                        double totalProfit = 0;
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            try {
                                totalMoney2 += Double.parseDouble(dataSnapshot1.child("money").getValue(String.class));
                                totalProfit += Double.parseDouble(dataSnapshot1.child("grossProfit").getValue(String.class));
                            } catch (Exception ex) {
                            }
                        }


                        try {
                            String st = totalProfit + "";
                            if (st.isEmpty()) {

                            } else {
                                tGrossProfitWithHoldPay.add(totalProfit + "");
                                double sum = sumAll(tGrossProfitWithHoldPay);
                                totalGrossProfitWithHoldPay.setText("Gross Profit On Hold Customer Pay : " + sum);
                            }
                        } catch (Exception ex) {
                        }
                        //totalGrossProfitWithHoldPay.setText("Gross Profit On Hold Customer Pay : "+totalProfit);
                        try {
                            String st = totalMoney2 + "";
                            if (st.isEmpty()) {

                            } else {
                                tHoldPaymentSellPay.add(totalMoney2 + "");
                                double sum = sumAll(tHoldPaymentSellPay);
                                totalHoldPaymentSellPay.setText("Hold Payment Paid By Customer : " + sum);
                            }
                        } catch (Exception ex) {
                        }
                        //totalHoldPaymentSellPay.setText("Hold Payment Paid By Customer : "+totalMoney2);
                        double result = finalTotalMoney - totalMoney2;
                        final double resultGross = grossProfitOnHold - totalProfit;
                        try {
                            String st = result + "";
                            if (st.isEmpty()) {

                            } else {
                                tOnHoldPaymentAfterPaySell.add(result + "");
                                double sum = sumAll(tOnHoldPaymentAfterPaySell);
                                totalOnHoldPaymentAfterPaySell.setText("Remaining Payment From Customer : " + sum);
                            }
                        } catch (Exception ex) {
                        }
                        //totalOnHoldPaymentAfterPaySell.setText("Remaining Payment From Customer : "+result);
                        try {
                            String st = resultGross + "";
                            if (st.isEmpty()) {

                            } else {
                                tGrossProfitRemainAfterPay.add(resultGross + "");
                                double sum = sumAll(tGrossProfitRemainAfterPay);
                                totalGrossProfitRemainAfterPay.setText("Remaining Profit From Customer : " + sum);
                            }
                        } catch (Exception ex) {
                        }
                        //totalGrossProfitRemainAfterPay.setText("Remaining Profit From Customer : "+resultGross);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }/*
            Query totalHoldPaymentSellPayQuery = mPayByCustomer.orderByChild("date").equalTo(date);
            final double finalTotalMoney = onHoldMoneyCustomer;
            totalHoldPaymentSellPayQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    double totalMoney2 = 0;
                    double totalProfit = 0;
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                    {
                        try{
                            totalMoney2+=Double.parseDouble(dataSnapshot1.child("money").getValue(String.class));
                            totalProfit+=Double.parseDouble(dataSnapshot1.child("grossProfit").getValue(String.class));
                        }catch (Exception ex){}
                    }
                    String holdPaymentPaidByCustomerText = getResources().getString(R.string.hold_payment_paid_by_customer_text);
                    totalHoldPaymentSellPay.setText(holdPaymentPaidByCustomerText+totalMoney2);
                    double result = finalTotalMoney - totalMoney2;
                    String remainingPaymentFromCustomerText = getResources().getString(R.string.remaining_payment_from_customer_text);
                    totalOnHoldPaymentAfterPaySell.setText(remainingPaymentFromCustomerText+result);

                    String grossProfitOnHoldCustomerPayText = getResources().getString(R.string.gross_profit_on_hold_customer_pay_text);
                    totalGrossProfitWithHoldPay.setText(grossProfitOnHoldCustomerPayText+totalProfit);
                    final double resultGross = grossProfitOnHold - totalProfit;
                    String remainingProfitFromCustomerText = getResources().getString(R.string.remaining_profit_from_customer_text);
                    totalGrossProfitRemainAfterPay.setText(remainingProfitFromCustomerText+resultGross);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            */
        }
    }

    public void tnp(View view){
        if (checktnp == 0){
            alertDialog("Click Upper Button","Before Click It You Need To Click Gross Profit or Total Sell Button!!");
        }else {
            tnpb.setVisibility(View.GONE);
            totalNetProfit.setVisibility(View.VISIBLE);
            double netProfit = grossProfitTotal - exp;
            String netProfitText = getResources().getString(R.string.net_profit_text);
            totalNetProfit.setText(netProfitText+netProfit);
        }
    }
    public void tcnp(View view){
        if (checktcnp == 0){
            alertDialog("Click Upper Button","Before Click It You Need To Click Cash Gross Profit or Total Cash Sell Button!!");
        }else{
            tcnpb.setVisibility(View.GONE);
            totalNetProfitCash.setVisibility(View.VISIBLE);
            double netProfit = grossProfitCash - exp;
            String netProfitCashText = getResources().getString(R.string.net_profit_cash_text);
            totalNetProfitCash.setText(netProfitCashText+netProfit);
        }

    }
    public void tohnp(View view){
        if (checktohnp == 0){
            alertDialog("Click Upper Button","Before Click It You Need To Click On Hold Gross Profit or Total On HOld Sell Button!!");
        }else{
            tohnpb.setVisibility(View.GONE);
            totalNetProfitOnHold.setVisibility(View.VISIBLE);
            double netProfit = grossProfitOnHold - exp;
            String netProfitOnHoldText = getResources().getString(R.string.net_profit_on_hold_text);
            totalNetProfitOnHold.setText(netProfitOnHoldText+netProfit);
        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference mBuyRef = mRootRef.child(user.getUid()+"/buy");
        DatabaseReference mSellRef = mRootRef.child(user.getUid()+"/sell");
        final DatabaseReference mExpRef = mRootRef.child(user.getUid()+"/Expenditure");
        final DatabaseReference mPayToSupplier = mRootRef.child(user.getUid()+"/payToSupplier");
        final DatabaseReference mPayByCustomer = mRootRef.child(user.getUid()+"/payByCustomer");

        for (int h = 0; h < datesList.size(); h++)
        {
            final String date = datesList.get(h);
        /*
                    Query totalExpQuery = mExpRef.orderByChild("date").equalTo(date);
                    totalExpQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            double priceTotal = 0;
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                            {
                                try
                                {
                                    priceTotal += Double.parseDouble(dataSnapshot1.child("money").getValue(String.class));
                                }catch (Exception ex){}
                            }
                            double netProfit = grossProfit - priceTotal;
                            try {
                                String st = netProfit+"";
                                if (st.isEmpty())
                                {

                                }
                                else {
                                    tNetProfit.add(netProfit+"");
                                    double sum = sumAll(tNetProfit);
                                    totalNetProfit.setText("Net Profit : "+sum);
                                }
                            }catch (Exception ex){}
                            //totalNetProfit.setText("Net Profit : "+netProfit);






                    Query totalExpQuery = mExpRef.orderByChild("date").equalTo(date);
                    totalExpQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            double priceTotal = 0;
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                            {
                                try
                                {
                                    priceTotal += Double.parseDouble(dataSnapshot1.child("money").getValue(String.class));
                                }catch (Exception ex){}
                            }
                            double netProfit = grossProfit - priceTotal;
                            try {
                                String st = netProfit+"";
                                if (st.isEmpty())
                                {

                                }
                                else {
                                    tNetProfitCash.add(netProfit+"");
                                    double sum = sumAll(tNetProfitCash);
                                    totalNetProfitCash.setText("Net Profit Cash : "+sum);
                                }
                            }catch (Exception ex){}
                            //totalNetProfitCash.setText("Net Profit Cash : "+netProfit);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

                    Query totalExpQuery = mExpRef.orderByChild("date").equalTo(date);
                    totalExpQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            double priceTotal = 0;
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                            {
                                try
                                {
                                    priceTotal += Double.parseDouble(dataSnapshot1.child("money").getValue(String.class));
                                }catch (Exception ex){}
                            }
                            double netProfit = grossProfit - priceTotal;
                            try {
                                String st = netProfit+"";
                                if (st.isEmpty())
                                {

                                }
                                else {
                                    tNetProfitOnHold.add(netProfit+"");
                                    double sum = sumAll(tNetProfitOnHold);
                                    totalNetProfitOnHold.setText("Net Profit On Hold : "+sum);
                                }
                            }catch (Exception ex){}
                            //totalNetProfitOnHold.setText("Net Profit On Hold : "+netProfit);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    Query totalExpQuery = mExpRef.orderByChild("date").equalTo(date);
                    final double finalTotalProfit = totalProfit;
                    totalExpQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    double priceTotal = 0;
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                                    {
                                        try
                                        {
                                            priceTotal += Double.parseDouble(dataSnapshot1.child("money").getValue(String.class));
                                        }catch (Exception ex){}
                                    }
                                    double netProfit = finalTotalProfit - priceTotal;
                                    double netProfit2 = resultGross - priceTotal;
                                    try {
                                        String st = netProfit+"";
                                        if (st.isEmpty())
                                        {

                                        }
                                        else {
                                            tNetProfitWithHoldPay.add(netProfit+"");
                                            double sum = sumAll(tNetProfitWithHoldPay);
                                            totalNetProfitWithHoldPay.setText("Net Profit On Hold Customer Pay : "+sum);
                                        }
                                    }catch (Exception ex){}
                                    //totalNetProfitWithHoldPay.setText("Net Profit On Hold Customer Pay : "+netProfit);
                                    try {
                                        String st = netProfit2+"";
                                        if (st.isEmpty())
                                        {

                                        }
                                        else {
                                            tNetProfitRemainAfterPay.add(netProfit2+"");
                                            double sum = sumAll(tNetProfitRemainAfterPay);
                                            totalNetProfitRemainAfterPay.setText("Remaining Net Profit : "+sum);
                                        }
                                    }catch (Exception ex){}
                                    //totalNetProfitRemainAfterPay.setText("Remaining Net Profit : "+netProfit2);

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                 */
            Query totalExpQuery = mExpRef.orderByChild("date").equalTo(date);
            totalExpQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    double priceTotal = 0;
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                    {
                        try
                        {
                            priceTotal += Double.parseDouble(dataSnapshot1.child("money").getValue(String.class));
                        }catch (Exception ex){}
                    }
                    try {
                        String st = priceTotal+"";
                        if (st.isEmpty())
                        {

                        }
                        else {
                            tExp.add(priceTotal+"");
                            double sum = sumAll(tExp);
                            exp = sum;
                            totalExp.setText("Total Expenditure : "+sum);
                        }
                    }catch (Exception ex){}
                    //totalExp.setText("Total Expenditure : "+priceTotal);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


    }


    public double sumAll(ArrayList<String> list)
    {
        try
        {
            double all[] = new double[list.size()];

            for (int g = 0; g<list.size(); g++)
            {
                try
                {
                    all[g] = Double.parseDouble(list.get(g));
                }catch (Exception ex)
                {

                }
            }


            double sum = 0;
            for (int k = 0; k < list.size(); k++)
            {
                sum += all[k];
            }
            return sum;
        }catch (Exception ex){}
        return 0.0;
    }

    public void List(View view)
    {


    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        NetworkConnectivityCheck.connectionCheck(this);
    }
}
