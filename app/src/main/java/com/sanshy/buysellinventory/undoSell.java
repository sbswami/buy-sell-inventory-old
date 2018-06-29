package com.sanshy.buysellinventory;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
import static com.sanshy.buysellinventory.MyDialogBox.DateRequestDialog;
import static com.sanshy.buysellinventory.MyUserStaticClass.userIdMainStatic;

public class undoSell extends AppCompatActivity {

    AutoCompleteTextView suggestion_box4;

    ListView listView;


    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();

    ArrayList<String> Product = new ArrayList<>();
    ArrayList<String> date = new ArrayList<>();
    ArrayList<String> money = new ArrayList<>();
    ArrayList<String> supplier = new ArrayList<>();
    ArrayList<String> mode = new ArrayList<>();
    ArrayList<String> quantity = new ArrayList<>();
    ArrayList<String> sellPrice = new ArrayList<>();
    ArrayList<String> buyPrice = new ArrayList<>();
    ArrayList<String> keyId = new ArrayList<>();
    private int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_undo_sell);

        suggestion_box4 = findViewById(R.id.suggestion_box4);
        listView = findViewById(R.id.listView);
//        AdView adView1;
//        adView1 = findViewById(R.id.adView);
//
//        adView1.loadAd(new AdRequest.Builder().build());


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                try{
                    final String DateText =date.get(i);
                    final String ProductText = Product.get(i);
                    final String CustomerText =supplier.get(i);
                    final String QuantityText = quantity.get(i);
                    final String MoneyText = money.get(i);
                    final String ModeText = mode.get(i);
                    final String KeyIdText = keyId.get(i);
                    final String productSellPrice = sellPrice.get(i);
                    final String productBuyPrice = buyPrice.get(i);

                    double buyP = Double.parseDouble(productBuyPrice);
                    double sellP = Double.parseDouble(productSellPrice);
                    double quant = Double.parseDouble(QuantityText);
                    final double grossProfitOfSell = (sellP-buyP)*quant;

                    AlertDialog.Builder builder = new AlertDialog.Builder(undoSell.this);
                    builder.setTitle(R.string.sell_deatail)
                            .setMessage(getString(R.string.date__)+DateText+"\n" +
                                    getString(R.string.product_name__)+ProductText+"\n" +
                                    getString(R.string.customer__) +CustomerText +"\n"+
                                    getString(R.string.quantity__) + QuantityText+"\n"+
                                    getString(R.string.money__) +MoneyText+"\n"+
                                    getString(R.string.mode__)+ModeText)
                            .setPositiveButton(getString(R.string.undo), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (ModeText.equals(getString(R.string.cash_text))){
                                        final DatabaseReference mStockRef = mRootRef.child(userIdMainStatic+"/stock/"+ProductText);
                                        final String[] temp = new String[1];
                                        MyProgressBar.ShowProgress(undoSell.this);
tempo.clear();
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
                                                double New = Double.parseDouble(QuantityText);

                                                result = Old + New;
                                                String Result = result + "";
                                                System.out.println(Result);
                                                mStockRef.child("quantity").setValue(Result);
                                                DatabaseReference mUndoSellRef = mRootRef.child(userIdMainStatic+"/sell/"+KeyIdText);
                                                mUndoSellRef.removeValue();

                                                final DatabaseReference mStatementInventory = mRootRef.child(userIdMainStatic+"/Statement/Inventory/"+DateText);
                                                mStatementInventory.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        double sellMoney = Double.parseDouble(MoneyText);
                                                        double grossProfitMoney = grossProfitOfSell;
                                                        if (dataSnapshot.exists()){

                                                            String tSellCloud = (String) dataSnapshot.child(TOTAL_SELL).getValue();
                                                            String tCashSellCloud = (String) dataSnapshot.child(TOTAL_CASH_SELL).getValue();

                                                            String tGrossProfit = (String) dataSnapshot.child(GROSS_PROFIT).getValue();
                                                            String tCashGrossProfit = (String) dataSnapshot.child(CASH_GROSS_PROFIT).getValue();

                                                            String tNetProfit = (String) dataSnapshot.child(NET_PROFIT).getValue();

                                                            double sellCloud = Double.parseDouble(tSellCloud);
                                                            double cashSellCloud  = Double.parseDouble(tCashSellCloud);

                                                            double grossProfit = Double.parseDouble(tGrossProfit);
                                                            double cashGrossProfit = Double.parseDouble(tCashGrossProfit);

                                                            double netProfit = Double.parseDouble(tNetProfit);

                                                            double nowSell = sellCloud - sellMoney;

                                                            double nowGrossProfit = grossProfit-grossProfitMoney;

                                                            double nowNetProfit = netProfit - grossProfitMoney;

                                                            String saveSell = String.valueOf(nowSell);
                                                            mStatementInventory.child(TOTAL_SELL).setValue(saveSell);

                                                            String saveGrossProfit = String.valueOf(nowGrossProfit);
                                                            mStatementInventory.child(GROSS_PROFIT).setValue(saveGrossProfit);

                                                            String saveNetProfit = String.valueOf(nowNetProfit);
                                                            mStatementInventory.child(NET_PROFIT).setValue(saveNetProfit);

                                                            double nowCashSell = cashSellCloud-sellMoney;
                                                            double nowCashGrossProfit = cashGrossProfit-grossProfitMoney;

                                                            String saveCashSell = String.valueOf(nowCashSell);
                                                            mStatementInventory.child(TOTAL_CASH_SELL).setValue(saveCashSell);
                                                            String saveCashGrossProfit = String.valueOf(nowCashGrossProfit);
                                                            mStatementInventory.child(CASH_GROSS_PROFIT).setValue(saveCashGrossProfit);


                                                        }
                                                        MyProgressBar.HideProgress();
tempo.add(1);
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                                        MyProgressBar.HideProgress();
tempo.add(1);
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                MyProgressBar.HideProgress();
tempo.add(1);
                                            }
                                        });

                                    }
                                    else if (ModeText.equals(getString(R.string.on_hold_text)))
                                    {
                                        final DatabaseReference mStockRef = mRootRef.child(userIdMainStatic+"/stock/"+ProductText);
                                        final String[] temp = new String[1];
                                        MyProgressBar.ShowProgress(undoSell.this);
tempo.clear();
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
                                                double New = Double.parseDouble(QuantityText);
                                                double grossProfit = 0;
                                                try
                                                {
                                                    double quan = Double.parseDouble(QuantityText);
                                                    grossProfit = quan * Double.parseDouble(productSellPrice) - quan * Double.parseDouble(productBuyPrice);
                                                }catch(Exception ex){}

                                                final double finalGrossProfit = grossProfit;
                                                final double finalOld = Old;
                                                final double finalNew = New;
                                                MyProgressBar.ShowProgress(undoSell.this);
tempo.clear();
                                                final DatabaseReference mOnHoldCustomerRef = mRootRef.child(userIdMainStatic+"/onHoldCustomer/"+CustomerText);
                                                mOnHoldCustomerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        try
                                                        {
                                                            double OldCustomerM = Double.parseDouble(dataSnapshot.child("onHoldMoney").getValue(String.class));
                                                            double NewCustomerM = Double.parseDouble(MoneyText);
                                                            if (OldCustomerM<NewCustomerM){
                                                                String canUndoText = getResources().getString(R.string.can_not_undo_customer_money_text);
                                                                AlertDialog.Builder builder1 = new AlertDialog.Builder(undoSell.this);
                                                                builder1.setTitle(getString(R.string.unable))
                                                                        .setMessage(canUndoText)
                                                                        .setPositiveButton(getString(R.string.ok_),null)
                                                                        .create().show();
                                                                MyProgressBar.HideProgress();
tempo.add(1);
                                                            }
                                                            else {
                                                                double OldProfit = Double.parseDouble(dataSnapshot.child("grossProfit").getValue(String.class));
                                                                double resultProfit = OldProfit - finalGrossProfit;
                                                                mOnHoldCustomerRef.child("grossProfit").setValue(resultProfit+"");
                                                                double resultCustomerResult = OldCustomerM - NewCustomerM;
                                                                mOnHoldCustomerRef.child("onHoldMoney").setValue(resultCustomerResult+"");

                                                                double Qresult = finalOld + finalNew;
                                                                String Result = Qresult + "";
                                                                System.out.println(Result);
                                                                mStockRef.child("quantity").setValue(Result);

                                                                DatabaseReference mUndoSellRef = mRootRef.child(userIdMainStatic+"/sell/"+KeyIdText);
                                                                mUndoSellRef.removeValue();

                                                                final DatabaseReference mStatementInventory = mRootRef.child(userIdMainStatic+"/Statement/Inventory/"+DateText);
                                                                mStatementInventory.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                        double sellMoney = Double.parseDouble(MoneyText);
                                                                        double grossProfitMoney = finalGrossProfit;
                                                                        if (dataSnapshot.exists()){

                                                                            String tSellCloud = (String) dataSnapshot.child(TOTAL_SELL).getValue();
                                                                            String tOnHoldSellCloud = (String) dataSnapshot.child(TOTAL_ON_HOLD_SELL).getValue();

                                                                            String tGrossProfit = (String) dataSnapshot.child(GROSS_PROFIT).getValue();
                                                                            String tOnHoldGrossProfit = (String) dataSnapshot.child(ON_HOLD_GROSS_PROFIT).getValue();

                                                                            String tNetProfit = (String) dataSnapshot.child(NET_PROFIT).getValue();

                                                                            double sellCloud = Double.parseDouble(tSellCloud);
                                                                            double onHoldSellCloud = Double.parseDouble(tOnHoldSellCloud);

                                                                            double grossProfit = Double.parseDouble(tGrossProfit);
                                                                            double onHoldGrossProfit = Double.parseDouble(tOnHoldGrossProfit);

                                                                            double netProfit = Double.parseDouble(tNetProfit);

                                                                            double nowSell = sellCloud - sellMoney;

                                                                            double nowGrossProfit = grossProfit - grossProfitMoney;

                                                                            double nowNetProfit = netProfit - grossProfitMoney;

                                                                            String saveSell = String.valueOf(nowSell);
                                                                            mStatementInventory.child(TOTAL_SELL).setValue(saveSell);

                                                                            String saveGrossProfit = String.valueOf(nowGrossProfit);
                                                                            mStatementInventory.child(GROSS_PROFIT).setValue(saveGrossProfit);

                                                                            String saveNetProfit = String.valueOf(nowNetProfit);
                                                                            mStatementInventory.child(NET_PROFIT).setValue(saveNetProfit);

                                                                            double nowOnHoldSell = onHoldSellCloud-sellMoney;
                                                                            double nowOnHoldGrossProfit = onHoldGrossProfit-grossProfitMoney;

                                                                            String saveOnHoldSell = String.valueOf(nowOnHoldSell);
                                                                            mStatementInventory.child(TOTAL_ON_HOLD_SELL).setValue(saveOnHoldSell);
                                                                            String saveOnHoldGrossProfit = String.valueOf(nowOnHoldGrossProfit);
                                                                            mStatementInventory.child(ON_HOLD_GROSS_PROFIT).setValue(saveOnHoldGrossProfit);
                                                                        }

                                                                        MyProgressBar.HideProgress();
tempo.add(1);
                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                                                        MyProgressBar.HideProgress();
tempo.add(1);
                                                                    }
                                                                });
                                                            }


                                                        }
                                                        catch (Exception ex)
                                                        {
                                                            System.out.println(ex);
                                                        }
                                                        finally {
                                                            MyProgressBar.HideProgress();
tempo.add(1);
                                                        }

                                                        MyProgressBar.HideProgress();
