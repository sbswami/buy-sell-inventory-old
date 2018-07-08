package com.sanshy.buysellinventory;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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

import static com.sanshy.buysellinventory.MyUserStaticClass.isPaid;
import static com.sanshy.buysellinventory.MyUserStaticClass.saveExcelFileCustomer;
import static com.sanshy.buysellinventory.MyUserStaticClass.saveExcelFileSupplier;
import static com.sanshy.buysellinventory.MyUserStaticClass.userIdMainStatic;


public class Customer extends AppCompatActivity {

    ListView listView;

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();

    public ArrayList<citem> ciList = new ArrayList<>();

    AdView adView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        FloatingActionButton downloadCustomer = (FloatingActionButton) findViewById(R.id.download_customer);
        downloadCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DownloadProductButton();

//                Snackbar.make(view, periviousPageToken, Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        listView = findViewById(R.id.listView);

        adView1 = findViewById(R.id.adView);

        myAds();
    }
    private void DownloadProductButton() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.save_excel_file)
                .setMessage(getString(R.string.save_customer_list_in_excel_file_))
                .setPositiveButton(getString(R.string.save_text), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean check = saveExcelFileCustomer(Customer.this,getString(R.string.customer_text)+".xls",ciList);
                        if (check){
                            MyDialogBox.ShowDialog(Customer.this,getString(R.string.saved));
                        }
                        else {
                            MyDialogBox.ShowDialog(Customer.this,getString(R.string.error_));
                        }
                    }
                })
                .setNegativeButton(getString(R.string.cancel_text),null);


        builder.create().show();
    }

    private void myAds() {
        if (!isPaid()){
            adView1.loadAd(new AdRequest.Builder().build());
        }else{
            adView1.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        MyProgressBar.ShowProgress(this);

        final DatabaseReference mCustomerRef = mRootRef.child(userIdMainStatic+"/customer");
        mCustomerRef.addValueEventListener(new ValueEventListener() {
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

                    MyProgressBar.HideProgress();
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(Customer.this);
                            builder.setTitle(getString(R.string.customer_text))
                                    .setMessage(getString(R.string.name__) + Name[i] + "\n" +
                                            getString(R.string.phone__) + Phone[i] + "\n" +
                                            getString(R.string.city__) + City[i] + "\n" +
                                            getString(R.string.address_) + Address[i])
                                    .setPositiveButton(getString(R.string.delete_), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i2) {

                                            final DatabaseReference mOnHoldCustomerRef = mRootRef.child(userIdMainStatic+"/onHoldCustomer");
                                            Query query = mOnHoldCustomerRef.orderByChild("name").equalTo(Name[i]);
                                            MyProgressBar.ShowProgress(Customer.this);
                                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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
                                                        builder1.setTitle(R.string.can_not_delete)
                                                                .setMessage(R.string.payment_still_remaining_)
                                                                .setPositiveButton(getString(R.string.ok_),null)
                                                                .create()
                                                                .show();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                    MyProgressBar.HideProgress();
                                                }
                                            });

                                        }
                                    })
                                    .setNegativeButton(getString(R.string.cancel_text), null);
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
