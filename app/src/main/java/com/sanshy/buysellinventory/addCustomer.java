package com.sanshy.buysellinventory;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

import static com.sanshy.buysellinventory.MyUserStaticClass.isPaid;
import static com.sanshy.buysellinventory.MyUserStaticClass.loadAds;
import static com.sanshy.buysellinventory.MyUserStaticClass.showAds;
import static com.sanshy.buysellinventory.MyUserStaticClass.userIdMainStatic;

public class addCustomer extends AppCompatActivity {

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();

    EditText name,phone;

    AutoCompleteTextView city,address;


    final ArrayList<String> hintList1 = new ArrayList<>();
    final ArrayList<String> hintList2 = new ArrayList<>();


    AdView adView1,adView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        city = findViewById(R.id.city);
        address = findViewById(R.id.address);

        adView1 = findViewById(R.id.adView);
        adView2 = findViewById(R.id.adView2);

        myAds();
    }

    public void myAds(){
        if (!isPaid()){
            adView1.loadAd(new AdRequest.Builder().build());
            adView2.loadAd(new AdRequest.Builder().build());
            loadAds(this);

        }else{
            adView1.setVisibility(View.GONE);
            adView2.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!isPaid()){
            showAds();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference mHint2 = mRootRef.child(userIdMainStatic+"/city");
        mHint2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hintList1.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    hintList1.add(dataSnapshot1.child("hint").getValue(String.class));
                }
                try
                {
                    String list[] = new String[hintList1.size()];
                    for (int i = 0; i < hintList1.size() ; i++)
                    {
                        list[i] = hintList1.get(i);
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(addCustomer.this,android.R.layout.simple_spinner_dropdown_item, Arrays.asList(list));
                    city.setAdapter(arrayAdapter);
                }catch (Exception e)
                {

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference mHint3 = mRootRef.child(userIdMainStatic+"/address");
        mHint3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hintList2.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    hintList2.add(dataSnapshot1.child("hint").getValue(String.class));
                }
                try
                {
                    String list[] = new String[hintList2.size()];
                    for (int i = 0; i < hintList2.size() ; i++)
                    {
                        list[i] = hintList2.get(i);
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(addCustomer.this,android.R.layout.simple_spinner_dropdown_item, Arrays.asList(list));
                    address.setAdapter(arrayAdapter);
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
        NetworkConnectivityCheck.connectionCheck(addCustomer.this);
        final String Name = name.getText().toString();
        final String Phone = phone.getText().toString();
        final String City = city.getText().toString();
        String Address = address.getText().toString();

        int check1 = 0;
        for (int i = 0; i < hintList1.size(); i++)
        {
            if (hintList1.get(i).equals(City))
            {
                check1++;
            }
        }
        if (check1 == 0)
        {
            DatabaseReference mHint2 = mRootRef.child(userIdMainStatic+"/city");

            String Id = mHint2.push().getKey();

            mHint2.child(Id).child("id").setValue(Id);
            mHint2.child(Id).child("hint").setValue(City);
        }
        int check2 = 0;
        for (int i = 0; i < hintList2.size(); i++)
        {
            if (hintList2.get(i).equals(Address))
            {
                check2++;
            }
        }
        if (check2 == 0)
        {
            DatabaseReference mHint2 = mRootRef.child(userIdMainStatic+"/address");

            String Id = mHint2.push().getKey();

            mHint2.child(Id).child("id").setValue(Id);
            mHint2.child(Id).child("hint").setValue(Address);
        }

        if (Name.isEmpty())
        {
            name.setError(getString(R.string.required_field));
            return;
        }
        if (Phone.isEmpty())
        {
            phone.setError(getString(R.string.required_field));
            return;
        }
        if (City.isEmpty())
        {
            city.setError(getString(R.string.required_field));
            return;
        }
        if (Address.isEmpty())
        {
            Address = getString(R.string.not_defined);
        }

        DatabaseReference allCus = mRootRef.child(userIdMainStatic+"/customer");
        final String finalAddress = Address;
        MyProgressBar.ShowProgress(addCustomer.this);
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
                    DatabaseReference mCustomerRef = mRootRef.child(userIdMainStatic+"/customer/"+Name+"_"+Phone+"_"+userIdMainStatic);

                    citem ci = new citem(Name,Phone,City, finalAddress,Name+"_"+Phone,Name+"_"+City);
                    mCustomerRef.setValue(ci);
                    DatabaseReference mOnHoldCustomerRef = mRootRef.child(userIdMainStatic+"/onHoldCustomer/"+Name);
                    mOnHoldCustomerRef.child("name").setValue(Name);
                    mOnHoldCustomerRef.child("onHoldMoney").setValue("0");
                    mOnHoldCustomerRef.child("grossProfit").setValue("0");
                    Toast.makeText(addCustomer.this, R.string.customer_saved, Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(addCustomer.this, Customer.class));
                    addCustomer.this.finish();
                }
                else
                {
                    try
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(addCustomer.this);
                        builder.setTitle(R.string.can_not_save)
                                .setMessage(getString(R.string.customer_already_exist_))
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

        NetworkConnectivityCheck.connectionCheck(addCustomer.this);

    }
}
