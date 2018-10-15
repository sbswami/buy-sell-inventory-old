package com.sanshy.buysellinventory;


import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Arrays;

import static com.sanshy.buysellinventory.MyUserStaticClass.MY_PERMISSIONS_REQUEST_WRITE_STORAGE;
import static com.sanshy.buysellinventory.MyUserStaticClass.isPaid;
import static com.sanshy.buysellinventory.MyUserStaticClass.loadAds;
import static com.sanshy.buysellinventory.MyUserStaticClass.saveExcelFileStock;
import static com.sanshy.buysellinventory.MyUserStaticClass.showAds;
import static com.sanshy.buysellinventory.MyUserStaticClass.userIdMainStatic;

public class Stock extends AppCompatActivity {

    ListView lv;

    TextView remainStock;
    public ArrayList<stockitem> stockitemList = new ArrayList<>();
    AutoCompleteTextView suggestion_box4;
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();

    ArrayList<Integer> tempo = new ArrayList<>();
    @Override
    protected void onPause() {
        super.onPause();

        if (!isPaid()){
            showAds();
        }

        if (tempo.size()==0){
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);


        FloatingActionButton UploadStock = (FloatingActionButton) findViewById(R.id.upload_stock);
        UploadStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UploadStockButton();

//                Snackbar.make(view, periviousPageToken, Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        remainStock = findViewById(R.id.remainStock);
        suggestion_box4 = findViewById(R.id.suggestion_box4);

        myAds();

//        AdView adView1;
//        adView1 = findViewById(R.id.adView);
//
//        adView1.loadAd(new AdRequest.Builder().build());
    }

