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

import static com.sanshy.buysellinventory.Buy.CASH_GROSS_PROFIT;
import static com.sanshy.buysellinventory.Buy.GROSS_PROFIT;
import static com.sanshy.buysellinventory.Buy.GROSS_PROFIT_PAID_BY_ON_HOLD_CUSTOMER;
import static com.sanshy.buysellinventory.Buy.NET_PROFIT;
import static com.sanshy.buysellinventory.Buy.ON_HOLD_GROSS_PROFIT;
import static com.sanshy.buysellinventory.Buy.TOTAL_BUY;
import static com.sanshy.buysellinventory.Buy.TOTAL_CASH_BUY;
import static com.sanshy.buysellinventory.Buy.TOTAL_CASH_SELL;
import static com.sanshy.buysellinventory.Buy.TOTAL_EXPENDITURE;
import static com.sanshy.buysellinventory.Buy.TOTAL_HOLD_PAID_BY_CUSTOMER;
import static com.sanshy.buysellinventory.Buy.TOTAL_HOLD_PAID_TO_SUPPLIER;
import static com.sanshy.buysellinventory.Buy.TOTAL_ON_HOLD_BUY;
import static com.sanshy.buysellinventory.Buy.TOTAL_ON_HOLD_SELL;
import static com.sanshy.buysellinventory.Buy.TOTAL_SELL;
import static com.sanshy.buysellinventory.MyUserStaticClass.isPaid;
import static com.sanshy.buysellinventory.MyUserStaticClass.userIdMainStatic;

public class Sell extends AppCompatActivity {

    AutoCompleteTextView suggestion_box,suggestion_box2;

    ArrayList<pitem> productList = new ArrayList<>();
    ArrayList<citem> customerList = new ArrayList<>();

    String CustomerName;

    RadioGroup group;
    RadioButton payType;
    RadioButton cash,onHold;
    EditText quantity;
    EditText price;

    String productBuyPrice;
    String productSellPrice;

    AdView adView1,adView2;

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

        cash = findViewById(R.id.cash_sell);
        onHold = findViewById(R.id.on_hold_sell);
        group = findViewById(R.id.radioGroup);

        cash.setChecked(true);

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

        MyProgressBar.ShowProgress(this);
        final DatabaseReference mProductRef = mRootRef.child(userIdMainStatic+"/product");
        final DatabaseReference mCustomerRef = mRootRef.child(userIdMainStatic+"/customer");

        mCustomerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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
                        price.setText(R.string.price_text);

                    }
                });

                quantity.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View view, int i, KeyEvent keyEvent) {
                        try
                        {
                            int checkProduct = 0;
                            for (int l = 0; l < productList.size(); l++)
                            {
                                if (suggestion_box.getText().toString().equals(productList.get(l).getName()))
                                {
                                    checkProduct++;
                                    break;
                                }
                            }
                            if (checkProduct == 0)
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Sell.this);
                                builder.setTitle(getString(R.string.can_not_save))
                                        .setMessage(getString(R.string.please_select_from_product_list_))
                                        .setPositiveButton(getString(R.string.ok_),null)
                                        .create()
                                        .show();
                                MyProgressBar.HideProgress();
                            }
                            String QuantityValue = quantity.getText().toString();
                            if (QuantityValue.isEmpty())
                            {
                                quantity.setError(getString(R.string.fill_it));
                                price.setText(R.string.price_text);
                            }
                            else if (suggestion_box.getText().toString().isEmpty()){
                                suggestion_box.setError(getString(R.string.fill_it));
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
            builder.setTitle(getString(R.string.can_not_save))
                    .setMessage(getString(R.string.please_select_from_product_list_))
                    .setPositiveButton(getString(R.string.ok_),null)
                    .create()
                    .show();
            MyProgressBar.HideProgress();
return;
        }
        int id = group.getCheckedRadioButtonId();

        String tempPayType;

        if (id==R.id.cash_sell){
            tempPayType = "Cash";
        }else{
            tempPayType = "On Hold";
        }


        payType = findViewById(id);

        final String PayType = tempPayType;


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
        if (CustomerName.isEmpty() && PayType.equals("On Hold"))
        {
            suggestion_box2.setError(getString(R.string.select_customer_name));
            MyProgressBar.HideProgress();
return;
        }
        if (PayType.equals("Cash")){
            if (!(CustomerName.isEmpty())){
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
                    builder.setTitle(getString(R.string.can_not_save))
                            .setMessage(getString(R.string.please_select_from_customer_list_))
                            .setPositiveButton(getString(R.string.ok_),null)
                            .create()
                            .show();
                    MyProgressBar.HideProgress();
                    return;
                }
            }
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
                builder.setTitle(getString(R.string.can_not_save))
                        .setMessage(getString(R.string.please_select_from_customer_list_))
                        .setPositiveButton(getString(R.string.ok_),null)
                        .create()
                        .show();
                MyProgressBar.HideProgress();
