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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
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
import java.util.Arrays;

import static com.sanshy.buysellinventory.MyUserStaticClass.isPaid;
import static com.sanshy.buysellinventory.MyUserStaticClass.userIdMainStatic;

public class addProduct extends AppCompatActivity {

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();

    EditText name,buyPrice,sellPrice,buyMargin;

    AutoCompleteTextView company;

    String oldName;
    DatabaseReference mProductReference,mBuyReference;

    final ArrayList<String> hintList = new ArrayList<>();

    AdView adView1,adView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        name = findViewById(R.id.name);
        buyPrice = findViewById(R.id.buyPrice);
        sellPrice = findViewById(R.id.sellPrice);
        company = findViewById(R.id.company);
        buyMargin = findViewById(R.id.buyMargin);

        adView1 = findViewById(R.id.adView);
        adView2 = findViewById(R.id.adView2);

        myAds();

    }

    private void myAds() {
        if (!isPaid()){
            adView1.loadAd(new AdRequest.Builder().build());
            adView2.loadAd(new AdRequest.Builder().build());
        }else{
            adView1.setVisibility(View.GONE);
            adView2.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference mHint = mRootRef.child(userIdMainStatic+"/company");
        mHint.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hintList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    hintList.add(dataSnapshot1.child("hint").getValue(String.class));
                }
                try
                {
                    String list[] = new String[hintList.size()];
                    for (int i = 0; i < hintList.size() ; i++)
                    {
                        list[i] = hintList.get(i);
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(addProduct.this,android.R.layout.simple_spinner_dropdown_item, Arrays.asList(list));
                    company.setAdapter(arrayAdapter);
                }catch (Exception e)
                {

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void cancel(View view)
    {
        this.finish();
    }

    public void save(View view)
    {
        NetworkConnectivityCheck.connectionCheck(addProduct.this);
        final String Name = name.getText().toString();
        final String BuyP = buyPrice.getText().toString();
        final String SellPrice = sellPrice.getText().toString();
        final String Company = company.getText().toString();
        final String Margin = buyMargin.getText().toString();

        int check = 0;
        for (int i = 0; i < hintList.size(); i++)
        {
            if (hintList.get(i).equals(Company))
            {
                check++;
            }
        }
        if (check == 0)
        {
            DatabaseReference mHint = mRootRef.child(userIdMainStatic+"/company");
            hintList.add(Company);
            String Id = mHint.push().getKey();

            mHint.child(Id).child("id").setValue(Id);
            mHint.child(Id).child("hint").setValue(Company);
        }


        if (Name.isEmpty())
        {
            name.setError(getString(R.string.required_field));
            return;
        }
        if (BuyP.isEmpty()&&Margin.isEmpty())
        {
            buyMargin.setError(getString(R.string.required_field));
            return;
        }
        if (!(BuyP.isEmpty()||Margin.isEmpty()))
        {
            MyDialogBox.ShowDialog(addProduct.this,getString(R.string.enter_any_one)+"\n" +
                    getString(R.string.price_or_margin));
            return;
        }
        if (SellPrice.isEmpty())
        {
            sellPrice.setError(getString(R.string.required_field));
            return;
        }

        String tempBuyPriceHolder = BuyP;
        if (BuyP.isEmpty()){
            double marginPercent = Double.parseDouble(Margin);
            double sellPri = Double.parseDouble(SellPrice);

            double marginMain = (marginPercent*sellPri)/100;
            double buyPri = sellPri - marginMain;
            tempBuyPriceHolder = String.valueOf(buyPri);
        }

        final String BuyPrice = tempBuyPriceHolder;

        DatabaseReference allCus = mRootRef.child(userIdMainStatic+"/product");
        MyProgressBar.ShowProgress(addProduct.this);
        allCus.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int check = 0;

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    if (dataSnapshot1.child("name").getValue(String.class).equals(Name))
                    {
                        check++;
                    }

                }
                MyProgressBar.HideProgress();
                if (check == 0)
                {
                    DatabaseReference mProductRef = mRootRef.child(userIdMainStatic+"/product/"+Name+"_"+userIdMainStatic);
                    pitem pi = new pitem(Name,SellPrice,BuyPrice,Company,Company+"_"+SellPrice,Company+"_"+BuyPrice);
                    mProductRef.setValue(pi);

                    DatabaseReference mStockRef = mRootRef.child(userIdMainStatic+"/stock/"+Name);
                    stockitem stocki = new stockitem(Name,"0",BuyPrice,SellPrice);
                    mStockRef.setValue(stocki);
                    Toast.makeText(addProduct.this, R.string.product_saved, Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(addProduct.this, Product.class));
                    addProduct.this.finish();
                }
                else
                {
                    try
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(addProduct.this);
                        builder.setTitle(getString(R.string.can_not_save))
                                .setMessage(R.string.product_already_exist_)
                                .setPositiveButton(getString(R.string.ok_),null)
                                .create()
                                .show();
                    }
                    catch (Exception ex)
                    {

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        NetworkConnectivityCheck.connectionCheck(addProduct.this);

    }

}