    private void UploadStockButton() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.save_excel_file)
                .setMessage(getString(R.string.save_the_existing_stock_))
                .setPositiveButton(getString(R.string.save_text), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Check if we're running on Android 5.0 or higher
                        if (Build.VERSION.SDK_INT >22) {
// Here, thisActivity is the current activity
                            if (ContextCompat.checkSelfPermission(Stock.this,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    != PackageManager.PERMISSION_GRANTED) {

                                // Permission is not granted
                                // Should we show an explanation?
                                if (ActivityCompat.shouldShowRequestPermissionRationale(Stock.this,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                                    Toast.makeText(Stock.this, R.string.permission_needed_excel, Toast.LENGTH_SHORT).show();

                                    // Show an explanation to the user *asynchronously* -- don't block
                                    // this thread waiting for the user's response! After the user
                                    // sees the explanation, try again to request the permission.
                                }
                                // No explanation needed; request the permission
                                ActivityCompat.requestPermissions(Stock.this,
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
        boolean check = saveExcelFileStock(Stock.this,getString(R.string.stock_text)+".xls",stockitemList);
        if (check){
            MyDialogBox.ShowDialog(Stock.this,getString(R.string.saved)+getString(R.string.saved_location));
        }
        else {
            MyDialogBox.ShowDialog(Stock.this,getString(R.string.error_));
        }
    }


    private void myAds() {
        if (!isPaid()){
            loadAds(this);
        }
    }



    @Override
    protected void onStart() {
        super.onStart();

        MyProgressBar.ShowProgress(this);
tempo.clear();
        DatabaseReference mStockRef = mRootRef.child(userIdMainStatic+"/stock");
        mStockRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                stockitemList.clear();
                for (DataSnapshot stockSnapshot : dataSnapshot.getChildren())
                {

                    stockitem stocki = stockSnapshot.getValue(stockitem.class);

                   try
                   {
                       stockitemList.add(stocki);
                   }
                   catch (NullPointerException ex)
                   {

                   }
                }
                String prodList[] = new String[stockitemList.size()+1];
                String quant[] = new String[stockitemList.size()+1];
                String buyP[] = new String[stockitemList.size()+1];
                String sellP[] = new String[stockitemList.size()+1];

                int totelQ = 0;
                double totelB = 0;
                double totelS = 0;


                for (int i = 0 ; i < stockitemList.size(); i++)
                {
                    try
                    {
                        prodList[i] = stockitemList.get(i).getProductName();
                        quant[i] = stockitemList.get(i).getQuantity();
                        buyP[i] = stockitemList.get(i).getBuyPrice();
                        sellP[i] = stockitemList.get(i).getSellPrice();
                        double buyPr = Double.parseDouble(buyP[i]);
                        double sellPr = Double.parseDouble(sellP[i]);
                        double quan = Double.parseDouble(quant[i]);
                        int qunt = (int)quan;
                        totelQ = totelQ + qunt;

                        quant[i] = qunt + "";
                        double tBuy = buyPr * quan;
                        double tSell = sellPr * quan;

                        totelB += tBuy;
                        totelS += tSell;

                        buyP[i] = tBuy+"";
                        sellP[i] = tSell+"";
                    }catch (Exception ex)
                    {

                    }
                }

                try
                {
                    prodList[stockitemList.size()] = getString(R.string.total);
                    quant[stockitemList.size()] = totelQ + "";
                    sellP[stockitemList.size()] = totelS+"";
                    buyP[stockitemList.size()] = totelB + "";
                    double profit = totelS-totelB;
                    remainStock.setText(getString(R.string.remain_profit)+ profit);
                    myListAdapter myList = new myListAdapter(Stock.this,prodList,quant,buyP,sellP);

                    lv = findViewById(R.id.listView);
                    lv.setAdapter(myList);
                }catch (Exception e)
                {

                }
                MyProgressBar.HideProgress();
tempo.add(1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                MyProgressBar.HideProgress();
tempo.add(1);
            }
        });

        final ArrayList<String> pList = new ArrayList<>();
        DatabaseReference mProductRefSearch = mRootRef.child(userIdMainStatic+"/product");
        mProductRefSearch.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    pList.add(dataSnapshot1.child("name").getValue(String.class));
                }
                String list[] = new String[pList.size()];
                for (int i = 0; i < pList.size() ; i++)
                {
                    list[i] = pList.get(i);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Stock.this,android.R.layout.simple_spinner_dropdown_item, Arrays.asList(list));

                suggestion_box4.setAdapter(adapter);
                suggestion_box4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        DatabaseReference mStockRef = mRootRef.child(userIdMainStatic+"/stock/"+suggestion_box4.getText().toString());
                        MyProgressBar.ShowProgress(Stock.this);
tempo.clear();
                        mStockRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                stockitemList.clear();
                                stockitem stocki = dataSnapshot.getValue(stockitem.class);

                                    try
                                    {
                                        stockitemList.add(stocki);
                                    }
                                    catch (NullPointerException ex)
                                    {

                                    }

                                String prodList[] = new String[stockitemList.size()+1];
                                String quant[] = new String[stockitemList.size()+1];
                                String buyP[] = new String[stockitemList.size()+1];
                                String sellP[] = new String[stockitemList.size()+1];

                                int totelQ = 0;
                                double totelB = 0;
                                double totelS = 0;


                                for (int i = 0 ; i < stockitemList.size(); i++)
                                {
                                    try
                                    {
                                        prodList[i] = stockitemList.get(i).getProductName();
                                        quant[i] = stockitemList.get(i).getQuantity();
                                        buyP[i] = stockitemList.get(i).getBuyPrice();
                                        sellP[i] = stockitemList.get(i).getSellPrice();
                                        double buyPr = Double.parseDouble(buyP[i]);
                                        double sellPr = Double.parseDouble(sellP[i]);
                                        double quan = Double.parseDouble(quant[i]);
                                        int qunt = (int)quan;
                                        totelQ = totelQ + qunt;

                                        quant[i] = qunt + "";
                                        double tBuy = buyPr * quan;
                                        double tSell = sellPr * quan;

                                        totelB += tBuy;
                                        totelS += tSell;

                                        buyP[i] = tBuy+"";
                                        sellP[i] = tSell+"";
                                    }catch (Exception ex)
                                    {

                                    }
                                }

                                try
                                {
                                    prodList[stockitemList.size()] = getString(R.string.total);
                                    quant[stockitemList.size()] = totelQ + "";
                                    sellP[stockitemList.size()] = totelS+"";
                                    buyP[stockitemList.size()] = totelB + "";
                                    double profit = totelS-totelB;
                                    remainStock.setText(getString(R.string.remain_profit)+ profit);
                                    myListAdapter myList = new myListAdapter(Stock.this,prodList,quant,buyP,sellP);

                                    lv = findViewById(R.id.listView);
                                    lv.setAdapter(myList);
                                }catch (Exception e)
                                {

                                }
                                MyProgressBar.HideProgress();
tempo.add(1);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                MyProgressBar.HideProgress();
tempo.add(1);
                            }
                        });


                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        NetworkConnectivityCheck.connectionCheck(this);

    }
}
