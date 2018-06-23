package com.sanshy.buysellinventory;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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


public class Customer extends AppCompatActivity {

    ListView listView;

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();

    public ArrayList<citem> ciList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        listView = findViewById(R.id.listView);

        AdView adView1;
        adView1 = findViewById(R.id.adView);

        adView1.loadAd(new AdRequest.Builder().build());
    }

    @Override
    protected void onStart() {
        super.onStart();



        final DatabaseReference mCustomerRef = mRootRef.child(user.getUid()+"/customer");
        mCustomerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ciList.clear();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    citem ci = dataSnapshot1.getValue(citem.class);

                    ciList.add(ci);
                }
                final String Name[] = new String[ciList.size()];
                final String Phone[] = new String[ciList.size()];
                final String City[] = new String[ciList.size()];
                final String Address[] = new String[ciList.size()];
                final String NamePhone[] = new String[ciList.size()];
                for (int i = 0; i < ciList.size(); i++)
                {
                    Name[i] = ciList.get(i).getName();
                    Phone[i] = ciList.get(i).getPhone();
                    City[i] = ciList.get(i).getCity();
                    Address[i] = ciList.get(i).getAddress();
                    NamePhone[i] = ciList.get(i).getNamePhone();
                }
                try
                {
                    mySimpleListAdapter arrayAdapter = new mySimpleListAdapter(Customer.this, Name);
                    listView.setAdapter(arrayAdapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(Customer.this);
                            builder.setTitle("Customer")
                                    .setMessage("Name : " + Name[i] + "\n" +
                                            "Phone : " + Phone[i] + "\n" +
                                            "City : " + City[i] + "\n" +
                                            "Address " + Address[i])
                                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i2) {

                                            final DatabaseReference mOnHoldCustomerRef = mRootRef.child(user.getUid()+"/onHoldCustomer");
                                            Query query = mOnHoldCustomerRef.orderByChild("name").equalTo(Name[i]);
                                            final ArrayList<String> tempo = new ArrayList<>();
                                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    if (tempo.size() == 0)
                                                    {
                                                        double Money = 0;
                                                        try
                                                        {
                                                            Money = Double.parseDouble(dataSnapshot.child(Name[i]).child("onHoldMoney").getValue(String.class));

                                                        }catch (Exception ex)
                                                        {

                                                        }
                                                        if (Money == 0)
                                                        {
                                                            Query applesQuery = mCustomerRef.orderByChild("namePhone").equalTo(NamePhone[i]);
                                                            Query deleteOnHold = mOnHoldCustomerRef.orderByChild("name").equalTo(Name[i]);
                                                            final ArrayList<String> temp1 = new ArrayList<>();
                                                            final ArrayList<String> temp2 = new ArrayList<>();
                                                            applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                    if (temp1.size() == 0)
                                                                    {
                                                                        for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                                                            appleSnapshot.getRef().removeValue();
                                                                        }
                                                                    }
                                                                    temp1.add("Kuch bhi");

                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                                    Log.e("Error", "onCancelled", databaseError.toException());
                                                                }
                                                            });
                                                            deleteOnHold.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                    if (temp2.size()==0)
                                                                    {
                                                                        for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                                                            appleSnapshot.getRef().removeValue();
                                                                        }
                                                                    }
                                                                    temp2.add("Kuchh Bhi");

                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                                    Log.e("Error", "onCancelled", databaseError.toException());
                                                                }
                                                            });
                                                        }
                                                        else
                                                        {
                                                            AlertDialog.Builder builder1 = new AlertDialog.Builder(Customer.this);
                                                            builder1.setTitle("Can't Delete")
                                                                    .setMessage("Payment Still Remaining")
                                                                    .setPositiveButton("OK",null)
                                                                    .create()
                                                                    .show();
                                                        }
                                                    }
                                                    tempo.add("Kuch Bhi");
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });

                                        }
                                    })
                                    .setNegativeButton("Cancel", null);
                            builder.create().show();


                        }
                    });
                }
                catch(Exception ex)
                {
                    System.out.println("\n"+ex+"\n");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    public void addC(View view)
    {
        Intent intent = new Intent(this,addCustomer.class);
        startActivity(intent);
        this.finish();
    }
    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        NetworkConnectivityCheck.connectionCheck(Customer.this);

    }


}
