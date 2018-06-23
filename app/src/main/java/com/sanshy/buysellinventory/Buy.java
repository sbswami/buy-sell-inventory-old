package com.sanshy.buysellinventory;

import android.annotation.SuppressLint;
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

public class Buy extends AppCompatActivity {

    AutoCompleteTextView suggestion_box,suggestion_box2;

    ArrayList<pitem> productList = new ArrayList<>();
    ArrayList<sitem> supplierList = new ArrayList<>();

    RadioGroup group;
    RadioButton payType;
    RadioButton cash,onHold;
    EditText quantity;
    TextView price;
    String productSellPrice;
    String productBuyPrice;

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

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

        final DatabaseReference mProductRef = mRootRef.child(user.getUid()+"/product");
        final DatabaseReference mSupplierRef = mRootRef.child(user.getUid()+"/supplier");

        mSupplierRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                supplierList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    sitem si = dataSnapshot1.getValue(sitem.class);
                    supplierList.add(si);
                }
                String Name[] = new String[supplierList.size()];
                for (int i = 0; i < supplierList.size(); i++)
                {
                    Name[i] = supplierList.get(i).getName();
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Buy.this,android.R.layout.simple_spinner_dropdown_item, Arrays.asList(Name));

                suggestion_box2.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mProductRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    pitem pi = dataSnapshot1.getValue(pitem.class);

                    productList.add(pi);
                }
                final String Name[] = new String[productList.size()];
                final String SellPrice[] = new String[productList.size()];
                final String BuyPrice[] = new String[productList.size()];
                for (int i = 0; i <productList.size(); i++)
                {
                    Name[i] = productList.get(i).getName();
                    BuyPrice[i] = productList.get(i).getBuyPrice();
                    SellPrice[i] = productList.get(i).getSellPrice();
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Buy.this,android.R.layout.simple_spinner_dropdown_item, Arrays.asList(Name));

                suggestion_box.setAdapter(adapter);
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
                                double BPrice = Double.parseDouble(BuyPrice[index[0]]);
                                double result = Quantity * BPrice;

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
    public void save(View view)
    {
        NetworkConnectivityCheck.connectionCheck(Buy.this);

        String ProductName = suggestion_box.getText().toString();
        final String Quantity = quantity.getText().toString();
        final String Price = price.getText().toString();
        String SupplierName = suggestion_box2.getText().toString();
        int id = group.getCheckedRadioButtonId();

        payType = findViewById(id);

        String PayType = payType.getText().toString();

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
            return;
        }


        if (ProductName.isEmpty())
        {
            suggestion_box.setError("Select Item");
            return;
        }
        if (Quantity.isEmpty())
        {
            quantity.setError("Select Quantity");
            return;
        }
        if (SupplierName.isEmpty())
        {
            suggestion_box2.setError("Select Supplier Name");
            return;
        }
        double New = Double.parseDouble(Quantity);
        if (New == 0)
        {
            try
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(Buy.this);
                builder.setTitle("Stock")
                        .setMessage("Select Quantity")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .create()
                        .show();
            }catch (Exception ex)
            {

            }

            return;
        }
        if (PayType.equals("On Hold"))
        {
            int checkSupplier = 0;
            for (int i = 0; i < supplierList.size(); i++)
            {
                if (SupplierName.equals(supplierList.get(i).getName()))
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
            final DatabaseReference mOnHoldSupplierRef = mRootRef.child(user.getUid()+"/onHoldSupplier/"+SupplierName);
            mOnHoldSupplierRef.child("onHoldMoney").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try
                    {
                        double Old = Double.parseDouble(dataSnapshot.getValue(String.class));
                        double New = Double.parseDouble(Price);
                        double result = Old+New;
                        mOnHoldSupplierRef.child("onHoldMoney").setValue(result+"");
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
        }
        if (Price.equals("Price")){
            String feedbackText = getResources().getString(R.string.feedback_request_text);
            Toast.makeText(Buy.this, feedbackText, Toast.LENGTH_SHORT).show();
            return;
        }
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String Date = dateFormat.format(date);
        DatabaseReference mBuyRef = mRootRef.child(user.getUid()+"/buy");
        final DatabaseReference mStockRef = mRootRef.child(user.getUid()+"/stock/"+ProductName);
        final String[] temp = new String[1];
        mStockRef.child("quantity").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot.getValue(String.class));
                temp[0] = dataSnapshot.getValue(String.class);
                System.out.println(temp[0]);
                double result = 0;
                double Old = 0;
                try
                {

                    Old = Double.parseDouble(temp[0]);


                }catch (Exception ex)
                {
                    Old = 0;
                }
                double New = Double.parseDouble(Quantity);
                result = Old + New;
                String Result = result + "";
                System.out.println(Result);
                mStockRef.child("quantity").setValue(Result);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        String buyId = mBuyRef.push().getKey();
        bitem bi = new bitem(ProductName,Quantity,Price,PayType,SupplierName,buyId,Date,Date+"_"+SupplierName,Date+"_"+ProductName,PayType+"_"+ProductName,PayType+"_"+SupplierName,Date+"_"+PayType,productBuyPrice,productSellPrice);
        mBuyRef.child(buyId).setValue(bi);

        MyDialogBox.ShowDialog(Buy.this,"Product Buy Done");
        Toast.makeText(this, "Product Buy Done", Toast.LENGTH_SHORT).show();
        finish();

    }
    public void supplierHistory(View view)
    {
        startActivity(new Intent(this, buyList.class));
    }
    public void productHistory(View view) {
        startActivity(new Intent(this,buyHistoryByProduct.class));
    }

    public void cancel(View view)
    {
        finish();
    }

    public void undo(View view){
        startActivity(new Intent(this,UndoBuy.class));
    }
    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        NetworkConnectivityCheck.connectionCheck(Buy.this);
    }

}
