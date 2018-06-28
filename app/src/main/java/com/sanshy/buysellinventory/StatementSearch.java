package com.sanshy.buysellinventory;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
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
import java.util.Arrays;
import java.util.Date;

import static com.sanshy.buysellinventory.Buy.CASH_GROSS_PROFIT;
import static com.sanshy.buysellinventory.Buy.GROSS_PROFIT;
import static com.sanshy.buysellinventory.Buy.GROSS_PROFIT_PAID_BY_ON_HOLD_CUSTOMER;
import static com.sanshy.buysellinventory.Buy.NET_PROFIT;
import static com.sanshy.buysellinventory.Buy.ON_HOLD_GROSS_PROFIT;
import static com.sanshy.buysellinventory.Buy.TOTAL_BUY;
import static com.sanshy.buysellinventory.Buy.TOTAL_CASH_BUY;
import static com.sanshy.buysellinventory.Buy.TOTAL_CASH_SELL;
import static com.sanshy.buysellinventory.Buy.TOTAL_EXPENDITURE;
import static com.sanshy.buysellinventory.Buy.TOTAL_HOLD_PAID_BY_CUSTOMER;
import static com.sanshy.buysellinventory.Buy.TOTAL_HOLD_PAID_TO_SUPPLIER;
import static com.sanshy.buysellinventory.Buy.TOTAL_ON_HOLD_BUY;
import static com.sanshy.buysellinventory.Buy.TOTAL_ON_HOLD_SELL;
import static com.sanshy.buysellinventory.Buy.TOTAL_SELL;
import static com.sanshy.buysellinventory.MyUserStaticClass.userIdMainStatic;

public class StatementSearch extends AppCompatActivity {

    ArrayList<Double> tCashBuy = new ArrayList<>(),tOnHoldBuy = new ArrayList<>();
    ArrayList<Double> tSell = new ArrayList<>(),tCashSell = new ArrayList<>(),tOnHoldSell = new ArrayList<>(),tHoldPaymentSellPay = new ArrayList<>(),tOnHoldPaymentAfterPaySell = new ArrayList<>();
    ArrayList<Double> tGrossProfit = new ArrayList<>(),tGrossProfitCash = new ArrayList<>(),tGrossProfitOnHold = new ArrayList<>(),tGrossProfitWithHoldPay = new ArrayList<>(),tGrossProfitRemainAfterPay = new ArrayList<>();
    ArrayList<Double> tExpenditure = new ArrayList<>(),tNetProfit = new ArrayList<>();

    ArrayList<Double> tBuy =new ArrayList<>();
    ArrayList<Double> tHoldPaymentBuyPay = new ArrayList<>();
    ArrayList<Double> tOnHoldPaymentAfterPayBuy = new ArrayList<>();
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    TextView totalBuy,totalCashBuy, totalOnHoldBuy,totalHoldPaymentBuyPay,totalOnHoldPaymentAfterPayBuy;
    TextView totalSell,totalCashSell,totalOnHoldSell,totalHoldPaymentSellPay,totalOnHoldPaymentAfterPaySell;
    TextView totalGrossProfit,totalGrossProfitCash,totalGrossProfitOnHold,totalGrossProfitWithHoldPay,totalGrossProfitRemainAfterPay;
    TextView totalExp,totalNetProfit,totalNetProfitCash,totalNetProfitOnHold,totalNetProfitWithHoldPay,totalNetProfitRemainAfterPay;

    ArrayList<String> datesList = new ArrayList<>();
    ArrayList<Integer> tempo = new ArrayList<>();
    @Override
    protected void onPause() {
        super.onPause();

        if (tempo.size()==0){
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statement_search);

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
        }

        Intent intent = getIntent();
        String dates[] = intent.getStringArrayExtra("dates");

        datesList.addAll(Arrays.asList(dates));
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

