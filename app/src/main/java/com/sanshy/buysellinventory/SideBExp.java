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

public class SideBExp extends AppCompatActivity {

    public static final String EXPEND = "expendText";
    public static final String INCOME = "incomeText";
    public static final String PROFIT = "profitText";
    EditText money;
    AutoCompleteTextView remark;

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();


    final ArrayList<String> hintList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_bexp);

        money = findViewById(R.id.money);
        remark = findViewById(R.id.remark);
        AdView adView1,adView2;
        adView1 = findViewById(R.id.adView);
        adView2 = findViewById(R.id.adView2);

        adView1.loadAd(new AdRequest.Builder().build());
        adView2.loadAd(new AdRequest.Builder().build());
    }

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference mHint = mRootRef.child(user.getUid()+"/SideBExpremark");
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
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SideBExp.this,android.R.layout.simple_spinner_dropdown_item,Arrays.asList(list));
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
    public void save(View view){
        MyProgressBar.ShowProgress(this);
        final String Money = money.getText().toString();
        String Remark = remark.getText().toString();

        if (Money.isEmpty())
        {
            money.setError("Please Fill It");
            MyProgressBar.HideProgress();
            return;
        }
        if (Remark.isEmpty())
        {
            remark.setError("Please Fill It");
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
            DatabaseReference mHint = mRootRef.child(user.getUid()+"/SideBExpremark");

            String Id = mHint.push().getKey();

            mHint.child(Id).child("id").setValue(Id);
            mHint.child(Id).child("hint").setValue(Remark);
        }
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        final String Date = dateFormat.format(date);
        DatabaseReference mExRef = mRootRef.child(user.getUid()+"/SideBusiness/Expenditure");

        String Id = mExRef.push().getKey();
        mExRef.child(Id).child("id").setValue(Id);
        mExRef.child(Id).child("money").setValue(Money);
        mExRef.child(Id).child("remark").setValue(Remark);
        mExRef.child(Id).child("date").setValue(Date);
        mExRef.child(Id).child("date_remark").setValue(Date+"_"+Remark);

        final DatabaseReference mSideStatementRef = mRootRef.child(user.getUid()+"/Statement/SideBStatement/"+Date);
        mSideStatementRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double expMoney = Double.parseDouble(Money);
                if (dataSnapshot.exists()){

                    String ExpendCloud = (String) dataSnapshot.child(EXPEND).getValue();
                    String IncomeCloud = (String) dataSnapshot.child(INCOME).getValue();
                    String ProfitCloud = (String) dataSnapshot.child(PROFIT).getValue();

                    double expCloud = Double.parseDouble(ExpendCloud);
                    double profCloud = Double.parseDouble(ProfitCloud);

                    double nowExp = expCloud+expMoney;
                    double nowProfit = profCloud-expMoney;

                    String SaveExp = String.valueOf(nowExp);
                    String SaveProfit = String.valueOf(nowProfit);

                    mSideStatementRef.child(EXPEND).setValue(SaveExp);
                    mSideStatementRef.child(PROFIT).setValue(SaveProfit);

                }
                else {
                    double profi = 0-expMoney;
                    String SaveProfit = String.valueOf(profi);
                    mSideStatementRef.child(EXPEND).setValue(Money);
                    mSideStatementRef.child(PROFIT).setValue(SaveProfit);
                    mSideStatementRef.child(INCOME).setValue("0");

                }

                MyProgressBar.HideProgress();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                MyProgressBar.HideProgress();
            }
        });
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void undoB(View view){
        startActivity(new Intent(this,UndoSideExp.class));
    }
    public void list(View view)
    {
        startActivity(new Intent(this,SideBExpList.class));
    }
    public void cancel(View view)
    {
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        NetworkConnectivityCheck.connectionCheck(this);
    }

}
