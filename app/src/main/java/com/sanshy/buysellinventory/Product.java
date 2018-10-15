package com.sanshy.buysellinventory;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.microsoft.schemas.office.x2006.encryption.CTKeyEncryptor;

import java.util.ArrayList;
import java.util.Arrays;

import static com.sanshy.buysellinventory.MyUserStaticClass.MY_PERMISSIONS_REQUEST_WRITE_STORAGE;
import static com.sanshy.buysellinventory.MyUserStaticClass.isPaid;
import static com.sanshy.buysellinventory.MyUserStaticClass.readExcelFileProuduct;
import static com.sanshy.buysellinventory.MyUserStaticClass.saveExcelFileProduct;
import static com.sanshy.buysellinventory.MyUserStaticClass.saveExcelFileStock;
import static com.sanshy.buysellinventory.MyUserStaticClass.sharedPref;
import static com.sanshy.buysellinventory.MyUserStaticClass.showCount;
import static com.sanshy.buysellinventory.MyUserStaticClass.userIdMainStatic;
import static com.sanshy.buysellinventory.TourGuide.guide;


public class Product extends AppCompatActivity {

    ListView listView;

    AutoCompleteTextView suggestion_box4;
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();

    public ArrayList<pitem> piList = new ArrayList<>();

    private static final int FILE_SELECT_CODE = 99;

    AdView adView1;

    FloatingActionButton addProdu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

//        FloatingActionButton uploadProduct = (FloatingActionButton) findViewById(R.id.upload_product);
//        uploadProduct.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                UploadProductButton();
//
////                Snackbar.make(view, periviousPageToken, Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show();
//            }
//        });

        FloatingActionButton downloadProduct = (FloatingActionButton) findViewById(R.id.download_product);
        downloadProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DownloadProductButton();

//                Snackbar.make(view, periviousPageToken, Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        addProdu = findViewById(R.id.floatingActionButton);

        listView = findViewById(R.id.listView);

        suggestion_box4 = findViewById(R.id.suggestion_box4);

        adView1 = findViewById(R.id.adView);