tempo.add(1);

                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                                        MyProgressBar.HideProgress();
tempo.add(1);
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                    else
                                    {
                                        String feedbackText = getResources().getString(R.string.feedback_request_text);
                                        Toast.makeText(undoSell.this, feedbackText, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .setNegativeButton(getString(R.string.cancel_text),null)
                            .create().show();
                }catch (Exception ex){}

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference mOnHoldSupplier = mRootRef.child(userIdMainStatic+"/sell");
        MyProgressBar.ShowProgress(this);
tempo.clear();
        mOnHoldSupplier.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Product.clear();
                date.clear();
                money.clear();
                supplier.clear();
                mode.clear();
                quantity.clear();
                keyId.clear();
                buyPrice.clear();
                sellPrice.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    Product.add(dataSnapshot1.child("productName").getValue(String.class));
                    date.add(dataSnapshot1.child("date").getValue(String.class));
                    money.add(dataSnapshot1.child("price").getValue(String.class));
                    supplier.add(dataSnapshot1.child("customerName").getValue(String.class));
                    String tempMode = dataSnapshot1.child("mode").getValue(String.class);
                    if (tempMode.equals("On Hold")){
                        tempMode = getString(R.string.on_hold_text);
                    }else{
                        tempMode = getString(R.string.cash_text);
                    }
                    mode.add(tempMode);
                    quantity.add(dataSnapshot1.child("quantity").getValue(String.class));
                    keyId.add(dataSnapshot1.child("sid").getValue(String.class));
                    buyPrice.add(dataSnapshot1.child("productBuyPrice").getValue(String.class));
                    sellPrice.add(dataSnapshot1.child("productSellPrice").getValue(String.class));

                }
                final String productList[] = new String[Product.size()+1];
                String Date[] = new String[date.size()+1];
                String Amount[] = new String[money.size()+1];
                String Supplier[] = new String[supplier.size()+1];
                String Mode[] = new String[mode.size()+1];
                String Quantity[] = new String[quantity.size()+1];

                double total = 0;
                int quant = 0;
                int count = 0;
                for (int i = 0; i < Product.size(); i++)
                {
                    productList[i] = Product.get(i);
                    Date[i] = date.get(i);
                    Amount[i] = money.get(i);
                    Quantity[i] = quantity.get(i);
                    Mode[i] = mode.get(i);
                    Supplier[i] = supplier.get(i);
                    count++;
                    try
                    {
                        total += Double.parseDouble(Amount[i]);
                        quant += (int)Double.parseDouble(Quantity[i]);
                    }catch (Exception es)
                    {

                    }
                }

                Date[Product.size()] = count+getString(R.string._total);
                Amount[money.size()] = total+"";
                Quantity[quantity.size()] = quant+"";

                statementAdapter historyPayList = new statementAdapter(undoSell.this,Date,productList,Supplier,Quantity,Amount,Mode);
                listView.setAdapter(historyPayList);

                MyProgressBar.HideProgress();