return;
            }
        }
        if (Price.equals(getString(R.string.price_text)))
        {
            String feedbackText = getResources().getString(R.string.feedback_request_text);
            Toast.makeText(Sell.this, feedbackText, Toast.LENGTH_SHORT).show();
            MyProgressBar.HideProgress();
return;
        }
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        final String Date = dateFormat.format(date);
        final DatabaseReference mSellRef = mRootRef.child(userIdMainStatic+"/sell");

        final DatabaseReference mStockRef = mRootRef.child(userIdMainStatic+"/stock/"+ProductName);
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
                        builder.setTitle(getString(R.string.stock_text))
                                .setMessage(getString(R.string.out_of_stock_))
                                .setPositiveButton(getString(R.string.ok_), new DialogInterface.OnClickListener() {
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
                        builder.setTitle(getString(R.string.stock_text))
                                .setMessage(getString(R.string.select_quantity))
                                .setPositiveButton(getString(R.string.ok_), null)
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


                final DatabaseReference mOnHoldCustomerRef = mRootRef.child(userIdMainStatic+"/onHoldCustomer/"+CustomerName);
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
                final DatabaseReference mStatementInventory = mRootRef.child(userIdMainStatic+"/Statement/Inventory/"+Date);
                mStatementInventory.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        double sellMoney = Double.parseDouble(Price);
                        double grossProfitMoney = finalGrossProfit;
                        if (dataSnapshot.exists()){

                            String tSellCloud = (String) dataSnapshot.child(TOTAL_SELL).getValue();
                            String tCashSellCloud = (String) dataSnapshot.child(TOTAL_CASH_SELL).getValue();
                            String tOnHoldSellCloud = (String) dataSnapshot.child(TOTAL_ON_HOLD_SELL).getValue();

                            String tGrossProfit = (String) dataSnapshot.child(GROSS_PROFIT).getValue();
                            String tCashGrossProfit = (String) dataSnapshot.child(CASH_GROSS_PROFIT).getValue();
                            String tOnHoldGrossProfit = (String) dataSnapshot.child(ON_HOLD_GROSS_PROFIT).getValue();

                            String tNetProfit = (String) dataSnapshot.child(NET_PROFIT).getValue();

                            double sellCloud = Double.parseDouble(tSellCloud);
                            double cashSellCloud  = Double.parseDouble(tCashSellCloud);
                            double onHoldSellCloud = Double.parseDouble(tOnHoldSellCloud);

                            double grossProfit = Double.parseDouble(tGrossProfit);
                            double cashGrossProfit = Double.parseDouble(tCashGrossProfit);
                            double onHoldGrossProfit = Double.parseDouble(tOnHoldGrossProfit);

                            double netProfit = Double.parseDouble(tNetProfit);

                            double nowSell = sellMoney+sellCloud;

                            double nowGrossProfit = grossProfitMoney+grossProfit;

                            double nowNetProfit = grossProfitMoney+netProfit;

                            String saveSell = String.valueOf(nowSell);
                            mStatementInventory.child(TOTAL_SELL).setValue(saveSell);

                            String saveGrossProfit = String.valueOf(nowGrossProfit);
                            mStatementInventory.child(GROSS_PROFIT).setValue(saveGrossProfit);

                            String saveNetProfit = String.valueOf(nowNetProfit);
                            mStatementInventory.child(NET_PROFIT).setValue(saveNetProfit);

                            if (PayType.equals("On Hold")){
                                double nowOnHoldSell = onHoldSellCloud+sellMoney;
                                double nowOnHoldGrossProfit = onHoldGrossProfit+grossProfitMoney;

                                String saveOnHoldSell = String.valueOf(nowOnHoldSell);
                                mStatementInventory.child(TOTAL_ON_HOLD_SELL).setValue(saveOnHoldSell);
                                String saveOnHoldGrossProfit = String.valueOf(nowOnHoldGrossProfit);
                                mStatementInventory.child(ON_HOLD_GROSS_PROFIT).setValue(saveOnHoldGrossProfit);

                            }else{
                                double nowCashSell = cashSellCloud+sellMoney;
                                double nowCashGrossProfit = cashGrossProfit+grossProfitMoney;

                                String saveCashSell = String.valueOf(nowCashSell);
                                mStatementInventory.child(TOTAL_CASH_SELL).setValue(saveCashSell);
                                String saveCashGrossProfit = String.valueOf(nowCashGrossProfit);
                                mStatementInventory.child(CASH_GROSS_PROFIT).setValue(saveCashGrossProfit);

                            }

                        }
                        else {
                            String saveNetProfit = String.valueOf(grossProfitMoney);
                            mStatementInventory.child(NET_PROFIT).setValue(saveNetProfit);

                            String saveSell = String.valueOf(sellMoney);
                            mStatementInventory.child(TOTAL_SELL).setValue(saveSell);

                            mStatementInventory.child(GROSS_PROFIT).setValue(saveNetProfit);

                            if (PayType.equals("On Hold")){
                                mStatementInventory.child(ON_HOLD_GROSS_PROFIT).setValue(saveNetProfit);
                                mStatementInventory.child(TOTAL_ON_HOLD_SELL).setValue(saveSell);
                                mStatementInventory.child(CASH_GROSS_PROFIT).setValue("0");
                                mStatementInventory.child(TOTAL_CASH_SELL).setValue("0");
                            }else {
                                mStatementInventory.child(CASH_GROSS_PROFIT).setValue(saveNetProfit);
                                mStatementInventory.child(TOTAL_CASH_SELL).setValue(saveSell);
                                mStatementInventory.child(ON_HOLD_GROSS_PROFIT).setValue("0");
                                mStatementInventory.child(TOTAL_ON_HOLD_SELL).setValue("0");
                            }

                            mStatementInventory.child(TOTAL_CASH_BUY).setValue("0");
                            mStatementInventory.child(TOTAL_ON_HOLD_BUY).setValue("0");
                            mStatementInventory.child(TOTAL_BUY).setValue("0");
                            mStatementInventory.child(TOTAL_HOLD_PAID_TO_SUPPLIER).setValue("0");
                            mStatementInventory.child(TOTAL_HOLD_PAID_BY_CUSTOMER).setValue("0");
                            mStatementInventory.child(GROSS_PROFIT_PAID_BY_ON_HOLD_CUSTOMER).setValue("0");
                            mStatementInventory.child(TOTAL_EXPENDITURE).setValue("0");
                        }

                        MyProgressBar.HideProgress();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        MyProgressBar.HideProgress();
                    }
                });
                MyDialogBox.ShowDialog(Sell.this,getString(R.string.product_sell_done));
                Toast.makeText(Sell.this, getString(R.string.product_sell_done), Toast.LENGTH_SHORT).show();
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
