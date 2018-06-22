package com.sanshy.buysellinventory;

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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class payToSupplier extends AppCompatActivity {

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();

    AutoCompleteTextView suggestion_box3;
    TextView remainAmount;
    EditText amount;

    String Amount = "0";

    ArrayList<String> supplierList = new ArrayList<>();
    ArrayList<String> onHoldList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_to_supplier);

        suggestion_box3 = findViewById(R.id.suggestion_box3);
        amount = findViewById(R.id.amount);
        remainAmount = findViewById(R.id.remainHold);

        AdView adView1,adView2;
        adView1 = findViewById(R.id.adView);
        adView2 = findViewById(R.id.adView2);

        adView1.loadAd(new AdRequest.Builder().build());
        adView2.loadAd(new AdRequest.Builder().build());
    }

    @Override
    protected void onStart() {
        super.onStart();
        final DatabaseReference mCustomerRef = mRootRef.child(user.getUid()+"/onHoldSupplier");

        mCustomerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                supplierList.clear();
                onHoldList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    supplierList.add(dataSnapshot1.child("name").getValue(String.class));
                    onHoldList.add(dataSnapshot1.child("onHoldMoney").getValue(String.class));
                }
                final String Name[] = new String[supplierList.size()];
                final String OnHoldMoney[] = new String[onHoldList.size()];
                for (int i = 0; i < supplierList.size(); i++)
                {
                    Name[i] = supplierList.get(i);
                    OnHoldMoney[i] = onHoldList.get(i);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(payToSupplier.this,android.R.layout.simple_spinner_dropdown_item, Arrays.asList(Name));

                suggestion_box3.setAdapter(adapter);

                final int[] index = new int[1];
                suggestion_box3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        index[0] = Arrays.asList(Name).indexOf(suggestion_box3.getText().toString());
                        remainAmount.setText("Total : "+OnHoldMoney[index[0]]);
                        amount.setText(OnHoldMoney[index[0]]);
                        Amount = OnHoldMoney[index[0]];
                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    public void pay(View view)
    {
        String Name = suggestion_box3.getText().toString();
        final String PayMoney = amount.getText().toString();

        if (Name.isEmpty())
        {
            suggestion_box3.setError("Please Select Supplier Name");
            return;
        }
        if (PayMoney.isEmpty())
        {
            amount.setError("Please Enter");
            return;
        }
        if (Double.parseDouble(PayMoney) > Double.parseDouble(Amount))
        {
            Toast.makeText(this, "You Are Paying In Advance", Toast.LENGTH_SHORT).show();
        }
        int checkSupplier = 0;
        for (int i = 0; i < supplierList.size(); i++)
        {
            if (suggestion_box3.getText().toString().equals(supplierList.get(i)))
            {
                checkSupplier++;
                break;
            }
        }
        if (checkSupplier == 0)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Can't Save")
                    .setMessage("Please Select From Supplier List")
                    .setPositiveButton("OK",null)
                    .create()
                    .show();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are You Sure!!")
                .setMessage("After Paying You Will Not Able To Edit||")
                .setPositiveButton("Pay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        Date date = new Date();
                        String Date = dateFormat.format(date);
                        DatabaseReference mPayHistory = mRootRef.child(user.getUid()+"/payToSupplier");
                        String Id = mPayHistory.push().getKey();
                        mPayHistory.child(Id).child("payToSupplierId").setValue(Id);
                        mPayHistory.child(Id).child("name").setValue(suggestion_box3.getText().toString());
                        mPayHistory.child(Id).child("date").setValue(Date);
                        mPayHistory.child(Id).child("money").setValue(PayMoney);
                        mPayHistory.child(Id).child("dateName").setValue(Date+"_"+suggestion_box3.getText().toString());

                        DatabaseReference mOnHoldSupplier = mRootRef.child(user.getUid()+"/onHoldSupplier/");
                        double result = Double.parseDouble(Amount) - Double.parseDouble(PayMoney);

                        mOnHoldSupplier.child(suggestion_box3.getText().toString()).child("onHoldMoney").setValue(result+"");

                        Toast.makeText(payToSupplier.this, "Payment Done", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .create()
                .show();




    }
    public void historyPay(View view)
    {
        Intent intent = new Intent(this,historySupplier.class);
        startActivity(intent);
    }
    public void historyBuy(View view)
    {
        Intent intent = new Intent(this,historySupplierBuy.class);
        startActivity(intent);
    }
    public void cancel(View view)
    {
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
