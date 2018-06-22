package com.sanshy.buysellinventory;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AlertDialog;
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

public class Stock extends AppCompatActivity {

    ListView lv;

    TextView remainStock;
    public ArrayList<stockitem> stockitemList = new ArrayList<>();
    AutoCompleteTextView suggestion_box4;
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        remainStock = findViewById(R.id.remainStock);
        AdView adView1;
        adView1 = findViewById(R.id.adView);
        suggestion_box4 = findViewById(R.id.suggestion_box4);

        adView1.loadAd(new AdRequest.Builder().build());
    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference mStockRef = mRootRef.child(user.getUid()+"/stock");
        mStockRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
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
                    prodList[stockitemList.size()] = "Total";
                    quant[stockitemList.size()] = totelQ + "";
                    sellP[stockitemList.size()] = totelS+"";
                    buyP[stockitemList.size()] = totelB + "";
                    double profit = totelS-totelB;
                    remainStock.setText("Remain Profit "+ profit);
                    myListAdapter myList = new myListAdapter(Stock.this,prodList,quant,buyP,sellP);

                    lv = findViewById(R.id.listView);
                    lv.setAdapter(myList);
                }catch (Exception e)
                {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final ArrayList<String> pList = new ArrayList<>();
        DatabaseReference mProductRefSearch = mRootRef.child(user.getUid()+"/product");
        mProductRefSearch.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
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
                        DatabaseReference mStockRef = mRootRef.child(user.getUid()+"/stock/"+suggestion_box4.getText().toString());

                        mStockRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
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
                                    prodList[stockitemList.size()] = "Total";
                                    quant[stockitemList.size()] = totelQ + "";
                                    sellP[stockitemList.size()] = totelS+"";
                                    buyP[stockitemList.size()] = totelB + "";
                                    double profit = totelS-totelB;
                                    remainStock.setText("Remain Profit "+ profit);
                                    myListAdapter myList = new myListAdapter(Stock.this,prodList,quant,buyP,sellP);

                                    lv = findViewById(R.id.listView);
                                    lv.setAdapter(myList);
                                }catch (Exception e)
                                {

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
