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

public class StatementDay extends AppCompatActivity {

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
    String date;
    DatabaseReference mBuyRef;
    DatabaseReference mPayToSupplier;
    DatabaseReference mSellRef;
    DatabaseReference mPayByCustomer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statement_day);

        Intent intent = getIntent();
        date = intent.getStringExtra("date");

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
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public void ts(View view){
        checktnp = 1;
        tsb.setVisibility(View.GONE);
        totalSell.setVisibility(View.VISIBLE);
        tgpb.setVisibility(View.GONE);
        totalGrossProfit.setVisibility(View.VISIBLE);

        Query totalSellQuery = mSellRef.orderByChild("date").equalTo(date);
        totalSellQuery.addValueEventListener(new ValueEventListener() {
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
    }
    public void tcs(View view){

        checktcnp = 1;
        tcsb.setVisibility(View.GONE);
        totalCashSell.setVisibility(View.VISIBLE);
        tcgpb.setVisibility(View.GONE);
        totalGrossProfitCash.setVisibility(View.VISIBLE);

        Query totalCashSellQuery = mSellRef.orderByChild("date_mode").equalTo(date+"_Cash");
        totalCashSellQuery.addValueEventListener(new ValueEventListener() {
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

    }
    public void tohs(View view){

        tohgpb.setVisibility(View.GONE);
        totalGrossProfitOnHold.setVisibility(View.VISIBLE);
        tohsb.setVisibility(View.GONE);
        totalOnHoldSell.setVisibility(View.VISIBLE);

        Query totalOnHoldSellQuery = mSellRef.orderByChild("date_mode").equalTo(date+"_On Hold");
        totalOnHoldSellQuery.addValueEventListener(new ValueEventListener() {
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
                checktohnp = 1;
                checkOnHoldButtonSell = 1;
                grossProfitOnHold = grossProfit;
                String grossProfitOnHoldText = getResources().getString(R.string.gross_profit_on_hold_text);
                totalGrossProfitOnHold.setText(grossProfitOnHoldText+grossProfit);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public void ppbc(View view){
        if (checkOnHoldButtonSell == 0){
            alertDialog("Click Upper Button","Before Click It You Need To Click On Hold Sell Button!!");
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

            Query totalHoldPaymentSellPayQuery = mPayByCustomer.orderByChild("date").equalTo(date);
            final double finalTotalMoney = onHoldMoneyCustomer;
            totalHoldPaymentSellPayQuery.addValueEventListener(new ValueEventListener() {
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
        }
    }
    public void rpfc(View view){
        if (checkOnHoldButtonSell == 0){
            alertDialog("Click Upper Button","Before Click It You Need To Click On Hold Sell Button!!");
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

            Query totalHoldPaymentSellPayQuery = mPayByCustomer.orderByChild("date").equalTo(date);
            final double finalTotalMoney = onHoldMoneyCustomer;
            totalHoldPaymentSellPayQuery.addValueEventListener(new ValueEventListener() {
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
        }
    }

    public void tb(View view){
        tbb.setVisibility(View.GONE);
        totalBuy.setVisibility(View.VISIBLE);
        Query totalBuyQuery = mBuyRef.orderByChild("date").equalTo(date);
        totalBuyQuery.addValueEventListener(new ValueEventListener() {
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


                String totalBuyText = getResources().getString(R.string.total_buy_text);
                totalBuy.setText(totalBuyText+priceTotal);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void tcb(View view){
        tcbb.setVisibility(View.GONE);
        totalCashBuy.setVisibility(View.VISIBLE);
        Query totalCashBuyQuery = mBuyRef.orderByChild("date_mode").equalTo(date+"_Cash");
        totalCashBuyQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double totalMoney = 0;
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    try{
                        totalMoney += Double.parseDouble(dataSnapshot1.child("price").getValue(String.class));
                    }catch (Exception ex){}
                }
                String totalCasBuyText = getResources().getString(R.string.total_cash_buy_text);
                totalCashBuy.setText(totalCasBuyText+totalMoney);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void tohb(View view){
        tohbb.setVisibility(View.GONE);
        totalOnHoldBuy.setVisibility(View.VISIBLE);
        Query totalOnHoldBuyQuery = mBuyRef.orderByChild("date_mode").equalTo(date+"_On Hold");
        totalOnHoldBuyQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double totalMoney1 = 0;
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    try{
                        totalMoney1 += Double.parseDouble(dataSnapshot1.child("price").getValue(String.class));
                    }catch (Exception ex){}
                }
                onHoldMoneySupplier = totalMoney1;
                checkOnHoldButtonBuy =1;
                String totalOnHOldBuyText = getResources().getString(R.string.total_on_hold_buy_text);
                totalOnHoldBuy.setText(totalOnHOldBuyText+totalMoney1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void ppbs(View view){
        if (checkOnHoldButtonBuy == 0){
            alertDialog("Click Upper Button","Before Click It You Need To Click On Hold Buy Button!!");
        }
        else{
            tppbsb.setVisibility(View.GONE);
            totalHoldPaymentBuyPay.setVisibility(View.VISIBLE);
            trpbsb.setVisibility(View.GONE);
            totalOnHoldPaymentAfterPayBuy.setVisibility(View.VISIBLE);
            Query totalHoldPaymentBuyPayQuery = mPayToSupplier.orderByChild("date").equalTo(date);
            final double finalTotalMoney = onHoldMoneySupplier;
            totalHoldPaymentBuyPayQuery.addValueEventListener(new ValueEventListener() {
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
            Query totalHoldPaymentBuyPayQuery = mPayToSupplier.orderByChild("date").equalTo(date);
            final double finalTotalMoney = onHoldMoneySupplier;
            totalHoldPaymentBuyPayQuery.addValueEventListener(new ValueEventListener() {
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
        }


    }

    public void tgp(View view){

        checktnp = 1;
        tsb.setVisibility(View.GONE);
        totalSell.setVisibility(View.VISIBLE);

        tgpb.setVisibility(View.GONE);
        totalGrossProfit.setVisibility(View.VISIBLE);
        Query totalSellQuery = mSellRef.orderByChild("date").equalTo(date);
        totalSellQuery.addValueEventListener(new ValueEventListener() {
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
                final double grossProfit = totalSellMoney - totalBuyMoney;
                grossProfitTotal = grossProfit;
                String grossProfitText = getResources().getString(R.string.gross_profit_text);
                totalGrossProfit.setText(grossProfitText+grossProfit);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void tcgp(View view){

        checktcnp = 1;
        tcsb.setVisibility(View.GONE);
        totalCashSell.setVisibility(View.VISIBLE);
        tcgpb.setVisibility(View.GONE);
        totalGrossProfitCash.setVisibility(View.VISIBLE);
        Query totalCashSellQuery = mSellRef.orderByChild("date_mode").equalTo(date+"_Cash");
        totalCashSellQuery.addValueEventListener(new ValueEventListener() {
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

    }
    public void tohgp(View view){

        tohgpb.setVisibility(View.GONE);
        totalGrossProfitOnHold.setVisibility(View.VISIBLE);
        tohsb.setVisibility(View.GONE);
        totalOnHoldSell.setVisibility(View.VISIBLE);

        Query totalOnHoldSellQuery = mSellRef.orderByChild("date_mode").equalTo(date+"_On Hold");
        totalOnHoldSellQuery.addValueEventListener(new ValueEventListener() {
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

                checkOnHoldButtonSell = 1;
                checktohnp = 1;

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void gppbc(View view){

        if (checkOnHoldButtonSell == 0){
            alertDialog("Click Upper Button","Before Click It You Need To Click On Hold Gross Profit or On Hold Sell Button!!");
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

            Query totalHoldPaymentSellPayQuery = mPayByCustomer.orderByChild("date").equalTo(date);
            final double finalTotalMoney = onHoldMoneyCustomer;
            totalHoldPaymentSellPayQuery.addValueEventListener(new ValueEventListener() {
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
        }
    }
    public void rgpfc(View view){

        if (checkOnHoldButtonSell == 0){
            alertDialog("Click Upper Button","Before Click It You Need To Click On Hold Gross Profit or On Hold Sell Button!!");
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

            Query totalHoldPaymentSellPayQuery = mPayByCustomer.orderByChild("date").equalTo(date);
            final double finalTotalMoney = onHoldMoneyCustomer;
            totalHoldPaymentSellPayQuery.addValueEventListener(new ValueEventListener() {
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

        final DatabaseReference mExpRef = mRootRef.child(user.getUid()+"/Expenditure");
        Query totalExpQuery = mExpRef.orderByChild("date").equalTo(date);
        totalExpQuery.addValueEventListener(new ValueEventListener() {
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
                exp = priceTotal;
                String totalExpenditureText = getResources().getString(R.string.total_expenditure_text);
                totalExp.setText(totalExpenditureText+priceTotal);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        connectionCheck();

    }

    public void connectionCheck()
    {

        if (isInternetOn())
        {

        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Connection Problem")
                    .setMessage("Please Connect To Internet and Click OK!!!")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            connectionCheck();
                        }
                    })
                    .setCancelable(false)
                    .setNegativeButton("Close App", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            android.os.Process.killProcess(android.os.Process.myPid());
                        }
                    });
            builder.create().show();
        }
    }

    public final boolean isInternetOn() {

        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {

            // if connected with internet


            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {


            return false;
        }
        return false;
    }

}