        myAds();


    }

    private void DownloadProductButton() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.save_excel_file)
                .setMessage(getString(R.string.save_the_existing_product_))
                .setPositiveButton(getString(R.string.save_text), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        // Check if we're running on Android 5.0 or higher
                        if (Build.VERSION.SDK_INT >22) {
// Here, thisActivity is the current activity
                            if (ContextCompat.checkSelfPermission(Product.this,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    != PackageManager.PERMISSION_GRANTED) {

                                // Permission is not granted
                                // Should we show an explanation?
                                if (ActivityCompat.shouldShowRequestPermissionRationale(Product.this,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                                    Toast.makeText(Product.this, R.string.permission_needed_excel, Toast.LENGTH_SHORT).show();

                                    // Show an explanation to the user *asynchronously* -- don't block
                                    // this thread waiting for the user's response! After the user
                                    // sees the explanation, try again to request the permission.
                                }
                                // No explanation needed; request the permission
                                ActivityCompat.requestPermissions(Product.this,
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
        boolean check = saveExcelFileProduct(Product.this,getString(R.string.product_text)+".xls",piList);
        if (check){
            MyDialogBox.ShowDialog(Product.this,getString(R.string.saved)+getString(R.string.saved_location));
        }
        else {
            MyDialogBox.ShowDialog(Product.this,getString(R.string.error_));
        }
    }

    private void UploadProductButton() {
        Intent myIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        myIntent.addCategory(Intent.CATEGORY_OPENABLE);
        myIntent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        startActivityForResult(myIntent,FILE_SELECT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode==FILE_SELECT_CODE)&&(resultCode==RESULT_OK)){
            MyProgressBar.ShowProgress(this);
            Uri uri = data.getData();
            String filePath = uri.getPath();
            String filePrimaryPath = filePath.substring(filePath.indexOf(":")+1);
            filePrimaryPath = "/storage/emulated/0/"+filePrimaryPath;


            ArrayList<pitem> Pi = readExcelFileProuduct(this,filePrimaryPath,false);
            if (Pi==null){
                MyDialogBox.ShowDialog(this,getString(R.string.unable_to_find_correct_file));
            }else{
                ArrayList<pitem> savingItems = new ArrayList<>();

                for (pitem Pit : Pi){

                    for (int l = 0; l < AllItems.size(); l++){
                        if (Pit.getName().equals(AllItems.get(l).getName())){
                            break;
                        }
                        if (l==AllItems.size()-1){
                            if (!(Pit.getName().equals(AllItems.get(l).getName()))){
                                savingItems.add(Pit);
                            }
                        }
                    }
                }

                for (pitem prom : savingItems){

                    String Name = prom.getName();
                    String SellPrice = prom.getSellPrice();
                    String BuyPrice = prom.getBuyPrice();
                    String Company = prom.getCompany();

                    DatabaseReference mProductRef = mRootRef.child(userIdMainStatic+"/product/"+Name+"_"+userIdMainStatic);
                    pitem pi = new pitem(Name,SellPrice,BuyPrice,Company,Company+"_"+SellPrice,Company+"_"+BuyPrice);
                    mProductRef.setValue(pi);

                    DatabaseReference mStockRef = mRootRef.child(userIdMainStatic+"/stock/"+Name);
                    stockitem stocki = new stockitem(Name,"0",BuyPrice,SellPrice);
                    mStockRef.setValue(stocki);
                }
                MyProgressBar.HideProgress();
                MyDialogBox.ShowDialog(this,savingItems.size()+" "+getString(R.string.saved));
            }
            MyProgressBar.HideProgress();
        }
    }

    private void myAds() {
        if (!isPaid()){
            adView1.loadAd(new AdRequest.Builder().build());
        }else{
            adView1.setVisibility(View.GONE);
        }
    }

    ArrayList<pitem> AllItems = new ArrayList<>();

    @Override
    protected void onStart() {
        super.onStart();

        MyProgressBar.ShowProgress(this);
        final DatabaseReference mProductRefMain = mRootRef.child(userIdMainStatic+"/product");
        mProductRefMain.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                piList.clear();
                AllItems.clear();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    pitem pi = dataSnapshot1.getValue(pitem.class);
                    AllItems.add(pi);
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
                            builder.setTitle(getString(R.string.product_text))
                                    .setMessage(getString(R.string.name__) + Name[i] + "\n" +
                                                getString(R.string.buy_price__) + BuyPrice[i] + "\n" +
                                                getString(R.string.sell_price__) + SellPrice[i] + "\n" +
                                                getString(R.string.company__) + Company[i])
                                    .setPositiveButton(getString(R.string.delete_), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i2) {
                                            final DatabaseReference mStockRef = mRootRef.child(userIdMainStatic+"/stock");
                                            MyProgressBar.ShowProgress(Product.this);
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
                                                        builder1.setTitle(getString(R.string.can_not_save))
                                                                .setMessage(getString(R.string.product_still_remaining_))
                                                                .setPositiveButton(getString(R.string.ok_),null)
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
                MyProgressBar.HideProgress();
            }
        });

        final ArrayList<String> pList = new ArrayList<>();
        DatabaseReference mProductSugRef = mRootRef.child(userIdMainStatic+"/product");
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
                        final DatabaseReference mProductRef = mRootRef.child(userIdMainStatic+"/product");
                        Query query = mProductRef.orderByChild("name").equalTo(suggestion_box4.getText().toString());
                        MyProgressBar.ShowProgress(Product.this);
                        query.addValueEventListener(new ValueEventListener() {
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
                                            builder.setTitle(getString(R.string.product_text))
                                                    .setMessage(getString(R.string.name__) + Name[i] + "\n" +
                                                            getString(R.string.buy_price__) + BuyPrice[i] + "\n" +
                                                            getString(R.string.sell_price__) + SellPrice[i] + "\n" +
                                                            getString(R.string.company__) + Company[i])
                                                    .setPositiveButton(getString(R.string.delete_), new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i2) {
                                                            final DatabaseReference mStockRef = mRootRef.child(userIdMainStatic+"/stock");
                                                            Query query = mStockRef.orderByChild("productName").equalTo(Name[i]);
                                                            MyProgressBar.ShowProgress(Product.this);
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

                                                                        Query applesQuery = mProductRef.orderByChild("name").equalTo(Name[i]);
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
                                                                        builder1.setTitle(getString(R.string.can_not_save))
                                                                                .setMessage(getString(R.string.product_still_remaining_))
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

        Thread myThread = new Thread(runnable);
        myThread.start();

    }

    void productTour(){

        sharedPref = this.getSharedPreferences(getString(R.string.tour_show_time), Context.MODE_PRIVATE);
        int productCount = 1;
        productCount = sharedPref.getInt(getString(R.string.prod_count), productCount);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.prod_count), 1+productCount);
        editor.apply();

        switch (productCount){
            case 1 : guide(Product.this,"ProductAddProduct",getString(R.string.add_product_text),addProdu,showCount);
                break;
            case 2 : guide(Product.this,"searchProduct",getString(R.string.product_search_tour_guide),suggestion_box4,showCount);
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
                    productTour();
                }
            });
        }
    };


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
