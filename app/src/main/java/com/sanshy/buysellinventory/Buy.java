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

import static com.sanshy.buysellinventory.MyUserStaticClass.userIdMainStatic;

public class Buy extends AppCompatActivity {

    public static final String TOTAL_HOLD_PAID_TO_SUPPLIER = "TotalHoldPaidToSupplier";
    public static final String TOTAL_ON_HOLD_BUY = "TotalOnHoldBuy";
    public static final String TOTAL_CASH_BUY = "TotalCashBuy";
    public static final String TOTAL_BUY = "TotalBuy";
    public static final String TOTAL_SELL = "TotalSell";
    public static final String TOTAL_CASH_SELL = "TotalCashSell";
    public static final String TOTAL_ON_HOLD_SELL = "TotalOnHoldSell";
    public static final String TOTAL_HOLD_PAID_BY_CUSTOMER = "TotalHoldPaidByCustomer";
    public static final String GROSS_PROFIT = "GrossProfit";
    public static final String CASH_GROSS_PROFIT = "CashGrossProfit";
    public static final String ON_HOLD_GROSS_PROFIT = "OnHoldGrossProfit";
    public static final String TOTAL_EXPENDITURE = "TotalExpenditure";
    public static final String GROSS_PROFIT_PAID_BY_ON_HOLD_CUSTOMER = "GrossProfitPaidByOnHoldCustomer";
    public static final String NET_PROFIT = "NetProfit";
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

        cash = findViewById(R.id.cash_buy);
        onHold = findViewById(R.id.on_hold_buy);
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

