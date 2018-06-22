package com.sanshy.buysellinventory;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
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

public class addSupplier extends AppCompatActivity {

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();

    EditText name,phone;

    AutoCompleteTextView company,city,address;

    String oldName;
    DatabaseReference mSupplierReference;

    final ArrayList<String> hintList = new ArrayList<>();
    final ArrayList<String> hintList1 = new ArrayList<>();
    final ArrayList<String> hintList2 = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_supplier);
        name = findViewById(R.id.name);
        company = findViewById(R.id.company);
        phone = findViewById(R.id.phone);
        city = findViewById(R.id.city);
        address = findViewById(R.id.address);
        AdView adView1,adView2;
        adView1 = findViewById(R.id.adView);
        adView2 = findViewById(R.id.adView2);

        adView1.loadAd(new AdRequest.Builder().build());
        adView2.loadAd(new AdRequest.Builder().build());

    }

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference mHint = mRootRef.child(user.getUid()+"/company");
        mHint.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
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
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(addSupplier.this,android.R.layout.simple_spinner_dropdown_item, Arrays.asList(list));
                    company.setAdapter(arrayAdapter);
                }catch (Exception e)
                {

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference mHint2 = mRootRef.child(user.getUid()+"/city");
        mHint2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
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
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(addSupplier.this,android.R.layout.simple_spinner_dropdown_item, Arrays.asList(list));
                    city.setAdapter(arrayAdapter);
                }catch (Exception e)
                {

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        DatabaseReference mHint3 = mRootRef.child(user.getUid()+"/address");
        mHint3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
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
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(addSupplier.this,android.R.layout.simple_spinner_dropdown_item, Arrays.asList(list));
                    address.setAdapter(arrayAdapter);
                }catch (Exception e)
                {

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void cancel(View view)
    {
        this.finish();
    }

    public void save(View view)
    {
        connectionCheck();
        final String Name = name.getText().toString();
        final String Company = company.getText().toString();
        final String Phone = phone.getText().toString();
        final String City = city.getText().toString();
        String Address = address.getText().toString();

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
            DatabaseReference mHint = mRootRef.child(user.getUid()+"/company");

            String Id = mHint.push().getKey();

            mHint.child(Id).child("id").setValue(Id);
            mHint.child(Id).child("hint").setValue(Company);
        }
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
            DatabaseReference mHint2 = mRootRef.child(user.getUid()+"/city");

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
            DatabaseReference mHint2 = mRootRef.child(user.getUid()+"/address");

            String Id = mHint2.push().getKey();

            mHint2.child(Id).child("id").setValue(Id);
            mHint2.child(Id).child("hint").setValue(Address);
        }

        if (Name.isEmpty())
        {
            name.setError("Required Field");
            return;
        }
        if (Company.isEmpty())
        {
            company.setError("Required Field");
            return;
        }
        if (Phone.isEmpty())
        {
            phone.setError("Required Field");
            return;
        }
        if (City.isEmpty())
        {
            city.setError("Required Field");
            return;
        }
        if (Address.isEmpty())
        {
            Address = "Not Defined";
        }


        DatabaseReference allCus = mRootRef.child(user.getUid()+"/supplier");

        final String finalAddress = Address;
        final String finalAddress1 = Address;

        final ArrayList<String> temp = new ArrayList<>();
        allCus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (temp.size() == 0)
                {
                    int check = 0;

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                    {
                        if (dataSnapshot1.child("name").getValue(String.class).equals(Name))
                        {
                            check++;
                        }

                    }
                    if (check == 0)
                    {
                        DatabaseReference mSupplierRef = mRootRef.child(user.getUid()+"/supplier/"+Name+"_"+user.getUid());

                        sitem si = new sitem(Name,Company,Phone,City, finalAddress1,Name+"_"+City,Company+"_"+City);
                        mSupplierRef.setValue(si);
                        DatabaseReference mOnHoldSupplierRef = mRootRef.child(user.getUid()+"/onHoldSupplier/"+Name);
                        mOnHoldSupplierRef.child("id").setValue(Name+"_"+Phone);
                        mOnHoldSupplierRef.child("name").setValue(Name);
                        mOnHoldSupplierRef.child("onHoldMoney").setValue("0");
                        Toast.makeText(addSupplier.this, "Supplier Saved", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(addSupplier.this, Supplier.class));
                        addSupplier.this.finish();
                    }
                    else
                    {
                        try
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(addSupplier.this);
                            builder.setTitle("Can't Save")
                                    .setMessage("Supplier Already Exist")
                                    .setPositiveButton("OK",null)
                                    .create()
                                    .show();
                        }
                        catch (Exception ex)
                        {

                        }
                    }
                }
                temp.add("Kuchh Bhi");
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
