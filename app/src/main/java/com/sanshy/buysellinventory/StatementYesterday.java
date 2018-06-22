package com.sanshy.buysellinventory;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.util.Calendar;
import java.util.Date;

public class StatementYesterday extends AppCompatActivity {

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();

    ArrayList<String> sellProductList = new ArrayList<>();
    ArrayList<String> sellQuantityList = new ArrayList<>();
    ArrayList<String> sellPriceListSell = new ArrayList<>();
    ArrayList<String> buyPriceListSell = new ArrayList<>();

    TextView totalBuy,totalCashBuy, totalOnHoldBuy,totalHoldPaymentBuyPay,totalOnHoldPaymentAfterPayBuy;
    TextView totalSell,totalCashSell,totalOnHoldSell,totalHoldPaymentSellPay,totalOnHoldPaymentAfterPaySell;
    TextView totalGrossProfit,totalGrossProfitCash,totalGrossProfitOnHold,totalGrossProfitWithHoldPay,totalGrossProfitRemainAfterPay;
    TextView totalExp,totalNetProfit,totalNetProfitCash,totalNetProfitOnHold,totalNetProfitWithHoldPay,totalNetProfitRemainAfterPay;


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statement_day);

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

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference mBuyRef = mRootRef.child(user.getUid()+"/buy");
        DatabaseReference mSellRef = mRootRef.child(user.getUid()+"/sell");
        final DatabaseReference mExpRef = mRootRef.child(user.getUid()+"/Expenditure");
        final DatabaseReference mPayToSupplier = mRootRef.child(user.getUid()+"/payToSupplier");
        final DatabaseReference mPayByCustomer = mRootRef.child(user.getUid()+"/payByCustomer");

        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        cal.add(Calendar.DATE, -1);
        final String date = dateFormat.format(cal.getTime());

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
                String totalOnHOldBuyText = getResources().getString(R.string.total_on_hold_buy_text);
                totalOnHoldBuy.setText(totalOnHOldBuyText+totalMoney1);

                Query totalHoldPaymentBuyPayQuery = mPayToSupplier.orderByChild("date").equalTo(date);
                final double finalTotalMoney = totalMoney1;
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

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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

                String grossProfitText = getResources().getString(R.string.gross_profit_text);
                totalGrossProfit.setText(grossProfitText+grossProfit);

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
                        double netProfit = grossProfit - priceTotal;
                        String netProfitText = getResources().getString(R.string.net_profit_text);
                        totalNetProfit.setText(netProfitText+netProfit);

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

        Query totalCashSellQuery = mSellRef.orderByChild("date_mode").equalTo(date+"_Cash");
        totalCashSellQuery.addValueEventListener(new ValueEventListener() {
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

                String grossProfitCashText = getResources().getString(R.string.gross_profit_cash_text);
                totalGrossProfitCash.setText(grossProfitCashText+grossProfit);

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
                        double netProfit = grossProfit - priceTotal;
                        String netProfitCashText = getResources().getString(R.string.net_profit_cash_text);
                        totalNetProfitCash.setText(netProfitCashText+netProfit);

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

        Query totalOnHoldSellQuery = mSellRef.orderByChild("date_mode").equalTo(date+"_On Hold");
        totalOnHoldSellQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double totalMoney1 = 0;
                ArrayList<String> sQuantityList = new ArrayList<>();
                ArrayList<String> sellPriceListS = new ArrayList<>();
                ArrayList<String> buyPriceListS = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    try{
                        totalMoney1 += Double.parseDouble(dataSnapshot1.child("price").getValue(String.class));
                        sQuantityList.add(dataSnapshot1.child("quantity").getValue(String.class));
                        sellPriceListS.add(dataSnapshot1.child("productSellPrice").getValue(String.class));
                        buyPriceListS.add(dataSnapshot1.child("productBuyPrice").getValue(String.class));
                    }catch (Exception ex){}
                }
                String totalOnHoldSellText = getResources().getString(R.string.total_on_hold_sell_text);
                totalOnHoldSell.setText(totalOnHoldSellText+totalMoney1);

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

                String grossProfitOnHoldText = getResources().getString(R.string.gross_profit_on_hold_text);
                totalGrossProfitOnHold.setText(grossProfitOnHoldText+grossProfit);

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
                        double netProfit = grossProfit - priceTotal;
                        String netProfitOnHoldText = getResources().getString(R.string.net_profit_on_hold_text);
                        totalNetProfitOnHold.setText(netProfitOnHoldText+netProfit);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                Query totalHoldPaymentSellPayQuery = mPayByCustomer.orderByChild("date").equalTo(date);
                final double finalTotalMoney = totalMoney1;
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



                        String grossProfitOnHoldCustomerPayText = getResources().getString(R.string.gross_profit_on_hold_customer_pay_text);
                        totalGrossProfitWithHoldPay.setText(grossProfitOnHoldCustomerPayText+totalProfit);
                        String holdPaymentPaidByCustomerText = getResources().getString(R.string.hold_payment_paid_by_customer_text);
                        totalHoldPaymentSellPay.setText(holdPaymentPaidByCustomerText+totalMoney2);
                        double result = finalTotalMoney - totalMoney2;
                        final double resultGross = grossProfit - totalProfit;
                        String remainingPaymentFromCustomerText = getResources().getString(R.string.remaining_payment_from_customer_text);
                        totalOnHoldPaymentAfterPaySell.setText(remainingPaymentFromCustomerText+result);
                        String remainingProfitFromCustomerText = getResources().getString(R.string.remaining_profit_from_customer_text);
                        totalGrossProfitRemainAfterPay.setText(remainingProfitFromCustomerText+resultGross);

                        Query totalExpQuery = mExpRef.orderByChild("date").equalTo(date);
                        final double finalTotalProfit = totalProfit;
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
                                double netProfit = finalTotalProfit - priceTotal;
                                double netProfit2 = resultGross - priceTotal;
                                String netProfitOnHoldCustomerPayText = getResources().getString(R.string.net_profit_on_hold_customer_pay_text);
                                totalNetProfitWithHoldPay.setText(netProfitOnHoldCustomerPayText+netProfit);
                                String reminingNetProfitText = getResources().getString(R.string.remaining_net_profit_text);
                                totalNetProfitRemainAfterPay.setText(reminingNetProfitText+netProfit2);

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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