        final DatabaseReference mProductRef = mRootRef.child(userIdMainStatic+"/product");
        final DatabaseReference mSupplierRef = mRootRef.child(userIdMainStatic+"/supplier");

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
                        price.setText(R.string.price_text);
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
                                quantity.setError(getString(R.string.fill_it));
                                price.setText(R.string.price);
                            }
                            else if (suggestion_box.getText().toString().isEmpty()){
                                suggestion_box.setError(getString(R.string.fill_it));
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

                MyProgressBar.HideProgress();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                MyProgressBar.HideProgress();
            }
        });

    }
    public void save(View view)
    {
        NetworkConnectivityCheck.connectionCheck(Buy.this);
        MyProgressBar.ShowProgress(this);
        String ProductName = suggestion_box.getText().toString();
        final String Quantity = quantity.getText().toString();
        final String Price = price.getText().toString();
        String SupplierName = suggestion_box2.getText().toString();
        int id = group.getCheckedRadioButtonId();

        String tempPayType;

        if (id==R.id.cash_buy){
            tempPayType = "Cash";
        }else{
            tempPayType = "On Hold";
        }

        payType = findViewById(id);

        final String PayType = tempPayType;

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
            builder.setTitle(R.string.can_not_save)
                    .setMessage(R.string.please_select_from_product_list_)
                    .setPositiveButton(getString(R.string.ok_),null)
                    .create()
                    .show();
            MyProgressBar.HideProgress();
return;
        }


        if (ProductName.isEmpty())
        {
            suggestion_box.setError(getString(R.string.select_item));
            MyProgressBar.HideProgress();
return;
        }
        if (Quantity.isEmpty())
        {
            quantity.setError(getString(R.string.select_quantity));
            MyProgressBar.HideProgress();
return;
        }
        if (SupplierName.isEmpty())
        {
            suggestion_box2.setError(getString(R.string.select_supplier_name));
            MyProgressBar.HideProgress();
return;
        }
        double New = Double.parseDouble(Quantity);
        if (New == 0)
        {
            try
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(Buy.this);
                builder.setTitle(getString(R.string.stock_text))
                        .setMessage(getString(R.string.select_quantity))
                        .setPositiveButton(getString(R.string.ok_), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .create()
                        .show();
            }catch (Exception ex)
            {

            }

            MyProgressBar.HideProgress();
return;
        }
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
            builder.setTitle(R.string.can_not_save)
                    .setMessage(R.string.please_select_from_supplier_list_)
                    .setPositiveButton(getString(R.string.ok_),null)
                    .create()
                    .show();
            MyProgressBar.HideProgress();
            return;
        }
        if (PayType.equals("On Hold"))
        {
            final DatabaseReference mOnHoldSupplierRef = mRootRef.child(userIdMainStatic+"/onHoldSupplier/"+SupplierName);
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
        if (Price.equals(getString(R.string.price))){
            String feedbackText = getResources().getString(R.string.feedback_request_text);
            Toast.makeText(Buy.this, feedbackText, Toast.LENGTH_SHORT).show();
            MyProgressBar.HideProgress();
return;
        }
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String Date = dateFormat.format(date);
        DatabaseReference mBuyRef = mRootRef.child(userIdMainStatic+"/buy");
        final DatabaseReference mStockRef = mRootRef.child(userIdMainStatic+"/stock/"+ProductName);
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
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        String buyId = mBuyRef.push().getKey();
        bitem bi = new bitem(ProductName,Quantity,Price,PayType,SupplierName,buyId,Date,Date+"_"+SupplierName,Date+"_"+ProductName,PayType+"_"+ProductName,PayType+"_"+SupplierName,Date+"_"+PayType,productBuyPrice,productSellPrice);
        mBuyRef.child(buyId).setValue(bi);
        
        final DatabaseReference mStatementInventory = mRootRef.child(userIdMainStatic+"/Statement/Inventory/"+Date);
        mStatementInventory.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double buyMoney = Double.parseDouble(Price);
                if (dataSnapshot.exists()){

                    String tBuyCloud = (String) dataSnapshot.child(TOTAL_BUY).getValue();
                    String tCashBuyCloud = (String) dataSnapshot.child(TOTAL_CASH_BUY).getValue();
                    String tOnHoldBuyCloud = (String) dataSnapshot.child(TOTAL_ON_HOLD_BUY).getValue();
                    String tHoldPaidToSuppCloud = (String) dataSnapshot.child(TOTAL_HOLD_PAID_TO_SUPPLIER).getValue();
                    
                    double buyCloud = Double.parseDouble(tBuyCloud);
                    double cashBuyCloud = Double.parseDouble(tCashBuyCloud);
                    double onHoldBuyCloud = Double.parseDouble(tOnHoldBuyCloud);
                    
                    double nowTBuy = buyMoney+buyCloud;
                    
                    if (PayType.equals("On Hold")){
                        double nowTOnHold = buyMoney + onHoldBuyCloud;
                        String saveTOnHold = String.valueOf(nowTOnHold);
                        mStatementInventory.child(TOTAL_ON_HOLD_BUY).setValue(saveTOnHold);
                    }else{
                        double nowTCash = buyMoney + cashBuyCloud;
                        String saveTCash = String.valueOf(nowTCash);
                        mStatementInventory.child(TOTAL_CASH_BUY).setValue(saveTCash);
                    }
                    
                    String saveTBuy = String.valueOf(nowTBuy);

                    mStatementInventory.child(TOTAL_BUY).setValue(saveTBuy);

                }
                else {
                    if (PayType.equals("On Hold")){
                        mStatementInventory.child(TOTAL_ON_HOLD_BUY).setValue(Price);
                        mStatementInventory.child(TOTAL_CASH_BUY).setValue("0");
                    }else {
                        mStatementInventory.child(TOTAL_CASH_BUY).setValue(Price);
                        mStatementInventory.child(TOTAL_ON_HOLD_BUY).setValue("0");
                    }
                    mStatementInventory.child(TOTAL_BUY).setValue(Price);                    
                    mStatementInventory.child(TOTAL_HOLD_PAID_TO_SUPPLIER).setValue("0");
                    mStatementInventory.child(TOTAL_SELL).setValue("0");
                    mStatementInventory.child(TOTAL_CASH_SELL).setValue("0");
                    mStatementInventory.child(TOTAL_ON_HOLD_SELL).setValue("0");
                    mStatementInventory.child(TOTAL_HOLD_PAID_BY_CUSTOMER).setValue("0");
                    mStatementInventory.child(GROSS_PROFIT).setValue("0");
                    mStatementInventory.child(CASH_GROSS_PROFIT).setValue("0");
                    mStatementInventory.child(ON_HOLD_GROSS_PROFIT).setValue("0");
                    mStatementInventory.child(GROSS_PROFIT_PAID_BY_ON_HOLD_CUSTOMER).setValue("0");
                    mStatementInventory.child(TOTAL_EXPENDITURE).setValue("0");
                    mStatementInventory.child(NET_PROFIT).setValue("0");
                }

                MyProgressBar.HideProgress();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                MyProgressBar.HideProgress();
            }
        });
        MyDialogBox.ShowDialog(Buy.this,getString(R.string.product_buy_done));
        Toast.makeText(this, getString(R.string.product_buy_done), Toast.LENGTH_SHORT).show();
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
