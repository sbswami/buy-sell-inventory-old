package com.sanshy.buysellinventory;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
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

public class payByCustomer extends AppCompatActivity {

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();

    AutoCompleteTextView suggestion_box3;
    TextView remainAmount;
    EditText amount;

    String Amount = "0";
    String Profit = "0";

    ArrayList<String> customerList = new ArrayList<>();
    ArrayList<String> onHoldList = new ArrayList<>();
    ArrayList<String> grossList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_by_customer);

        suggestion_box3 = findViewById(R.id.suggestion_box3);
        amount = findViewById(R.id.amount);
        remainAmount = findViewById(R.id.remainHold);

        AdView adView1,adView2;
        adView1 = findViewById(R.id.adView);
        adView2 = findViewById(R.id.adView2);

        adView1.loadAd(new AdRequest.Builder().build());
        adView2.loadAd(new AdRequest.Builder().build());
    }

    @Override
    protected void onStart() {
        super.onStart();
        final DatabaseReference mCustomerRef = mRootRef.child(user.getUid()+"/onHoldCustomer");
        MyProgressBar.ShowProgress(this);
        mCustomerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                customerList.clear();
                onHoldList.clear();
                grossList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    customerList.add(dataSnapshot1.child("name").getValue(String.class));
                    onHoldList.add(dataSnapshot1.child("onHoldMoney").getValue(String.class));
                    grossList.add(dataSnapshot1.child("grossProfit").getValue(String.class));
                }
                final String Name[] = new String[customerList.size()];
                final String OnHoldMoney[] = new String[onHoldList.size()];
                final String GrossProfit[] = new String[grossList.size()];
                for (int i = 0; i < customerList.size(); i++)
                {
                    Name[i] = customerList.get(i);
                    OnHoldMoney[i] = onHoldList.get(i);
                    GrossProfit[i] = grossList.get(i);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(payByCustomer.this,android.R.layout.simple_spinner_dropdown_item, Arrays.asList(Name));

                suggestion_box3.setAdapter(adapter);

                MyProgressBar.HideProgress();

                final int[] index = new int[1];
                suggestion_box3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        index[0] = Arrays.asList(Name).indexOf(suggestion_box3.getText().toString());
                        remainAmount.setText("Total : "+OnHoldMoney[index[0]]);
                        amount.setText(OnHoldMoney[index[0]]);
                        Amount = OnHoldMoney[index[0]];
                        Profit = GrossProfit[index[0]];
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                MyProgressBar.HideProgress();
            }
        });


    }
    public void pay(View view)
    {
        MyProgressBar.ShowProgress(this);
        String Name = suggestion_box3.getText().toString();
        final String PayMoney = amount.getText().toString();

        if (Name.isEmpty())
        {
            suggestion_box3.setError("Please Select Customer Name");
            MyProgressBar.HideProgress();
return;
        }
        if (PayMoney.isEmpty())
        {
            amount.setError("Please Enter");
            MyProgressBar.HideProgress();
return;
        }
        if (PayMoney.equals("0")){
            MyDialogBox.ShowDialog(this,"You Can't Pay 0");
            MyProgressBar.HideProgress();
            return;
        }
        int checkCustomer = 0;
        for (int i = 0; i < customerList.size(); i++)
        {
            if (suggestion_box3.getText().toString().equals(customerList.get(i)))
            {
                checkCustomer++;
                break;
            }
        }
        if (checkCustomer == 0)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Can't Save")
                    .setMessage("Please Select From Customer List")
                    .setPositiveButton("OK",null)
                    .create()
                    .show();
            MyProgressBar.HideProgress();
return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are You Sure!!")
                .setMessage("After Paying You Will Not Able To Edit It!!!")
                .setPositiveButton("Pay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (Double.parseDouble(PayMoney) > Double.parseDouble(Amount))
                        {
                            MyDialogBox.ShowDialog(payByCustomer.this,"You Are Paying In Advance");
                        }
                        double PayProfit = 0;
                        try
                        {
                            double grossProfitRatio = Double.parseDouble(Profit)/Double.parseDouble(Amount);
                            PayProfit = grossProfitRatio * Double.parseDouble(PayMoney);
                        }
                        catch (Exception ex)
                        {

                        }
                        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        Date date = new Date();
                        String Date = dateFormat.format(date);
                        DatabaseReference mPayHistory = mRootRef.child(user.getUid()+"/payByCustomer");
                        String Id = mPayHistory.push().getKey();
                        mPayHistory.child(Id).child("payByCustomerId").setValue(Id);
                        mPayHistory.child(Id).child("name").setValue(suggestion_box3.getText().toString());
                        mPayHistory.child(Id).child("date").setValue(Date);
                        mPayHistory.child(Id).child("money").setValue(PayMoney);
                        mPayHistory.child(Id).child("dateName").setValue(Date+"_"+suggestion_box3.getText().toString());
                        mPayHistory.child(Id).child("grossProfit").setValue(PayProfit+"");



                        DatabaseReference mOnHoldCustomer = mRootRef.child(user.getUid()+"/onHoldCustomer/");
                        double result = Double.parseDouble(Amount) - Double.parseDouble(PayMoney);
                        double resultProfit = Double.parseDouble(Profit) - PayProfit;

                        mOnHoldCustomer.child(suggestion_box3.getText().toString()).child("onHoldMoney").setValue(result+"");
                        mOnHoldCustomer.child(suggestion_box3.getText().toString()).child("grossProfit").setValue(resultProfit+"");

                        final double finalPayProfit = PayProfit;
                        final double finalAmountPay = Double.parseDouble(PayMoney);
                        final DatabaseReference mStatementInventory = mRootRef.child(user.getUid()+"/Statement/Inventory/"+Date);
                        mStatementInventory.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){

                                    String tHPBCustomer = (String) dataSnapshot.child(TOTAL_HOLD_PAID_BY_CUSTOMER).getValue();
                                    String tGrossPByOnHoldCustomer = (String) dataSnapshot.child(GROSS_PROFIT_PAID_BY_ON_HOLD_CUSTOMER).getValue();

                                    double tHPBC = Double.parseDouble(tHPBCustomer);
                                    double tGPBOHC = Double.parseDouble(tGrossPByOnHoldCustomer);

                                    double nowHPBC = tHPBC + finalAmountPay;
                                    double nowGPBOHC = tGPBOHC + finalPayProfit;

                                    String saveHPBC = String.valueOf(nowHPBC);
                                    String saveGPBOHC = String.valueOf(nowGPBOHC);

                                    mStatementInventory.child(TOTAL_HOLD_PAID_BY_CUSTOMER).setValue(saveHPBC);
                                    mStatementInventory.child(GROSS_PROFIT_PAID_BY_ON_HOLD_CUSTOMER).setValue(saveGPBOHC);


                                }
                                else {

                                    String saveHPBC = String.valueOf(finalAmountPay);
                                    String saveGPBOHC = String.valueOf(finalPayProfit);

                                    mStatementInventory.child(TOTAL_HOLD_PAID_BY_CUSTOMER).setValue(saveHPBC);
                                    mStatementInventory.child(GROSS_PROFIT_PAID_BY_ON_HOLD_CUSTOMER).setValue(saveGPBOHC);

                                    mStatementInventory.child(TOTAL_EXPENDITURE).setValue("0");
                                    mStatementInventory.child(NET_PROFIT).setValue("0");
                                    mStatementInventory.child(TOTAL_CASH_BUY).setValue("0");
                                    mStatementInventory.child(TOTAL_ON_HOLD_BUY).setValue("0");
                                    mStatementInventory.child(TOTAL_BUY).setValue("0");
                                    mStatementInventory.child(TOTAL_HOLD_PAID_TO_SUPPLIER).setValue("0");
                                    mStatementInventory.child(TOTAL_SELL).setValue("0");
                                    mStatementInventory.child(TOTAL_CASH_SELL).setValue("0");
                                    mStatementInventory.child(TOTAL_ON_HOLD_SELL).setValue("0");
                                    mStatementInventory.child(GROSS_PROFIT).setValue("0");
                                    mStatementInventory.child(CASH_GROSS_PROFIT).setValue("0");
                                    mStatementInventory.child(ON_HOLD_GROSS_PROFIT).setValue("0");
                                }

                                MyProgressBar.HideProgress();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                MyProgressBar.HideProgress();
                            }
                        });

                        Toast.makeText(payByCustomer.this, "Payment Done", Toast.LENGTH_SHORT).show();
                        finish();

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MyProgressBar.HideProgress();
                    }
                })
                .create().show();
    }
    public void historyPay(View view)
    {
        Intent intent = new Intent(this,historyCustomer.class);
        startActivity(intent);
    }
    public void historyBuy(View view)
    {
        Intent intent = new Intent(this,historyCustomerSell.class);
        startActivity(intent);
    }
    public void cancel(View view)
    {
        this.finish();
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        NetworkConnectivityCheck.connectionCheck(this);
    }

}
