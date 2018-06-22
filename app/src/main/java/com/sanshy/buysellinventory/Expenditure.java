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

public class Expenditure extends AppCompatActivity {

    EditText money;
    AutoCompleteTextView remark;

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();


    final ArrayList<String> hintList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenditure);

        money = findViewById(R.id.money);
        remark = findViewById(R.id.remark);
        AdView adView1,adView2;
        adView1 = findViewById(R.id.adView);
        adView2 = findViewById(R.id.adView2);

        adView1.loadAd(new AdRequest.Builder().build());
        adView2.loadAd(new AdRequest.Builder().build());
    }

    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference mHint = mRootRef.child(user.getUid()+"/remark");
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
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Expenditure.this,android.R.layout.simple_spinner_dropdown_item,Arrays.asList(list));
                    remark.setAdapter(arrayAdapter);
                }catch (Exception e)
                {

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void undoB(View view){
        startActivity(new Intent(this,UndoExp.class));
    }

    public void save(View view)
    {
        String Money = money.getText().toString();
        String Remark = remark.getText().toString();

        if (Money.isEmpty())
        {
            money.setError("Please Fill It");
            return;
        }
        if (Remark.isEmpty())
        {
            remark.setError("Please Fill It");
            return;
        }

        int check = 0;
        for (int i = 0; i < hintList.size(); i++)
        {
            if (hintList.get(i).equals(Remark))
            {
                check++;
            }
        }
        if (check == 0)
        {
            DatabaseReference mHint = mRootRef.child(user.getUid()+"/remark");

            String Id = mHint.push().getKey();

            mHint.child(Id).child("id").setValue(Id);
            mHint.child(Id).child("hint").setValue(Remark);
        }
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        final String Date = dateFormat.format(date);
        DatabaseReference mExRef = mRootRef.child(user.getUid()+"/Expenditure");
        String Id = mExRef.push().getKey();
        mExRef.child(Id).child("id").setValue(Id);
        mExRef.child(Id).child("money").setValue(Money);
        mExRef.child(Id).child("remark").setValue(Remark);
        mExRef.child(Id).child("date").setValue(Date);
        mExRef.child(Id).child("date_remark").setValue(Date+"_"+Remark);


        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        finish();
    }
    public void list(View view)
    {
        startActivity(new Intent(this,expenditureList.class));
    }
    public void cancel(View view)
    {
        finish();
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
