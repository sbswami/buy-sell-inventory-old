package com.sanshy.buysellinventory;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
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

public class UndoBuy extends AppCompatActivity {

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
    ArrayList<String> keyId = new ArrayList<>();
    private int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_undo);

        suggestion_box4 = findViewById(R.id.suggestion_box4);
        listView = findViewById(R.id.listView);
        AdView adView1;
        adView1 = findViewById(R.id.adView);

        adView1.loadAd(new AdRequest.Builder().build());


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                try{
                    String DateText =date.get(i);
                    final String ProductText = Product.get(i);
                    final String SupplierText =supplier.get(i);
                    final String QuantityText = quantity.get(i);
                    final String MoneyText = money.get(i);
                    final String ModeText = mode.get(i);
                    final String KeyIdText = keyId.get(i);

                    AlertDialog.Builder builder = new AlertDialog.Builder(UndoBuy.this);
                    builder.setTitle("Buy Detail!")
                            .setMessage("Date : "+DateText+"\n" +
                                    "Product Name : "+ProductText+"\n" +
                                    "Supplier : " +SupplierText +"\n"+
                                    "Quantity : " + QuantityText+"\n"+
                                    "Money : " +MoneyText+"\n"+
                                    "Mode: "+ModeText)
                            .setPositiveButton("Undo!!", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (ModeText.equals("Cash")){
                                        final DatabaseReference mStockRef = mRootRef.child(user.getUid()+"/stock/"+ProductText);
                                        final String[] temp = new String[1];
                                        final ArrayList<String> cont = new ArrayList<>();
                                        mStockRef.child("quantity").addValueEventListener(new ValueEventListener() {

                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {



                                                if (cont.size() == 0)
                                                {   cont.add("kuchh bhi");
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
                                                    if (Old < New){
                                                        String canUndoText = getResources().getString(R.string.can_not_undo_text);
                                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(UndoBuy.this);
                                                        builder1.setTitle("Unable")
                                                                .setMessage(canUndoText)
                                                                .setPositiveButton("OK",null)
                                                                .create().show();
                                                    }
                                                    else{
                                                        result = Old - New;
                                                        String Result = result + "";
                                                        System.out.println(Result);
                                                        mStockRef.child("quantity").setValue(Result);
                                                        DatabaseReference mBuyUndoRef = mRootRef.child(user.getUid()+"/buy/"+KeyIdText);
                                                        mBuyUndoRef.removeValue();
                                                    }

                                                    cont.add("kuchh bhi");

                                                }
                                                cont.add("kuchh bhi");



                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                    }
                                    else if (ModeText.equals("On Hold"))
                                    {
                                        final DatabaseReference mStockRef = mRootRef.child(user.getUid()+"/stock/"+ProductText);
                                        final String[] temp = new String[1];
                                        final ArrayList<String> cont = new ArrayList<>();
                                        mStockRef.child("quantity").addValueEventListener(new ValueEventListener() {

                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {



                                                if (cont.size() == 0)
                                                {   cont.add("kuchh bhi");
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
                                                    if (Old < New){
                                                        String canUndoText = getResources().getString(R.string.can_not_undo_text);
                                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(UndoBuy.this);
                                                        builder1.setTitle("Unable")
                                                                .setMessage(canUndoText)
                                                                .setPositiveButton("OK",null)
                                                                .create().show();
                                                    }
                                                    else{
                                                        final double finalOld = Old;
                                                        final double finalNew = New;
                                                        final ArrayList<String> hold = new ArrayList<>();
                                                        final DatabaseReference mOnHoldSupplierRef = mRootRef.child(user.getUid()+"/onHoldSupplier/"+SupplierText);
                                                        mOnHoldSupplierRef.child("onHoldMoney").addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                if (hold.size() == 0)
                                                                {
                                                                    try
                                                                    {
                                                                        double OldSupplierM = Double.parseDouble(dataSnapshot.getValue(String.class));
                                                                        double NewSupplierM = Double.parseDouble(MoneyText);
                                                                        if (OldSupplierM<NewSupplierM){
                                                                            String canUndoText = getResources().getString(R.string.can_not_undo_supplier_money_text);
                                                                            AlertDialog.Builder builder1 = new AlertDialog.Builder(UndoBuy.this);
                                                                            builder1.setTitle("Unable")
                                                                                    .setMessage(canUndoText)
                                                                                    .setPositiveButton("OK",null)
                                                                                    .create().show();
                                                                        }
                                                                        else {
                                                                            double resultSupplierResult = OldSupplierM - NewSupplierM;
                                                                            mOnHoldSupplierRef.child("onHoldMoney").setValue(resultSupplierResult+"");

                                                                            double Qresult = finalOld - finalNew;
                                                                            String Result = Qresult + "";
                                                                            System.out.println(Result);
                                                                            mStockRef.child("quantity").setValue(Result);

                                                                            DatabaseReference mBuyUndoRef = mRootRef.child(user.getUid()+"/buy/"+KeyIdText);
                                                                            mBuyUndoRef.removeValue();
                                                                        }

                                                                    }
                                                                    catch (Exception ex)
                                                                    {
                                                                        System.out.println(ex);
                                                                    }

                                                                }
                                                                hold.add("kuch bhi");
                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        });


                                                    }

                                                    cont.add("kuchh bhi");

                                                }
                                                cont.add("kuchh bhi");



                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                    else
                                    {
                                        String feedbackText = getResources().getString(R.string.feedback_request_text);
                                        Toast.makeText(UndoBuy.this, feedbackText, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .setNegativeButton("Cancel",null)
                            .create().show();
                }catch (Exception ex){}

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference mOnHoldSupplier = mRootRef.child(user.getUid()+"/buy");

        mOnHoldSupplier.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Product.clear();
                date.clear();
                money.clear();
                supplier.clear();
                mode.clear();
                quantity.clear();
                keyId.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    Product.add(dataSnapshot1.child("productName").getValue(String.class));
                    date.add(dataSnapshot1.child("date").getValue(String.class));
                    money.add(dataSnapshot1.child("price").getValue(String.class));
                    supplier.add(dataSnapshot1.child("supplierName").getValue(String.class));
                    mode.add(dataSnapshot1.child("mode").getValue(String.class));
                    quantity.add(dataSnapshot1.child("quantity").getValue(String.class));
                    keyId.add(dataSnapshot1.child("bid").getValue(String.class));

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

                statementAdapter historyPayList = new statementAdapter(UndoBuy.this,Date,productList,Supplier,Quantity,Amount,Mode);
                listView.setAdapter(historyPayList);




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final ArrayList<String> cList = new ArrayList<>();
        DatabaseReference mCustomerRef = mRootRef.child(user.getUid()+"/product");
        mCustomerRef.addValueEventListener(new ValueEventListener() {
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
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(UndoBuy.this,android.R.layout.simple_spinner_dropdown_item, Arrays.asList(list));

                suggestion_box4.setAdapter(adapter);
                suggestion_box4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        DatabaseReference mSearchRef = mRootRef.child(user.getUid()+"/buy");

                        Query query = mSearchRef.orderByChild("productName").equalTo(suggestion_box4.getText().toString());

                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Product.clear();
                                date.clear();
                                money.clear();
                                supplier.clear();
                                mode.clear();
                                quantity.clear();
                                keyId.clear();
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                                {
                                    Product.add(dataSnapshot1.child("productName").getValue(String.class));
                                    date.add(dataSnapshot1.child("date").getValue(String.class));
                                    money.add(dataSnapshot1.child("price").getValue(String.class));
                                    supplier.add(dataSnapshot1.child("supplierName").getValue(String.class));
                                    mode.add(dataSnapshot1.child("mode").getValue(String.class));
                                    quantity.add(dataSnapshot1.child("quantity").getValue(String.class));
                                    keyId.add(dataSnapshot1.child("bid").getValue(String.class));
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

                                statementAdapter historyPayList = new statementAdapter(UndoBuy.this,Date,productList,Supplier,Quantity,Amount,Mode);
                                listView.setAdapter(historyPayList);




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
            DatabaseReference mBuyRef = mRootRef.child(user.getUid()+"/buy");
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
                for (int i = 0; i < Dates.size(); i++)
                {
                    Query query = mBuyRef.orderByChild("date").equalTo(betweenDates[i]);
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                            {
                                Product.add(dataSnapshot1.child("productName").getValue(String.class));
                                date.add(dataSnapshot1.child("date").getValue(String.class));
                                money.add(dataSnapshot1.child("price").getValue(String.class));
                                mode.add(dataSnapshot1.child("mode").getValue(String.class));
                                quantity.add(dataSnapshot1.child("quantity").getValue(String.class));
                                supplier.add(dataSnapshot1.child("supplierName").getValue(String.class));
                                keyId.add(dataSnapshot1.child("bid").getValue(String.class));

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


                            statementAdapter historyPayList = new statementAdapter(UndoBuy.this,Date,productList,Supplier,Quantity,Amount,Mode);
                            listView.setAdapter(historyPayList);



                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

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
            DatabaseReference mBuyRef = mRootRef.child(user.getUid()+"/buy");
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
                for (int i = 0; i < Dates.size(); i++)
                {
                    Query query = mBuyRef.orderByChild("date_productName").equalTo(betweenDates[i]+"_"+searchProduct);
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                            {
                                Product.add(dataSnapshot1.child("productName").getValue(String.class));
                                date.add(dataSnapshot1.child("date").getValue(String.class));
                                money.add(dataSnapshot1.child("price").getValue(String.class));
                                mode.add(dataSnapshot1.child("mode").getValue(String.class));
                                quantity.add(dataSnapshot1.child("quantity").getValue(String.class));
                                supplier.add(dataSnapshot1.child("supplierName").getValue(String.class));
                                keyId.add(dataSnapshot1.child("bid").getValue(String.class));

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

                            statementAdapter historyPayList = new statementAdapter(UndoBuy.this,Date,productList,Supplier,Quantity,Amount,Mode);
                            listView.setAdapter(historyPayList);



                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

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