package com.sanshy.buysellinventory;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ListView;
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

import java.util.ArrayList;

import static com.sanshy.buysellinventory.MyUserStaticClass.MY_PERMISSIONS_REQUEST_WRITE_STORAGE;
import static com.sanshy.buysellinventory.MyUserStaticClass.isPaid;
import static com.sanshy.buysellinventory.MyUserStaticClass.saveExcelFileSupplier;
import static com.sanshy.buysellinventory.MyUserStaticClass.sharedPref;
import static com.sanshy.buysellinventory.MyUserStaticClass.showCount;
import static com.sanshy.buysellinventory.MyUserStaticClass.userIdMainStatic;
import static com.sanshy.buysellinventory.TourGuide.guide;


public class Supplier extends AppCompatActivity {

    ListView listView;

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();

    public ArrayList<sitem> siList = new ArrayList<>();
    AdView adView1;

    FloatingActionButton addSuppli;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier);

        FloatingActionButton downloadSupplier = (FloatingActionButton) findViewById(R.id.download_supplier);
        downloadSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DownloadProductButton();

//                Snackbar.make(view, periviousPageToken, Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        addSuppli = findViewById(R.id.floatingActionButton2);

        listView = findViewById(R.id.listView);

        adView1 = findViewById(R.id.adView);

        myAds();
    }

    private void DownloadProductButton() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.save_excel_file)
                .setMessage(getString(R.string.save_supplier_list_in_excel_file_))
                .setPositiveButton(getString(R.string.save_text), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        // Check if we're running on Android 5.0 or higher
                        if (Build.VERSION.SDK_INT >22) {
// Here, thisActivity is the current activity
                            if (ContextCompat.checkSelfPermission(Supplier.this,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    != PackageManager.PERMISSION_GRANTED) {

                                // Permission is not granted
                                // Should we show an explanation?
                                if (ActivityCompat.shouldShowRequestPermissionRationale(Supplier.this,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                                    Toast.makeText(Supplier.this, R.string.permission_needed_excel, Toast.LENGTH_SHORT).show();

                                    // Show an explanation to the user *asynchronously* -- don't block
                                    // this thread waiting for the user's response! After the user
                                    // sees the explanation, try again to request the permission.
                                }
                                // No explanation needed; request the permission
                                ActivityCompat.requestPermissions(Supplier.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        MY_PERMISSIONS_REQUEST_WRITE_STORAGE);

                                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                                // app-defined int constant. The callback method gets the
                                // result of the request.
                            } else {
                                saveExcelFinal();
                                // Permission has already been granted
                            }

                        } else {

                            saveExcelFinal();

                            // Implement this feature without material design
                        }
                    }
                })
                .setNegativeButton(getString(R.string.cancel_text),null);


        builder.create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    saveExcelFinal();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                    Toast.makeText(this, getString(R.string.permission_needed_excel), Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    public void saveExcelFinal(){
        boolean check = saveExcelFileSupplier(Supplier.this,getString(R.string.supplier_text)+".xls",siList);
        if (check){
            MyDialogBox.ShowDialog(Supplier.this,getString(R.string.saved)+getString(R.string.saved_location));
        }
        else {
            MyDialogBox.ShowDialog(Supplier.this,getString(R.string.error_));
        }
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

        final DatabaseReference mSupplierRef = mRootRef.child(userIdMainStatic+"/supplier");
        mSupplierRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

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

                    MyProgressBar.HideProgress();

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(Supplier.this);
                            builder.setTitle(getString(R.string.supplier_text))
                                    .setMessage(getString(R.string.name__) + Name[i] + "\n" +
                                            getString(R.string.company__) + Company[i] + "\n" +
                                            getString(R.string.phone__) + Phone[i] + "\n" +
                                            getString(R.string.city__) + City[i] + "\n" +
                                            getString(R.string.address_) + Address[i])
                                    .setPositiveButton(getString(R.string.delete_), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i2) {
                                            final DatabaseReference mOnHoldSupplierRef = mRootRef.child(userIdMainStatic+"/onHoldSupplier");
                                            Query query = mOnHoldSupplierRef.orderByChild("name").equalTo(Name[i]);
                                            MyProgressBar.ShowProgress(Supplier.this);
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
                                                        Query applesQuery = mSupplierRef.orderByChild("name").equalTo(Name[i]);
                                                        Query deleteOnHold = mOnHoldSupplierRef.orderByChild("name").equalTo(Name[i]);
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
                                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(Supplier.this);
                                                        builder1.setTitle(getString(R.string.can_not_save))
                                                                .setMessage(getString(R.string.payment_still_remaining_))
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
                                    .setNegativeButton(getString(R.string.cancel_text),null);
                            builder.create().show();


                        }
                    });
                }
                catch(Exception ex)
                {
                    System.out.println("\n"+ex+"\n");
                    MyProgressBar.HideProgress();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                MyProgressBar.HideProgress();
            }
        });

        Thread myThread = new Thread(runnable);
        myThread.start();

    }

    void supplierTour(){

        sharedPref = this.getSharedPreferences(getString(R.string.tour_show_time), Context.MODE_PRIVATE);
        int supplierCount = 1;
        supplierCount = sharedPref.getInt(getString(R.string.supplie_count), supplierCount);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.supplie_count), 1+supplierCount);
        editor.apply();

        switch (supplierCount){
            case 1 : guide(Supplier.this,"SupplierAddSupplier",getString(R.string.add_supplier_text),addSuppli,showCount);
                break;
        }
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    supplierTour();
                }
            });
        }
    };

    public void addS(View view){
        Intent intent = new Intent(this,addSupplier.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        NetworkConnectivityCheck.connectionCheck(this);
    }


}
