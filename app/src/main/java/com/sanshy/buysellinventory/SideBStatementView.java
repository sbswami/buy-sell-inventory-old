package com.sanshy.buysellinventory;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import static com.sanshy.buysellinventory.MyUserStaticClass.userIdMainStatic;
import static com.sanshy.buysellinventory.SideBExp.EXPEND;
import static com.sanshy.buysellinventory.SideBExp.INCOME;
import static com.sanshy.buysellinventory.SideBExp.PROFIT;

public class SideBStatementView extends AppCompatActivity {

    public TextView incomeText;
    public TextView expendText;
    public TextView profitText;
    int checkExp = 0;
    int checkIncome = 0;
    double exp = 0;
    double Income = 0;
    ArrayList<String> tExp = new ArrayList<>();
    ArrayList<String> tIncome = new ArrayList<>();
    ArrayList<String> datesList = new ArrayList<>();

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_bstatement_view);

        incomeText = findViewById(R.id.income);
        expendText = findViewById(R.id.expend);
        profitText = findViewById(R.id.profit);

        Intent intent = getIntent();
        String dates[] = intent.getStringArrayExtra("dates");

        Collections.addAll(datesList, dates);
        System.out.println(datesList);
    }

    ArrayList<Double> expList = new ArrayList<>();
    ArrayList<Double> incomeList = new ArrayList<>();
    ArrayList<Double> profitList = new ArrayList<>();

    @Override
    protected void onStart() {
        super.onStart();

        expList.clear();
        incomeList.clear();
        profitList.clear();
        MyProgressBar.ShowProgress(this);
tempo.clear();
        for (int x = 0; x < datesList.size(); x++){
            final int xFinal = x;
            String date = datesList.get(x);
            DatabaseReference mSideStatement = mRootRef.child(userIdMainStatic+"/Statement/SideBStatement/"+date);
            mSideStatement.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        String exp = (String) dataSnapshot.child(EXPEND).getValue();
                        String income = (String) dataSnapshot.child(INCOME).getValue();
                        String profit = (String) dataSnapshot.child(PROFIT).getValue();

                        double expD = Double.parseDouble(exp);
                        double incomeD = Double.parseDouble(income);
                        double profitD = Double.parseDouble(profit);

                        expList.add(expD);
                        incomeList.add(incomeD);
                        profitList.add(profitD);
                    }


                    if (xFinal==(datesList.size()-1)){
                        double totalExp = 0;
                        double totalIncome = 0;
                        double totalProfit = 0;
                        for (int z = 0; z < profitList.size(); z++){
                            totalExp+=expList.get(z);
                            totalIncome+=incomeList.get(z);
                            totalProfit+=profitList.get(z);
                        }
                        expendText.setText(getString(R.string.total_exp_)+totalExp);
                        incomeText.setText(getString(R.string.total_income_)+totalIncome);
                        profitText.setText(getString(R.string.total_profit_)+totalProfit);
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

    ArrayList<Integer> tempo = new ArrayList<>();
    @Override
    protected void onPause() {
        super.onPause();

        if (tempo.size()==0){
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        NetworkConnectivityCheck.connectionCheck(this);
    }
}
