package com.sanshy.buysellinventory;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.sanshy.buysellinventory.MyUserStaticClass.userIdMainStatic;

public class buyList extends AppCompatActivity {

    AutoCompleteTextView suggestion_box4;

    ListView listView;

    TextView remainAmount;

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    final ArrayList<String> finalDate = new ArrayList<>();
    final ArrayList<String> supplierList = new ArrayList<>();
    final ArrayList<String> Money = new ArrayList<>();
    final ArrayList<String> finalMoney = new ArrayList<>();

    ArrayList<String> Remark = new ArrayList<>();
    ArrayList<String> date = new ArrayList<>();
    ArrayList<String> money = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_list);

        suggestion_box4 = findViewById(R.id.suggestion_box4);
        listView = findViewById(R.id.listView);
        remainAmount = findViewById(R.id.remainAmount);
//        AdView adView1;
//        adView1 = findViewById(R.id.adView);
//
//        adView1.loadAd(new AdRequest.Builder().build());
    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference mBuyRef = mRootRef.child(userIdMainStatic+"/buy");
        final ArrayList<String> DateSupplier = new ArrayList<>();
        final Set<String> hs = new HashSet<>();
        MyProgressBar.ShowProgress(buyList.this);
tempo.clear();
        mBuyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                finalDate.clear();
                supplierList.clear();
                finalMoney.clear();
                DateSupplier.clear();
                hs.clear();
                Money.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    DateSupplier.add(dataSnapshot1.child("date_supplierName").getValue(String.class));
                }
                hs.addAll(DateSupplier);
                DateSupplier.clear();
                DateSupplier.addAll(hs);
                hs.clear();
                String dateValue[] = new String[DateSupplier.size()+1];

                for (int i = 0; i < DateSupplier.size(); i++)
                {
                    dateValue[i] = DateSupplier.get(i);
                }

                for (int i = 0; i < DateSupplier.size(); i++)
                {
                    final int iFinal = i;
                    Query query = mBuyRef.orderByChild("date_supplierName").equalTo(DateSupplier.get(i));
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ArrayList<String> Money = new ArrayList<>();
                            String Id = "";
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                            {
                                try {
                                    Money.add(dataSnapshot1.child("price").getValue(String.class));
                                    Id = dataSnapshot1.child("bid").getValue(String.class);
                                }catch (Exception ex)
                                {
                                    System.out.println(ex);
                                }

                            }
                            double result = 0;
                            for (int i = 0; i< Money.size(); i++)
                            {
                                try
                                {
                                    result += Double.parseDouble(Money.get(i));
                                }catch (Exception ex)
                                {
                                    System.out.println(ex);
                                }
                            }
                            try
                            {
                                supplierList.add(dataSnapshot.child(Id).child("supplierName").getValue(String.class));
                                finalDate.add(dataSnapshot.child(Id).child("date").getValue(String.class));
                                finalMoney.add(result+"");

                                 String sl[] = new String[supplierList.size()+1];
                                String fd[] = new String[finalDate.size()+1];
                                String fm[] = new String[finalMoney.size()+1];

                                double grandTotal = 0;

                                for (int k = 0; k < supplierList.size(); k++)
                                {
                                    sl[k] = supplierList.get(k);
                                    fd[k] = finalDate.get(k);
                                    fm[k] = finalMoney.get(k);

                                    grandTotal += Double.parseDouble(fm[k]);
                                }

                                sl[supplierList.size()] = "Total";
                                fm[supplierList.size()] = grandTotal+"";
                                remainAmount.setText("Total Amount "+grandTotal);

                                historyPayListAdapter historyPayList = new historyPayListAdapter(buyList.this,fd,sl,fm);
                                listView.setAdapter(historyPayList);
                                if (iFinal==(DateSupplier.size()-1)){
                                    MyProgressBar.HideProgress();
tempo.add(1);

                                }
                            }catch (Exception ex)
                            {
                                System.out.println(ex);
                            }finally {
                                if (iFinal==(DateSupplier.size()-1)){
                                    MyProgressBar.HideProgress();
tempo.add(1);
                                }
                            }

                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
////////////////////////////////////////////////////////////////////////////////////////////////
//        /*
//        mBuyRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Product.clear();
//                date.clear();
//                money.clear();
//                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
//                {
//                    Product.add(dataSnapshot1.child("supplierName").getValue(String.class));
//                    date.add(dataSnapshot1.child("date").getValue(String.class));
//                    money.add(dataSnapshot1.child("price").getValue(String.class));
//                }
//                final String remarkList[] = new String[Product.size()+1];
//                String Date[] = new String[date.size()+1];
//                String Amount[] = new String[money.size()+1];
//
//                double total = 0;
//                int count = 0;
//                for (int i = 0; i < Product.size(); i++)
//                {
//                    remarkList[i] = Product.get(i);
//                    Date[i] = date.get(i);
//                    Amount[i] = money.get(i);
//                    count++;
//                    try
//                    {
//                        total += Double.parseDouble(Amount[i]);
//                    }catch (Exception es)
//                    {
//
//                    }
//                }
//
//                remarkList[Product.size()] = count+" Total";
//                Amount[money.size()] = total+"";
//
//                historyPayListAdapter historyPayList = new historyPayListAdapter(buyList.this,remarkList,Date,Amount);
//                listView.setAdapter(historyPayList);
//
//                remainAmount.setText(total+"");
//
//
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//*/
        final ArrayList<String> cList = new ArrayList<>();
        DatabaseReference mCustomerRef = mRootRef.child(userIdMainStatic+"/supplier");
        mCustomerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    cList.add(dataSnapshot1.child("name").getValue(String.class));
                }
                String list[] = new String[cList.size()];
                for (int i = 0; i < cList.size() ; i++)
                {
                    list[i] = cList.get(i);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(buyList.this,android.R.layout.simple_spinner_dropdown_item, Arrays.asList(list));
                suggestion_box4.setAdapter(adapter);
                suggestion_box4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        String text = suggestion_box4.getText().toString();
                        final DatabaseReference mBuyRef = mRootRef.child(userIdMainStatic+"/buy");
                        final ArrayList<String> DateSupplier = new ArrayList<>();
                        final Set<String> hs = new HashSet<>();
                        MyProgressBar.ShowProgress(buyList.this);
tempo.clear();
                        Query query = mBuyRef.orderByChild("supplierName").equalTo(text);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                finalDate.clear();
                                supplierList.clear();
                                finalMoney.clear();
                                DateSupplier.clear();
                                hs.clear();
                                Money.clear();
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                                {
                                    DateSupplier.add(dataSnapshot1.child("date_supplierName").getValue(String.class));
                                }
                                hs.addAll(DateSupplier);
                                DateSupplier.clear();
                                DateSupplier.addAll(hs);
                                hs.clear();
                                String dateValue[] = new String[DateSupplier.size()+1];

                                for (int i = 0; i < DateSupplier.size(); i++)
                                {
                                    dateValue[i] = DateSupplier.get(i);
                                }

                                for (int i = 0; i < DateSupplier.size(); i++)
                                {
                                    final int iFinal = i;
                                    Query query = mBuyRef.orderByChild("date_supplierName").equalTo(DateSupplier.get(i));
                                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            ArrayList<String> Money = new ArrayList<>();
                                            String Id = "";
                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                                            {
                                                try {
                                                    Money.add(dataSnapshot1.child("price").getValue(String.class));
                                                    Id = dataSnapshot1.child("bid").getValue(String.class);
                                                }catch (Exception ex)
                                                {
                                                    System.out.println(ex);
                                                }

                                            }
                                            double result = 0;
                                            for (int i = 0; i< Money.size(); i++)
                                            {
                                                try
                                                {
                                                    result += Double.parseDouble(Money.get(i));
                                                }catch (Exception ex)
                                                {
                                                    System.out.println(ex);
                                                }
                                            }
                                            try
                                            {
                                                supplierList.add(dataSnapshot.child(Id).child("supplierName").getValue(String.class));
                                                finalDate.add(dataSnapshot.child(Id).child("date").getValue(String.class));
                                                finalMoney.add(result+"");

                                                String sl[] = new String[supplierList.size()+1];
                                                String fd[] = new String[finalDate.size()+1];
                                                String fm[] = new String[finalMoney.size()+1];

                                                double grandTotal = 0;

                                                for (int k = 0; k < supplierList.size(); k++)
                                                {
                                                    sl[k] = supplierList.get(k);
                                                    fd[k] = finalDate.get(k);
                                                    fm[k] = finalMoney.get(k);

                                                    grandTotal += Double.parseDouble(fm[k]);
                                                }

                                                sl[supplierList.size()] = "Total";
                                                fm[supplierList.size()] = grandTotal+"";
                                                remainAmount.setText("Total Amount "+grandTotal);

                                                historyPayListAdapter historyPayList = new historyPayListAdapter(buyList.this,fd,sl,fm);
                                                listView.setAdapter(historyPayList);

                                                if (iFinal==(DateSupplier.size()-1)){
                                                    MyProgressBar.HideProgress();
tempo.add(1);
                                                    }
                                            }catch (Exception ex)
                                            {
                                                System.out.println(ex);
                                            }

                                            finally {
                                                if (iFinal==(DateSupplier.size()-1)){
                                                    MyProgressBar.HideProgress();
tempo.add(1);
                                                }
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

//
//                        //TODO
//                        finalDate.clear();
//                        supplierList.clear();
//                        finalMoney.clear();
//                        //TODO: Old Data From Here
//                        Query query1 = mBuyRef.orderByChild("supplierName").equalTo(text);
//                        query1.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                String Id = "";
//                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
//                                {
//                                    try {
//                                        Money.add(dataSnapshot1.child("price").getValue(String.class));
//                                        Id = dataSnapshot1.child("bid").getValue(String.class);
//                                    }catch (Exception ex)
//                                    {
//                                        System.out.println(ex);
//                                    }
//
//                                }
//                                double result = 0;
//                                for (int i = 0; i< Money.size(); i++)
//                                {
//                                    try
//                                    {
//                                        result += Double.parseDouble(Money.get(i));
//                                    }catch (Exception ex)
//                                    {
//                                        System.out.println(ex);
//                                    }
//                                }
//                                try
//                                {
//                                    supplierList.add(dataSnapshot.child(Id).child("supplierName").getValue(String.class));
//                                    finalDate.add(dataSnapshot.child(Id).child("date").getValue(String.class));
//                                    finalMoney.add(result+"");
//
//                                    String sl[] = new String[supplierList.size()+1];
//                                    String fd[] = new String[finalDate.size()+1];
//                                    String fm[] = new String[finalMoney.size()+1];
//
//                                    double grandTotal = 0;
//
//                                    for (int k = 0; k < supplierList.size(); k++)
//                                    {
//                                        sl[k] = supplierList.get(k);
//                                        fd[k] = finalDate.get(k);
//                                        fm[k] = finalMoney.get(k);
//
//                                        grandTotal += Double.parseDouble(fm[k]);
//                                    }
//
//                                    sl[supplierList.size()] = "Total";
//                                    fm[supplierList.size()] = grandTotal+"";
//
//                                    remainAmount.setText("Total Amount "+grandTotal);
//
//                                    historyPayListAdapter historyPayList = new historyPayListAdapter(buyList.this,fd,sl,fm);
//                                    listView.setAdapter(historyPayList);
//
//
//
//                                }catch (Exception ex)
//                                {
//                                    System.out.println(ex);
//                                }
//
//
//                                System.out.println(supplierList);
//                                System.out.println(finalMoney);
//                                System.out.println(finalDate);
//
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//
//                            }
//                        });
//
//                        String sl[] = new String[supplierList.size()+1];
//                        String fd[] = new String[finalDate.size()+1];
//                        String fm[] = new String[finalMoney.size()+1];
//
//                        double grandTotal = 0;
//
//                        for (int k = 0; k < supplierList.size(); k++)
//                        {
//                            sl[k] = supplierList.get(k);
//                            fd[k] = finalDate.get(k);
//                            fm[k] = finalMoney.get(k);
//
//                            grandTotal += Double.parseDouble(fm[k]);
//                        }
//
//                        sl[supplierList.size()] = "Total";
//                        fm[supplierList.size()] = grandTotal+"";
//
//                        remainAmount.setText("Total Amount "+grandTotal);
//
//                        historyPayListAdapter historyPayList = new historyPayListAdapter(buyList.this,fd,sl,fm);
//                        listView.setAdapter(historyPayList);
//

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        NetworkConnectivityCheck.connectionCheck(buyList.this);
    }

}
