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
import static com.sanshy.buysellinventory.MyUserStaticClass.isPaid;
import static com.sanshy.buysellinventory.MyUserStaticClass.userIdMainStatic;

public class Expenditure extends AppCompatActivity {

    EditText money;
    AutoCompleteTextView remark;

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();

    AdView adView1,adView2;

    final ArrayList<String> hintList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenditure);

        money = findViewById(R.id.money);
        remark = findViewById(R.id.remark);

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

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference mHint = mRootRef.child(userIdMainStatic+"/remark");
        mHint.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hintList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    hintList.add(dataSnapshot1.child("hint").getValue(String.class));
                }
                try
                {
                    String list[] = new String[hintList.size()];
                    for (int i = 0; i < hintList.size() ; i++)
                    {
                        list[i] = hintList.get(i);
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Expenditure.this,android.R.layout.simple_spinner_dropdown_item,Arrays.asList(list));
                    remark.setAdapter(arrayAdapter);
                }catch (Exception e)
                {

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void undoB(View view){
        startActivity(new Intent(this,UndoExp.class));
    }

    public void save(View view)
    {
        MyProgressBar.ShowProgress(this);
        final String Money = money.getText().toString();
        String Remark = remark.getText().toString();

        if (Money.isEmpty())
        {
            money.setError(getString(R.string.fill_it));
            MyProgressBar.HideProgress();
return;
        }
        if (Remark.isEmpty())
        {
            remark.setError(getString(R.string.fill_it));
            MyProgressBar.HideProgress();
return;
        }

        int check = 0;
        for (int i = 0; i < hintList.size(); i++)
        {
            if (hintList.get(i).equals(Remark))
            {
                check++;
            }
        }
        if (check == 0)
        {
            DatabaseReference mHint = mRootRef.child(userIdMainStatic+"/remark");

            String Id = mHint.push().getKey();

            mHint.child(Id).child("id").setValue(Id);
            mHint.child(Id).child("hint").setValue(Remark);
        }
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        final String Date = dateFormat.format(date);
        DatabaseReference mExRef = mRootRef.child(userIdMainStatic+"/Expenditure");
        String Id = mExRef.push().getKey();
        mExRef.child(Id).child("id").setValue(Id);
        mExRef.child(Id).child("money").setValue(Money);
        mExRef.child(Id).child("remark").setValue(Remark);
        mExRef.child(Id).child("date").setValue(Date);
        mExRef.child(Id).child("date_remark").setValue(Date+"_"+Remark);

        final DatabaseReference mStatementInventory = mRootRef.child(userIdMainStatic+"/Statement/Inventory/"+Date);
        mStatementInventory.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double expMoney = Double.parseDouble(Money);
                if (dataSnapshot.exists()){

                    String tExpCloud = (String) dataSnapshot.child(TOTAL_EXPENDITURE).getValue();
                    String netProfitCloud = (String) dataSnapshot.child(NET_PROFIT).getValue();

                    double tExp = Double.parseDouble(tExpCloud);
                    double netPro = Double.parseDouble(netProfitCloud);

                    double nowExp = tExp + expMoney;
                    double nowNetPro = netPro - expMoney;

                    String saveExp = String.valueOf(nowExp);
                    String saveNetPro = String.valueOf(nowNetPro);

                    mStatementInventory.child(TOTAL_EXPENDITURE).setValue(saveExp);
                    mStatementInventory.child(NET_PROFIT).setValue(saveNetPro);


                }
                else {
                    double netPro = 0-expMoney;
                    String saveNetPro = String.valueOf(netPro);
                    mStatementInventory.child(TOTAL_EXPENDITURE).setValue(Money);
                    mStatementInventory.child(NET_PROFIT).setValue(saveNetPro);
                    mStatementInventory.child(TOTAL_CASH_BUY).setValue("0");
                    mStatementInventory.child(TOTAL_ON_HOLD_BUY).setValue("0");
                    mStatementInventory.child(TOTAL_BUY).setValue("0");
                    mStatementInventory.child(TOTAL_HOLD_PAID_TO_SUPPLIER).setValue("0");
                    mStatementInventory.child(TOTAL_SELL).setValue("0");
                    mStatementInventory.child(TOTAL_CASH_SELL).setValue("0");
                    mStatementInventory.child(TOTAL_ON_HOLD_SELL).setValue("0");
                    mStatementInventory.child(TOTAL_HOLD_PAID_BY_CUSTOMER).setValue("0");
                    mStatementInventory.child(GROSS_PROFIT).setValue("0");
                    mStatementInventory.child(CASH_GROSS_PROFIT).setValue("0");
                    mStatementInventory.child(ON_HOLD_GROSS_PROFIT).setValue("0");
                    mStatementInventory.child(GROSS_PROFIT_PAID_BY_ON_HOLD_CUSTOMER).setValue("0");
                }

                MyProgressBar.HideProgress();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                MyProgressBar.HideProgress();
            }
        });
        
        MyDialogBox.ShowDialog(Expenditure.this,getString(R.string.saved));
        Toast.makeText(this, getString(R.string.saved), Toast.LENGTH_SHORT).show();
        finish();
    }
    public void list(View view)
    {
        startActivity(new Intent(this,expenditureList.class));
    }
    public void cancel(View view)
    {
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        NetworkConnectivityCheck.connectionCheck(Expenditure.this);

    }

}