    @Override
    protected void onStart() {
        super.onStart();
        MyProgressBar.ShowProgress(this);
tempo.clear();
        for(int i = 0; i < datesList.size() ; i++ ){

            final int iFinal = i;
            String Date = datesList.get(i);
            final DatabaseReference mStatementInventory = mRootRef.child(userIdMainStatic+"/Statement/Inventory/"+Date);
            mStatementInventory.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){

                        String tBuyCloud = (String) dataSnapshot.child(TOTAL_BUY).getValue();
                        String tCashBuyCloud = (String) dataSnapshot.child(TOTAL_CASH_BUY).getValue();
                        String tOnHoldBuyCloud = (String) dataSnapshot.child(TOTAL_ON_HOLD_BUY).getValue();

                        String tExpCloud = (String) dataSnapshot.child(TOTAL_EXPENDITURE).getValue();
                        String netProfitCloud = (String) dataSnapshot.child(NET_PROFIT).getValue();

                        String tSellCloud = (String) dataSnapshot.child(TOTAL_SELL).getValue();
                        String tCashSellCloud = (String) dataSnapshot.child(TOTAL_CASH_SELL).getValue();
                        String tOnHoldSellCloud = (String) dataSnapshot.child(TOTAL_ON_HOLD_SELL).getValue();

                        String tGrossProfitCloud = (String) dataSnapshot.child(GROSS_PROFIT).getValue();
                        String tCashGrossProfitCloud = (String) dataSnapshot.child(CASH_GROSS_PROFIT).getValue();
                        String tOnHoldGrossProfitCloud = (String) dataSnapshot.child(ON_HOLD_GROSS_PROFIT).getValue();

                        String tHPBCustomer = (String) dataSnapshot.child(TOTAL_HOLD_PAID_BY_CUSTOMER).getValue();
                        String tGrossPByOnHoldCustomer = (String) dataSnapshot.child(GROSS_PROFIT_PAID_BY_ON_HOLD_CUSTOMER).getValue();

                        String tHPTSupplier = (String) dataSnapshot.child(TOTAL_HOLD_PAID_TO_SUPPLIER).getValue();

                        double tHPTS = Double.parseDouble(tHPTSupplier);

                        double tHPBC = Double.parseDouble(tHPBCustomer);
                        double tGPBOHC = Double.parseDouble(tGrossPByOnHoldCustomer);

                        double sellCloud = Double.parseDouble(tSellCloud);
                        double cashSellCloud  = Double.parseDouble(tCashSellCloud);
                        double onHoldSellCloud = Double.parseDouble(tOnHoldSellCloud);

                        double grossProfit = Double.parseDouble(tGrossProfitCloud);
                        double cashGrossProfit = Double.parseDouble(tCashGrossProfitCloud);
                        double onHoldGrossProfit = Double.parseDouble(tOnHoldGrossProfitCloud);

                        double buyCloud = Double.parseDouble(tBuyCloud);
                        double cashBuyCloud = Double.parseDouble(tCashBuyCloud);
                        double onHoldBuyCloud = Double.parseDouble(tOnHoldBuyCloud);

                        double tExp = Double.parseDouble(tExpCloud);
                        double netPro = Double.parseDouble(netProfitCloud);

                        double remainingSell = onHoldSellCloud - tHPBC;

                        double remainingBuy = onHoldBuyCloud - tHPTS;

                        double remainingGross = onHoldGrossProfit - tGPBOHC;

                        tSell.add(sellCloud);
                        tCashSell.add(cashSellCloud);
                        tOnHoldSell.add(onHoldSellCloud);
                        tHoldPaymentSellPay.add(tHPBC);
                        tOnHoldPaymentAfterPaySell.add(remainingSell);

                        tBuy.add(buyCloud);
                        tCashBuy.add(cashBuyCloud);
                        tOnHoldBuy.add(onHoldBuyCloud);
                        tHoldPaymentBuyPay.add(tHPTS);
                        tOnHoldPaymentAfterPayBuy.add(remainingBuy);

                        tGrossProfit.add(grossProfit);
                        tGrossProfitCash.add(cashGrossProfit);
                        tGrossProfitOnHold.add(onHoldGrossProfit);
                        tGrossProfitWithHoldPay.add(tGPBOHC);
                        tGrossProfitRemainAfterPay.add(remainingGross);

                        tExpenditure.add(tExp);

                        tNetProfit.add(netPro);

                    }
                    if (iFinal==(datesList.size()-1)){

                        double totalSellD = 0,totalCashSellD = 0,totalOnHoldSellD = 0,totalHPBCD = 0, totalRemainSellD = 0;
                        double totalBuyD = 0,totalCashBuyD = 0, totalOnHoldBuyD = 0, totalHPTSD = 0, totalRemainBuyD = 0;
                        double totalGrossD = 0,totalCashGrossD = 0,totalOnHoldGrossD = 0,totalGPBCD = 0,totalRemainGrossD =0;
                        double totalExpD = 0, totalNetProfitD = 0;

                        for (int x = 0; x < tNetProfit.size(); x++){
                            totalBuyD+=tBuy.get(x);
                            totalCashBuyD+=tCashBuy.get(x);
                            totalOnHoldBuyD+=tOnHoldBuy.get(x);
                            totalHPTSD+=tHoldPaymentBuyPay.get(x);
                            totalRemainBuyD+=tOnHoldPaymentAfterPayBuy.get(x);

                            totalSellD+=tSell.get(x);
                            totalCashSellD+=tCashSell.get(x);
                            totalOnHoldSellD+=tOnHoldSell.get(x);
                            totalHPBCD+=tHoldPaymentSellPay.get(x);
                            totalRemainSellD+=tOnHoldPaymentAfterPaySell.get(x);

                            totalGrossD+=tGrossProfit.get(x);
                            totalCashGrossD+=tGrossProfitCash.get(x);
                            totalOnHoldGrossD+=tGrossProfitOnHold.get(x);
                            totalGPBCD+=tGrossProfitWithHoldPay.get(x);
                            totalRemainGrossD+=tGrossProfitRemainAfterPay.get(x);

                            totalExpD+=tExpenditure.get(x);

                            totalNetProfitD+=tNetProfit.get(x);
                        }

                        totalSell.setText(getString(R.string.total_sell_text)+totalSellD);
                        totalCashSell.setText(getString(R.string.total_cash_sell_text)+totalCashSellD);
                        totalOnHoldSell.setText(getString(R.string.total_on_hold_sell_text)+totalOnHoldSellD);
                        totalHoldPaymentSellPay.setText(getString(R.string.hold_payment_paid_by_customer_text)+totalHPBCD);
                        totalOnHoldPaymentAfterPaySell.setText(getString(R.string.remaining_payment_from_customer_text)+totalRemainSellD);

                        totalBuy.setText(getString(R.string.total_buy_text)+totalBuyD);
                        totalCashBuy.setText(getString(R.string.total_cash_buy_text)+totalCashBuyD);
                        totalOnHoldBuy.setText(getString(R.string.total_on_hold_buy_text)+totalOnHoldBuyD);
                        totalHoldPaymentBuyPay.setText(getString(R.string.hold_payment_paid_to_supplier_text)+totalHPTSD);
                        totalOnHoldPaymentAfterPayBuy.setText(getString(R.string.remaining_payment_for_supplier_text)+totalRemainBuyD);

                        totalGrossProfit.setText(getString(R.string.gross_profit_text)+totalGrossD);
                        totalGrossProfitCash.setText(getString(R.string.gross_profit_cash_text)+totalCashGrossD);
                        totalGrossProfitOnHold.setText(getString(R.string.gross_profit_on_hold_text)+totalOnHoldGrossD);
                        totalGrossProfitWithHoldPay.setText(getString(R.string.gross_profit_on_hold_customer_pay_text)+totalGPBCD);
                        totalGrossProfitRemainAfterPay.setText(getString(R.string.remaining_profit_from_customer_text)+totalRemainGrossD);

                        totalExp.setText(getString(R.string.total_expenditure_text)+totalExpD);

                        totalNetProfit.setText(getString(R.string.net_profit_text)+totalNetProfitD);
                        MyProgressBar.HideProgress();
tempo.add(1);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    MyProgressBar.HideProgress();
tempo.add(1);
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        NetworkConnectivityCheck.connectionCheck(this);
    }
}
