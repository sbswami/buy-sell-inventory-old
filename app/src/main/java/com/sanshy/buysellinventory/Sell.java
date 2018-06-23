package com.sanshy.buysellinventory;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class Sell extends AppCompatActivity {

    AutoCompleteTextView suggestion_box,suggestion_box2;

    ArrayList<pitem> productList = new ArrayList<>();
    ArrayList<citem> customerList = new ArrayList<>();

    String CustomerName;

    RadioGroup group;
    RadioButton payType;
    RadioButton cash,onHold;
    EditText quantity;
    TextView price;

    String productBuyPrice;
    String productSellPrice;

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);

        suggestion_box = findViewById(R.id.suggestion_box);
        suggestion_box2 = findViewById(R.id.suggestion_box2);
        quantity = findViewById(R.id.quantity);
        price = findViewById(R.id.price);

        cash = findViewById(R.id.cash);
        onHold = findViewById(R.id.onHold);
        group = findViewById(R.id.radioGroup);

        cash.setChecked(true);

        AdView adView1,adView2;
        adView1 = findViewById(R.id.adView);
        adView2 = findViewById(R.id.adView2);

        adView1.loadAd(new AdRequest.Builder().build());
        adView2.loadAd(new AdRequest.Builder().build());
    }

    @Override
    protected void onStart() {
        super.onStart();

        MyProgressBar.ShowProgress(this);
        final DatabaseReference mProductRef = mRootRef.child(user.getUid()+"/product");
        final DatabaseReference mCustomerRef = mRootRef.child(user.getUid()+"/customer");

        mCustomerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                customerList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    citem ci = dataSnapshot1.getValue(citem.class);
                    customerList.add(ci);
                }
                String Name[] = new String[customerList.size()];
                for (int i = 0; i < customerList.size(); i++)
                {
                    Name[i] = customerList.get(i).getName();
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Sell.this,android.R.layout.simple_spinner_dropdown_item, Arrays.asList(Name));

                suggestion_box2.setAdapter(adapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mProductRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                productList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    pitem pi = dataSnapshot1.getValue(pitem.class);

                    productList.add(pi);
                }
                final String Name[] = new String[productList.size()];
                final String BuyPrice[] = new String[productList.size()];
                final String SellPrice[] = new String[productList.size()];
                for (int i = 0; i <productList.size(); i++)
                {
                    Name[i] = productList.get(i).getName();
                    BuyPrice[i] = productList.get(i).getBuyPrice();
                    SellPrice[i] = productList.get(i).getSellPrice();
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Sell.this,android.R.layout.simple_spinner_dropdown_item, Arrays.asList(Name));

                suggestion_box.setAdapter(adapter);

                MyProgressBar.HideProgress();

                final int[] index = new int[1];
                suggestion_box.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        index[0] = Arrays.asList(Name).indexOf(suggestion_box.getText().toString());
                        productBuyPrice = BuyPrice[index[0]];
                        productSellPrice = SellPrice[index[0]];
                        quantity.setText("");

                    }
                });

                quantity.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View view, int i, KeyEvent keyEvent) {
                        try
                        {

                            String QuantityValue = quantity.getText().toString();
                            if (QuantityValue.isEmpty())
                            {
                                quantity.setError("Fill It");
                                price.setText("Price");
                            }
                            else
                            {
                                double Quantity = Double.parseDouble(quantity.getText().toString());
                                double SPrice = Double.parseDouble(SellPrice[index[0]]);
                                double result = Quantity * SPrice;

                                price.setText(result+"");
                            }
                        }catch (Exception e)
                        {

                        }
                        return false;
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void save(View view) {
        MyProgressBar.ShowProgress(this);
        final String ProductName = suggestion_box.getText().toString();
        final String Quantity = quantity.getText().toString();
        final String Price = price.getText().toString();
        final String CustomerName = suggestion_box2.getText().toString();

        int quan = 0;
        try
        {
            quan = (int)Double.parseDouble(Quantity);
        } catch (Exception ex)
        {

        }
        double grossProfit = 0;
try
{
    grossProfit = quan * Double.parseDouble(productSellPrice) - quan * Double.parseDouble(productBuyPrice);
}catch(Exception ex)
{

}

        int checkProduct = 0;
        for (int i = 0; i < productList.size(); i++)
        {
            if (ProductName.equals(productList.get(i).getName()))
            {
                checkProduct++;
                break;
            }
        }
        if (checkProduct == 0)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Can't Save")
                    .setMessage("Please Select From Product List")
                    .setPositiveButton("OK",null)
                    .create()
                    .show();
            MyProgressBar.HideProgress();
return;
        }



        int id = group.getCheckedRadioButtonId();

        payType = findViewById(id);

        final String PayType = payType.getText().toString();


        if (ProductName.isEmpty())
        {
            suggestion_box.setError("Select Item");
            MyProgressBar.HideProgress();
return;
        }
        if (Quantity.isEmpty())
        {
            quantity.setError("Enter Quantity");
            MyProgressBar.HideProgress();
return;
        }
        if (CustomerName.isEmpty() && PayType.equals("On Hold"))
        {
            suggestion_box2.setError("Select Customer Name");
            MyProgressBar.HideProgress();
return;
        }
        if (PayType.equals("On Hold"))
        {
            int checkCustomer = 0;
            for (int i = 0; i < customerList.size(); i++)
            {
                if (CustomerName.equals(customerList.get(i).getName()))
                {
                    checkCustomer++;
                    break;
                }
            }
            if (checkCustomer == 0)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Can't Save")
                        .setMessage("Please Select From Customer List")
                        .setPositiveButton("OK",null)
                        .create()
                        .show();
                MyProgressBar.HideProgress();
return;
            }
        }
        if (Price.equals("Price"))
        {
            String feedbackText = getResources().getString(R.string.feedback_request_text);
            Toast.makeText(Sell.this, feedbackText, Toast.LENGTH_SHORT).show();
            MyProgressBar.HideProgress();
return;
        }
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        final String Date = dateFormat.format(date);
        final DatabaseReference mSellRef = mRootRef.child(user.getUid()+"/sell");

        final DatabaseReference mStockRef = mRootRef.child(user.getUid()+"/stock/"+ProductName);
        final String[] temp = new String[1];
        final double finalGrossProfit = grossProfit;
        mStockRef.child("quantity").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                System.out.println(dataSnapshot.getValue(String.class));
                temp[0] = dataSnapshot.getValue(String.class);
                System.out.println(temp[0]);
                double Old = Double.parseDouble(temp[0]);
                double New = Double.parseDouble(Quantity);
                double result = Old - New;

                if (New > Old)
                {
                    try
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Sell.this);
                        builder.setTitle("Stock")
                                .setMessage("Out of Stock")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                })
                                .create()
                                .show();

                        MyProgressBar.HideProgress();