tempo.add(1);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                MyProgressBar.HideProgress();
tempo.add(1);
            }
        });

        final ArrayList<String> cList = new ArrayList<>();
        DatabaseReference mCustomerRef = mRootRef.child(userIdMainStatic+"/product");
        mCustomerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    cList.add(dataSnapshot1.child("name").getValue(String.class));
                }
                String list[] = new String[cList.size()];
                for (int i = 0; i < cList.size() ; i++)
                {
                    list[i] = cList.get(i);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(undoSell.this,android.R.layout.simple_spinner_dropdown_item, Arrays.asList(list));

                suggestion_box4.setAdapter(adapter);
                suggestion_box4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        DatabaseReference mSearchRef = mRootRef.child(userIdMainStatic+"/sell");

                        Query query = mSearchRef.orderByChild("productName").equalTo(suggestion_box4.getText().toString());
                        MyProgressBar.ShowProgress(undoSell.this);
tempo.clear();
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Product.clear();
                                date.clear();
                                money.clear();
                                supplier.clear();
                                mode.clear();
                                quantity.clear();
                                keyId.clear();
                                buyPrice.clear();
                                sellPrice.clear();
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                                {
                                    Product.add(dataSnapshot1.child("productName").getValue(String.class));
                                    date.add(dataSnapshot1.child("date").getValue(String.class));
                                    money.add(dataSnapshot1.child("price").getValue(String.class));
                                    supplier.add(dataSnapshot1.child("customerName").getValue(String.class));
                                    String tempMode = dataSnapshot1.child("mode").getValue(String.class);
                    if (tempMode.equals("On Hold")){
                        tempMode = getString(R.string.on_hold_text);
                    }else{
                        tempMode = getString(R.string.cash_text);
                    }
                    mode.add(tempMode);
                                    quantity.add(dataSnapshot1.child("quantity").getValue(String.class));
                                    keyId.add(dataSnapshot1.child("sid").getValue(String.class));
                                    buyPrice.add(dataSnapshot1.child("productBuyPrice").getValue(String.class));
                                    sellPrice.add(dataSnapshot1.child("productSellPrice").getValue(String.class));
                                }
                                final String productList[] = new String[Product.size()+1];
                                String Date[] = new String[date.size()+1];
                                String Amount[] = new String[money.size()+1];
                                String Supplier[] = new String[supplier.size()+1];
                                String Mode[] = new String[mode.size()+1];
                                String Quantity[] = new String[quantity.size()+1];

                                double total = 0;
                                int quant = 0;
                                int count = 0;
                                for (int i = 0; i < Product.size(); i++)
                                {
                                    productList[i] = Product.get(i);
                                    Date[i] = date.get(i);
                                    Amount[i] = money.get(i);
                                    Quantity[i] = quantity.get(i);
                                    Mode[i] = mode.get(i);
                                    Supplier[i] = supplier.get(i);
                                    count++;
                                    try
                                    {
                                        total += Double.parseDouble(Amount[i]);
                                        quant += (int)Double.parseDouble(Quantity[i]);
                                    }catch (Exception es)
                                    {

                                    }
                                }

                                Date[Product.size()] = count+getString(R.string._total);
                                Amount[money.size()] = total+"";
                                Quantity[quantity.size()] = quant+"";

                                statementAdapter historyPayList = new statementAdapter(undoSell.this,Date,productList,Supplier,Quantity,Amount,Mode);
                                listView.setAdapter(historyPayList);

                                MyProgressBar.HideProgress();
tempo.add(1);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                MyProgressBar.HideProgress();
tempo.add(1);
                            }
                        });



                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    ArrayList<Integer> tempo = new ArrayList<>();
    @Override
    protected void onPause() {
        super.onPause();

        if (tempo.size()==0){
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }


    int fday = 0;
    int fmonth = 0;
    int fYear = 0;
    int tday = 0;
    int tmonth = 0;
    int tYear = 0;
    public void from(View view)
    {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        fday = dayOfMonth;
                        fmonth = ++monthOfYear;
                        fYear = year;
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();

        System.out.println(fday+fmonth+fYear+"");
    }
    public void to(View view)
    {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        tday = dayOfMonth;
                        tmonth = ++monthOfYear;
                        tYear = year;
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
    public static List<Date> dateInterval(Date initial, Date last) {
        List<Date> dates = new ArrayList<Date>();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(initial);

        while (calendar.getTime().before(last)) {
            Date result = calendar.getTime();
            dates.add(result);
            calendar.add(Calendar.DATE, 1);
        }

        return dates;
    }
    public void search(View view){
        String searchProduct = suggestion_box4.getText().toString();
        if (searchProduct.isEmpty())
        {
            if ((fday == 0) && (fmonth == 0) && (fYear == 0) && (tday == 0) && (tmonth == 0) && (tYear == 0))
            {
                DateRequestDialog(this);
                return;
            }
            DatabaseReference mSellRef = mRootRef.child(userIdMainStatic+"/sell");
            List<Date> dates = new ArrayList<>();
            String sDate1 = fday+"/"+fmonth+"/"+fYear;
            String sDate2 = tday+"/"+tmonth+"/"+tYear;
            DateFormat simple = new SimpleDateFormat("dd/MM/yyyy");
            try
            {
                Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);
                Date date2=new SimpleDateFormat("dd/MM/yyyy").parse(sDate2);
                dates = dateInterval(date1,date2);
                final ArrayList<String> Dates = new ArrayList<>();
                for (int i = 0; i< dates.size(); i++)
                {
                    Dates.add(simple.format(dates.get(i)));
                }
                Dates.add(simple.format(date2));

                final String betweenDates[] = new String[Dates.size()];
                for (int i = 0; i < Dates.size(); i++)
                {
                    betweenDates[i] = Dates.get(i);
                }
                Product.clear();
                date.clear();
                money.clear();
                mode.clear();
                quantity.clear();
                supplier.clear();
                keyId.clear();
                buyPrice.clear();
                sellPrice.clear();
                MyProgressBar.ShowProgress(this);
tempo.clear();
                for (int i = 0; i < Dates.size(); i++)
                {
                    final int iFinal = i;
                    Query query = mSellRef.orderByChild("date").equalTo(betweenDates[i]);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                            {
                                Product.add(dataSnapshot1.child("productName").getValue(String.class));
                                date.add(dataSnapshot1.child("date").getValue(String.class));
                                money.add(dataSnapshot1.child("price").getValue(String.class));
                                String tempMode = dataSnapshot1.child("mode").getValue(String.class);
                    if (tempMode.equals("On Hold")){
                        tempMode = getString(R.string.on_hold_text);
                    }else{
                        tempMode = getString(R.string.cash_text);
                    }
                    mode.add(tempMode);
                                quantity.add(dataSnapshot1.child("quantity").getValue(String.class));
                                supplier.add(dataSnapshot1.child("customerName").getValue(String.class));
                                keyId.add(dataSnapshot1.child("sid").getValue(String.class));
                                buyPrice.add(dataSnapshot1.child("productBuyPrice").getValue(String.class));
                                sellPrice.add(dataSnapshot1.child("productSellPrice").getValue(String.class));

                            }
                            final String productList[] = new String[Product.size()+1];
                            String Date[] = new String[date.size()+1];
                            String Amount[] = new String[money.size()+1];
                            String Mode[] = new String[mode.size()+1];
                            String Quantity[] = new String[quantity.size()+1];
                            String Supplier[] = new String[supplier.size()+1];

                            double total = 0;
                            int quant = 0;
                            int count = 0;
                            for (int i = 0; i < Product.size(); i++)
                            {
                                productList[i] = Product.get(i);
                                Date[i] = date.get(i);
                                Amount[i] = money.get(i);
                                Quantity[i] = quantity.get(i);
                                Mode[i] = mode.get(i);
                                Supplier[i] = supplier.get(i);
                                count++;
                                try
                                {
                                    total += Double.parseDouble(Amount[i]);
                                    quant += (int)Double.parseDouble(Quantity[i]);
                                }catch (Exception es)
                                {

                                }
                            }

                            Date[Product.size()] = count+getString(R.string._total);
                            Amount[money.size()] = total+"";
                            Quantity[quantity.size()] = quant+"";

                            statementAdapter historyPayList = new statementAdapter(undoSell.this,Date,productList,Supplier,Quantity,Amount,Mode);
                            listView.setAdapter(historyPayList);

                            if (iFinal==(Dates.size()-1)){
                                MyProgressBar.HideProgress();
tempo.add(1);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            MyProgressBar.HideProgress();
tempo.add(1);
                        }
                    });

                }
            }
            catch (Exception ex)
            {

            }

        }
        else {
            if ((fday == 0) && (fmonth == 0) && (fYear == 0) && (tday == 0) && (tmonth == 0) && (tYear == 0))
            {
                DateRequestDialog(this);
                return;
            }
            DatabaseReference mSellRef = mRootRef.child(userIdMainStatic+"/sell");
            List<Date> dates = new ArrayList<>();
            String sDate1 = fday+"/"+fmonth+"/"+fYear;
            String sDate2 = tday+"/"+tmonth+"/"+tYear;
            DateFormat simple = new SimpleDateFormat("dd/MM/yyyy");
            try
            {
                Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);
                Date date2=new SimpleDateFormat("dd/MM/yyyy").parse(sDate2);
                dates = dateInterval(date1,date2);
                final ArrayList<String> Dates = new ArrayList<>();
                for (int i = 0; i< dates.size(); i++)
                {
                    Dates.add(simple.format(dates.get(i)));
                }
                Dates.add(simple.format(date2));

                final String betweenDates[] = new String[Dates.size()];
                for (int i = 0; i < Dates.size(); i++)
                {
                    betweenDates[i] = Dates.get(i);
                }
                supplier.clear();
                Product.clear();
                date.clear();
                money.clear();
                mode.clear();
                quantity.clear();
                keyId.clear();
                buyPrice.clear();
                sellPrice.clear();
                MyProgressBar.ShowProgress(this);
tempo.clear();
                for (int i = 0; i < Dates.size(); i++)
                {
                    final int iFinal = i;
                    Query query = mSellRef.orderByChild("date_productName").equalTo(betweenDates[i]+"_"+searchProduct);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                            {
                                Product.add(dataSnapshot1.child("productName").getValue(String.class));
                                date.add(dataSnapshot1.child("date").getValue(String.class));
                                money.add(dataSnapshot1.child("price").getValue(String.class));
                                String tempMode = dataSnapshot1.child("mode").getValue(String.class);
                    if (tempMode.equals("On Hold")){
                        tempMode = getString(R.string.on_hold_text);
                    }else{
                        tempMode = getString(R.string.cash_text);
                    }
                    mode.add(tempMode);
                                quantity.add(dataSnapshot1.child("quantity").getValue(String.class));
                                supplier.add(dataSnapshot1.child("customerName").getValue(String.class));
                                keyId.add(dataSnapshot1.child("sid").getValue(String.class));
                                buyPrice.add(dataSnapshot1.child("productBuyPrice").getValue(String.class));
                                sellPrice.add(dataSnapshot1.child("productSellPrice").getValue(String.class));

                            }
                            final String productList[] = new String[Product.size()+1];
                            String Date[] = new String[date.size()+1];
                            String Amount[] = new String[money.size()+1];
                            String Mode[] = new String[mode.size()+1];
                            String Quantity[] = new String[quantity.size()+1];
                            String Supplier[] = new String[supplier.size()+1];

                            double total = 0;
                            int quant = 0;
                            int count = 0;
                            for (int i = 0; i < Product.size(); i++)
                            {
                                productList[i] = Product.get(i);
                                Date[i] = date.get(i);
                                Amount[i] = money.get(i);
                                Quantity[i] = quantity.get(i);
                                Mode[i] = mode.get(i);
                                Supplier[i] = supplier.get(i);
                                count++;
                                try
                                {
                                    total += Double.parseDouble(Amount[i]);
                                    quant += (int)Double.parseDouble(Quantity[i]);
                                }catch (Exception es)
                                {

                                }
                            }

                            Date[Product.size()] = count+getString(R.string._total);
                            Amount[money.size()] = total+"";
                            Quantity[quantity.size()] = quant+"";

                            statementAdapter historyPayList = new statementAdapter(undoSell.this,Date,productList,Supplier,Quantity,Amount,Mode);
                            listView.setAdapter(historyPayList);

                           if (iFinal==(Dates.size()-1)){
                               MyProgressBar.HideProgress();
tempo.add(1);
                           }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            MyProgressBar.HideProgress();
tempo.add(1);
                        }
                    });

                }
            }
            catch (Exception ex)
            {

            }

        }




    }



    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        NetworkConnectivityCheck.connectionCheck(this);

    }

}
