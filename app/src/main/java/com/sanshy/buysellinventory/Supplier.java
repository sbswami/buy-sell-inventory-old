package com.sanshy.buysellinventory;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
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


public class Supplier extends AppCompatActivity {

    ListView listView;

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();

    public ArrayList<sitem> siList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier);

        listView = findViewById(R.id.listView);

        AdView adView1;
        adView1 = findViewById(R.id.adView);

        adView1.loadAd(new AdRequest.Builder().build());
    }

    @Override
    protected void onStart() {
        super.onStart();



        final DatabaseReference mSupplierRef = mRootRef.child(user.getUid()+"/supplier");
        mSupplierRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                siList.clear();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    sitem si = dataSnapshot1.getValue(sitem.class);

                    siList.add(si);
                }
                final String Name[] = new String[siList.size()];
                final String Company[] = new String[siList.size()];
                final String Phone[] = new String[siList.size()];
                final String City[] = new String[siList.size()];
                final String Address[] = new String[siList.size()];
                for (int i = 0; i < siList.size(); i++)
                {
                    Name[i] = siList.get(i).getName();
                    Company[i] = siList.get(i).getCompany();
                    Phone[i] = siList.get(i).getPhone();
                    City[i] = siList.get(i).getCity();
                    Address[i] = siList.get(i).getAddress();
                }
                try
                {
                    mySimpleListAdapter arrayAdapter = new mySimpleListAdapter(Supplier.this, Name);
                    listView.setAdapter(arrayAdapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(Supplier.this);
                            builder.setTitle("Supplier")
                                    .setMessage("Name : " + Name[i] + "\n" +
                                            "Company : " + Company[i] + "\n" +
                                            "Phone : " + Phone[i] + "\n" +
                                            "City : " + City[i] + "\n" +
                                            "Address " + Address[i])
                                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i2) {
                                            final DatabaseReference mOnHoldSupplierRef = mRootRef.child(user.getUid()+"/onHoldSupplier");
                                            Query query = mOnHoldSupplierRef.orderByChild("name").equalTo(Name[i]);
                                            final ArrayList<String> tempo = new ArrayList<>();
                                            query.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
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
                                                            Query applesQuery = mSupplierRef.orderByChild("name").equalTo(Name[i]);
                                                            Query deleteOnHold = mOnHoldSupplierRef.orderByChild("name").equalTo(Name[i]);
                                                            final ArrayList<String> temp1 = new ArrayList<>();
                                                            final ArrayList<String> temp2 = new ArrayList<>();
                                                            applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                    if (temp1.size() == 0)
                                                                    {
                                                                        for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                                                            appleSnapshot.getRef().removeValue();
                                                                        }
                                                                    }
                                                                    temp1.add("Kuch bhi");

                                                                }

                                                                @Override
                                                                public void onCancelled(DatabaseError databaseError) {
                                                                    Log.e("Error", "onCancelled", databaseError.toException());
                                                                }
                                                            });
                                                            deleteOnHold.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                    if (temp2.size()==0)
                                                                    {
                                                                        for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                                                            appleSnapshot.getRef().removeValue();
                                                                        }
                                                                    }
                                                                    temp2.add("Kuchh Bhi");

                                                                }

                                                                @Override
                                                                public void onCancelled(DatabaseError databaseError) {
                                                                    Log.e("Error", "onCancelled", databaseError.toException());
                                                                }
                                                            });
                                                        }
                                                        else
                                                        {
                                                            AlertDialog.Builder builder1 = new AlertDialog.Builder(Supplier.this);
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
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });

                                        }
                                    })
                                    .setNegativeButton("Cancel",null);
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
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



    public void addS(View view)
    {
        Intent intent = new Intent(this,addSupplier.class);
        startActivity(intent);
        this.finish();
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