return;
                    }
                    catch (Exception ex)
                    {

                    }

                    MyProgressBar.HideProgress();
return;
                }
                if (New == 0)
                {
                    try
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Sell.this);
                        builder.setTitle("Stock")
                                .setMessage("Select Quantity")
                                .setPositiveButton("Ok", null)
                                .create()
                                .show();
                        MyProgressBar.HideProgress();
return;
                    }catch (Exception ex)
                    {

                    }
                    MyProgressBar.HideProgress();
return;
                }
                String Result = result + "";
                System.out.println(Result);
                mStockRef.child("quantity").setValue(Result);

                String sellId = mSellRef.push().getKey();
                sellitem si = new sellitem(ProductName,Quantity,Price,PayType,CustomerName,sellId,Date,PayType+"_"+ProductName,PayType+"_"+CustomerName,Date+"_"+CustomerName,Date+"_"+ProductName,Date+"_"+PayType,productBuyPrice,productSellPrice);
                mSellRef.child(sellId).setValue(si);


                MyProgressBar.HideProgress();
                MyDialogBox.ShowDialog(Sell.this,"Sell Done");

                final DatabaseReference mOnHoldCustomerRef = mRootRef.child(user.getUid()+"/onHoldCustomer/"+CustomerName);
                mOnHoldCustomerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try
                        {
                            double Old = Double.parseDouble(dataSnapshot.child("onHoldMoney").getValue(String.class));
                            double New = Double.parseDouble(Price);
                            double result = Old+New;
                            double OldProfit = Double.parseDouble(dataSnapshot.child("grossProfit").getValue(String.class));
                            double resultProfit = OldProfit + finalGrossProfit;
                            mOnHoldCustomerRef.child("grossProfit").setValue(resultProfit+"");
                            mOnHoldCustomerRef.child("onHoldMoney").setValue(result+"");
                        }
                        catch (Exception ex)
                        {
                            System.out.println(ex);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                Toast.makeText(Sell.this, "Product Sell Done", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void mySellList(View view)
    {
        startActivity(new Intent(this, sellHistory.class));
    }
    public void cancel(View view)
    {
        finish();
    }
    public void undoSell(View view){
        startActivity(new Intent(this,undoSell.class));
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        NetworkConnectivityCheck.connectionCheck(this);
    }

}
