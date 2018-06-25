package com.sanshy.buysellinventory;

import android.app.DatePickerDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

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

import static com.sanshy.buysellinventory.MyUserStaticClass.userIdMainStatic;

public class buyHistoryByProduct extends AppCompatActivity {

    AutoCompleteTextView suggestion_box4;
    ListView listView;

    TextView remainAmount;

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();

    ArrayList<String> Product = new ArrayList<>();
    ArrayList<String> date = new ArrayList<>();
    ArrayList<String> money = new ArrayList<>();
    ArrayList<String> supplier = new ArrayList<>();
    ArrayList<String> mode = new ArrayList<>();
    ArrayList<String> quantity = new ArrayList<>();
    private int mYear, mMonth, mDay, mHour, mMinute;






    DatabaseReference mProductRef = mRootRef.child(userIdMainStatic+"/product");
    DatabaseReference mBuyQuery = mRootRef.child(userIdMainStatic+"/buy");
    Query holdQuery = mBuyQuery.limitToLast(50);







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_history_by_product);

        suggestion_box4 = findViewById(R.id.suggestion_box4);
        listView = findViewById(R.id.listView);
        remainAmount = findViewById(R.id.remainAmount);
        AdView adView1;
        adView1 = findViewById(R.id.adView);

        adView1.loadAd(new AdRequest.Builder().build());
    }

    @Override
    protected void onStart() {
        super.onStart();


        MyProgressBar.ShowProgress(this);

        holdQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Product.clear();
                date.clear();
                money.clear();
                supplier.clear();
                mode.clear();
                quantity.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    Product.add(dataSnapshot1.child("productName").getValue(String.class));
                    date.add(dataSnapshot1.child("date").getValue(String.class));
                    money.add(dataSnapshot1.child("price").getValue(String.class));
                    supplier.add(dataSnapshot1.child("supplierName").getValue(String.class));
                    mode.add(dataSnapshot1.child("mode").getValue(String.class));
                    quantity.add(dataSnapshot1.child("quantity").getValue(String.class));

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

                Date[Product.size()] = count+" Total";
                Amount[money.size()] = total+"";
                Quantity[quantity.size()] = quant+"";

                statementAdapter historyPayList = new statementAdapter(buyHistoryByProduct.this,Date,productList,Supplier,Quantity,Amount,Mode);
                listView.setAdapter(historyPayList);

                remainAmount.setText("Total Money : "+total);
                MyProgressBar.HideProgress();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final ArrayList<String> cList = new ArrayList<>();
        mProductRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
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
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(buyHistoryByProduct.this,android.R.layout.simple_spinner_dropdown_item, Arrays.asList(list));

                suggestion_box4.setAdapter(adapter);
                suggestion_box4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        DatabaseReference mSearchRef = mRootRef.child(userIdMainStatic+"/buy");

                        Query query = mSearchRef.orderByChild("productName").equalTo(suggestion_box4.getText().toString());
                        MyProgressBar.ShowProgress(buyHistoryByProduct.this);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Product.clear();
                                date.clear();
                                money.clear();
                                supplier.clear();
                                mode.clear();
                                quantity.clear();
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                                {
                                    Product.add(dataSnapshot1.child("productName").getValue(String.class));
                                    date.add(dataSnapshot1.child("date").getValue(String.class));
                                    money.add(dataSnapshot1.child("price").getValue(String.class));
                                    supplier.add(dataSnapshot1.child("supplierName").getValue(String.class));
                                    mode.add(dataSnapshot1.child("mode").getValue(String.class));
                                    quantity.add(dataSnapshot1.child("quantity").getValue(String.class));

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

                                Date[Product.size()] = count+" Total";
                                Amount[money.size()] = total+"";
                                Quantity[quantity.size()] = quant+"";

                                statementAdapter historyPayList = new statementAdapter(buyHistoryByProduct.this,Date,productList,Supplier,Quantity,Amount,Mode);
                                listView.setAdapter(historyPayList);

                                remainAmount.setText("Total Money : "+total);

                                MyProgressBar.HideProgress();

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

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
    public void search(View view)
    {
        String searchProduct = suggestion_box4.getText().toString();
        if (searchProduct.isEmpty())
        {
            if ((fday == 0) && (fmonth == 0) && (fYear == 0) && (tday == 0) && (tmonth == 0) && (tYear == 0))
            {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                builder.setTitle("Choose Date")
                        .setMessage("Please Choose Any Date")
                        .setPositiveButton("OK",null)
                        .create()
                        .show();
                return;
            }
            DatabaseReference mBuyRef = mRootRef.child(userIdMainStatic+"/buy");
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
                MyProgressBar.ShowProgress(buyHistoryByProduct.this);
                for (int i = 0; i < Dates.size(); i++)
                {
                    final int iFinal = i;
                    Query query = mBuyRef.orderByChild("date").equalTo(betweenDates[i]);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                            {
                                Product.add(dataSnapshot1.child("productName").getValue(String.class));
                                date.add(dataSnapshot1.child("date").getValue(String.class));
                                money.add(dataSnapshot1.child("price").getValue(String.class));
                                mode.add(dataSnapshot1.child("mode").getValue(String.class));
                                quantity.add(dataSnapshot1.child("quantity").getValue(String.class));
                                supplier.add(dataSnapshot1.child("supplierName").getValue(String.class));

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

                            Date[Product.size()] = count+" Total";
                            Amount[money.size()] = total+"";
                            Quantity[quantity.size()] = quant+"";

                            /*TODO : change Page */
//                            try{
//                                if (Date[Product.size()].equals(betweenDates[Dates.size()])){
//                                    Intent intent = new Intent(sellHistory.this,fiveItemLister.class);
//                                    intent.putExtra("c1",Date);
//                                    intent.putExtra("c2",productList);
//                                    intent.putExtra("c3",Quantity);
//                                    intent.putExtra("c4",Amount);
//                                    intent.putExtra("c5",Mode);
//
//                                    intent.putExtra("col1","Date");
//                                    intent.putExtra("col2","Prod.");
//                                    intent.putExtra("col3","Quan.");
//                                    intent.putExtra("col4","Money");
//                                    intent.putExtra("col5","Mode");
//
//                                    intent.putExtra("total","Total Money : "+total);
//                                    startActivity(intent);
//
//
//                                }
//                            }catch (Exception ex){}

                            statementAdapter historyPayList = new statementAdapter(buyHistoryByProduct.this,Date,productList,Supplier,Quantity,Amount,Mode);
                            listView.setAdapter(historyPayList);

                            remainAmount.setText("Total Money : "+total);

                            if (iFinal==(Dates.size()-1)){

                                MyProgressBar.HideProgress();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            MyProgressBar.HideProgress();
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
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                builder.setTitle("Choose Date")
                        .setMessage("Please Choose Any Date")
                        .setPositiveButton("OK",null)
                        .create()
                        .show();
                return;
            }
            DatabaseReference mBuyRef = mRootRef.child(userIdMainStatic+"/buy");
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
                MyProgressBar.ShowProgress(buyHistoryByProduct.this);
                for (int i = 0; i < Dates.size(); i++)
                {
                    final int iFinal = i;
                    Query query = mBuyRef.orderByChild("date_productName").equalTo(betweenDates[i]+"_"+searchProduct);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                            {
                                Product.add(dataSnapshot1.child("productName").getValue(String.class));
                                date.add(dataSnapshot1.child("date").getValue(String.class));
                                money.add(dataSnapshot1.child("price").getValue(String.class));
                                mode.add(dataSnapshot1.child("mode").getValue(String.class));
                                quantity.add(dataSnapshot1.child("quantity").getValue(String.class));
                                supplier.add(dataSnapshot1.child("supplierName").getValue(String.class));

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

                            Date[Product.size()] = count+" Total";
                            Amount[money.size()] = total+"";
                            Quantity[quantity.size()] = quant+"";

                            /*TODO : Change Page*/
//                            try{
//                                if (Date[Product.size()].equals(betweenDates[Dates.size()])){
//                                    Intent intent = new Intent(sellHistory.this,fiveItemLister.class);
//                                    intent.putExtra("c1",Date);
//                                    intent.putExtra("c2",productList);
//                                    intent.putExtra("c3",Quantity);
//                                    intent.putExtra("c4",Amount);
//                                    intent.putExtra("c5",Mode);
//
//                                    intent.putExtra("col1","Date");
//                                    intent.putExtra("col2","Prod.");
//                                    intent.putExtra("col3","Quan.");
//                                    intent.putExtra("col4","Money");
//                                    intent.putExtra("col5","Mode");
//
//                                    intent.putExtra("total","Total Money : "+total);
//                                    startActivity(intent);
//
//
//                                }
//                            }catch (Exception ex){}

                            statementAdapter historyPayList = new statementAdapter(buyHistoryByProduct.this,Date,productList,Supplier,Quantity,Amount,Mode);
                            listView.setAdapter(historyPayList);

                            remainAmount.setText("Total Money : "+total);

                            if (iFinal==(Dates.size()-1)){
                                MyProgressBar.HideProgress();
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            MyProgressBar.HideProgress();
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
    protected void onPause() {
        super.onPause();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        NetworkConnectivityCheck.connectionCheck(this);
    }



}
