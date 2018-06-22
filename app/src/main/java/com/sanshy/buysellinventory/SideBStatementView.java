package com.sanshy.buysellinventory;

import android.content.Intent;
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

public class SideBStatementView extends AppCompatActivity {

    public TextView income;
    public TextView expend;
    public TextView profit;
    public Button profitButton;
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

        income = findViewById(R.id.income);
        expend = findViewById(R.id.expend);
        profit = findViewById(R.id.profit);

        profitButton = findViewById(R.id.profitButton);

        Intent intent = getIntent();
        String dates[] = intent.getStringArrayExtra("dates");

        Collections.addAll(datesList, dates);
        System.out.println(datesList);
    }

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference mExpRef = mRootRef.child(user.getUid()+"/SideBusiness/Expenditure");
        DatabaseReference mIncomeRef = mRootRef.child(user.getUid()+"/SideBusiness/Income");
        for (int h = 0; h < datesList.size(); h++)
        {
            final String date = datesList.get(h);
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
                    try {
                        String st = priceTotal+"";
                        if (st.isEmpty())
                        {

                        }
                        else {
                            tExp.add(priceTotal+"");
                            double sum = sumAll(tExp);
                            exp = sum;
                            expend.setText("Total Expenditure : "+sum);
                            checkExp = 1;
                        }
                    }catch (Exception ex){}
                    //totalExp.setText("Total Expenditure : "+priceTotal);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        for (int h = 0; h < datesList.size(); h++)
        {
            final String date = datesList.get(h);
            Query totalExpQuery = mIncomeRef.orderByChild("date").equalTo(date);
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
                    try {
                        String st = priceTotal+"";
                        if (st.isEmpty())
                        {

                        }
                        else {
                            tIncome.add(priceTotal+"");
                            double sum = sumAll(tIncome);
                            Income = sum;
                            income.setText("Total Income : "+sum);
                            checkIncome = 1;
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        android.os.Process.killProcess(android.os.Process.myPid());
    }
    public void profitB(View view){
        if ((checkExp == 0 )&&(checkIncome == 0))
        {
            Toast.makeText(this, "Please Wait...", Toast.LENGTH_SHORT).show();
        }
        else{
            double Profit = Income - exp;
            profit.setText("Profit : "+Profit);
            profit.setVisibility(View.VISIBLE);
            profitButton.setVisibility(View.GONE);
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
}
