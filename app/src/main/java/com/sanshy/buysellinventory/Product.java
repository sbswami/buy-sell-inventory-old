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
import android.widget.AutoCompleteTextView;
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


public class Product extends AppCompatActivity {

    ListView listView;

    AutoCompleteTextView suggestion_box4;
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();

    public ArrayList<pitem> piList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        listView = findViewById(R.id.listView);

        suggestion_box4 = findViewById(R.id.suggestion_box4);

        AdView adView1;
        adView1 = findViewById(R.id.adView);

        adView1.loadAd(new AdRequest.Builder().build());


    }

    @Override
    protected void onStart() {
        super.onStart();

        MyProgressBar.ShowProgress(this);
        final DatabaseReference mProductRefMain = mRootRef.child(user.getUid()+"/product");
        mProductRefMain.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                piList.clear();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    pitem pi = dataSnapshot1.getValue(pitem.class);

                    piList.add(pi);
                }
                final String Name[] = new String[piList.size()];
                final String SellPrice[] = new String[piList.size()];
                final String BuyPrice[] = new String[piList.size()];
                final String Company[] = new String[piList.size()];
                for (int i = 0 ;i < piList.size(); i++)
                {
                    Name[i] = piList.get(i).getName();
                    BuyPrice[i] = piList.get(i).getBuyPrice();
                    SellPrice[i] = piList.get(i).getSellPrice();
                    Company[i] = piList.get(i).getCompany();
                }
                try
                {
                    mySimpleListAdapter arrayAdapter = new mySimpleListAdapter(Product.this, Name);
                    listView.setAdapter(arrayAdapter);
                    MyProgressBar.HideProgress();
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(Product.this);
                            builder.setTitle("Product")
                                    .setMessage("Name : " + Name[i] + "\n" +
                                                "Buy Price : " + BuyPrice[i] + "\n" +
                                                "Sell Price : " + SellPrice[i] + "\n" +
                                                "Company : " + Company[i])
                                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i2) {
                                            final DatabaseReference mStockRef = mRootRef.child(user.getUid()+"/stock");
                                            Query query = mStockRef.orderByChild("productName").equalTo(Name[i]);
                                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    double Money = 0;
                                                    try
                                                    {
                                                        Money = Double.parseDouble(dataSnapshot.child(Name[i]).child("quantity").getValue(String.class));

                                                    }catch (Exception ex)
                                                    {

                                                    }
                                                    if (Money == 0)
                                                    {
                                                        Query applesQuery = mProductRefMain.orderByChild("name").equalTo(Name[i]);
                                                        Query deleteOnHold = mStockRef.orderByChild("productName").equalTo(Name[i]);
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
                                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(Product.this);
                                                        builder1.setTitle("Can't Delete")
                                                                .setMessage("Product Still Remaining")
                                                                .setPositiveButton("OK",null)
                                                                .create()
                                                                .show();
                                                    }
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
                MyProgressBar.HideProgress();
            }
        });

        final ArrayList<String> pList = new ArrayList<>();
        DatabaseReference mProductSugRef = mRootRef.child(user.getUid()+"/product");
        mProductSugRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Product.this,android.R.layout.simple_spinner_dropdown_item, Arrays.asList(list));
                suggestion_box4.setAdapter(adapter);

                suggestion_box4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        listView.setFocusable(true);
                        final DatabaseReference mProductRef = mRootRef.child(user.getUid()+"/product");
                        Query query = mProductRef.orderByChild("name").equalTo(suggestion_box4.getText().toString());
                        MyProgressBar.ShowProgress(Product.this);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                piList.clear();
                                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                    pitem pi = dataSnapshot1.getValue(pitem.class);
                                    piList.add(pi);
                                }

                                final String Name[] = new String[piList.size()];
                                final String SellPrice[] = new String[piList.size()];
                                final String BuyPrice[] = new String[piList.size()];
                                final String Company[] = new String[piList.size()];
                                for (int i = 0 ;i < piList.size(); i++)
                                {
                                    Name[i] = piList.get(i).getName();
                                    BuyPrice[i] = piList.get(i).getBuyPrice();
                                    SellPrice[i] = piList.get(i).getSellPrice();
                                    Company[i] = piList.get(i).getCompany();
                                }
                                try
                                {
                                    mySimpleListAdapter arrayAdapter = new mySimpleListAdapter(Product.this, Name);
                                    listView.setAdapter(arrayAdapter);
                                    MyProgressBar.HideProgress();
                                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                                            final AlertDialog.Builder builder = new AlertDialog.Builder(Product.this);
                                            builder.setTitle("Product")
                                                    .setMessage("Name : " + Name[i] + "\n" +
                                                            "Buy Price : " + BuyPrice[i] + "\n" +
                                                            "Sell Price : " + SellPrice[i] + "\n" +
                                                            "Company : " + Company[i])
                                                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i2) {
                                                            final DatabaseReference mStockRef = mRootRef.child(user.getUid()+"/stock");
                                                            Query query = mStockRef.orderByChild("productName").equalTo(Name[i]);

                                                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                    double Money = 0;
                                                                    try
                                                                    {
                                                                        Money = Double.parseDouble(dataSnapshot.child(Name[i]).child("quantity").getValue(String.class));

                                                                    }catch (Exception ex)
                                                                    {

                                                                    }
                                                                    if (Money == 0)
                                                                    {
                                                                        Query applesQuery = mProductRef.orderByChild("name").equalTo(Name[i]);
                                                                        Query deleteOnHold = mStockRef.orderByChild("productName").equalTo(Name[i]);
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
                                                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(Product.this);
                                                                        builder1.setTitle("Can't Delete")
                                                                                .setMessage("Product Still Remaining")
                                                                                .setPositiveButton("OK",null)
                                                                                .create()
                                                                                .show();
                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(DatabaseError databaseError) {

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
                                    MyProgressBar.HideProgress();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                MyProgressBar.HideProgress();
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



    public void addP(View view)
    {
        Intent intent = new Intent(this,addProduct.class);
        intent.putExtra("pName","Nothing");
        startActivity(intent);
        this.finish();
    }
    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        NetworkConnectivityCheck.connectionCheck(this);

    }


}
