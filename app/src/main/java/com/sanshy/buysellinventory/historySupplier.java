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

public class historySupplier extends AppCompatActivity {

    AutoCompleteTextView suggestion_box4;

    ListView listView;

    TextView remainAmount;

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();

    ArrayList<String> supplierName = new ArrayList<>();
    ArrayList<String> date = new ArrayList<>();
    ArrayList<String> amount = new ArrayList<>();
    private int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_supplier);

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

        DatabaseReference mOnHoldSupplier = mRootRef.child(userIdMainStatic+"/payToSupplier");
        Query query = mOnHoldSupplier.limitToLast(50);
        MyProgressBar.ShowProgress(this);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                supplierName.clear();
                date.clear();
                amount.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    supplierName.add(dataSnapshot1.child("name").getValue(String.class));
                    date.add(dataSnapshot1.child("date").getValue(String.class));
                    amount.add(dataSnapshot1.child("money").getValue(String.class));
                }
                final String SupplierName[] = new String[supplierName.size()+1];
                String Date[] = new String[date.size()+1];
                String Amount[] = new String[amount.size()+1];

                double total = 0;
                int count = 0;
                for (int i = 0; i < supplierName.size(); i++)
                {
                    SupplierName[i] = supplierName.get(i);
                    Date[i] = date.get(i);
                    Amount[i] = amount.get(i);
                    count++;
                    try
                    {
                        total += Double.parseDouble(Amount[i]);
                    }catch (Exception es)
                    {

                    }
                }

                SupplierName[supplierName.size()] = count+" Total";
                Amount[amount.size()] = total+"";
                remainAmount.setText("Total Amount : "+total);
                historyPayListAdapter historyPayList = new historyPayListAdapter(historySupplier.this,SupplierName,Date,Amount);
                listView.setAdapter(historyPayList);

                MyProgressBar.HideProgress();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final ArrayList<String> cList = new ArrayList<>();
        DatabaseReference mCustomerRef = mRootRef.child(userIdMainStatic+"/supplier");
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
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(historySupplier.this,android.R.layout.simple_spinner_dropdown_item, Arrays.asList(list));

                suggestion_box4.setAdapter(adapter);
                suggestion_box4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        DatabaseReference mSearchRef = mRootRef.child(userIdMainStatic+"/payToSupplier/");

                        Query query = mSearchRef.orderByChild("name").equalTo(suggestion_box4.getText().toString());
                        MyProgressBar.ShowProgress(historySupplier.this);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                supplierName.clear();
                                date.clear();
                                amount.clear();
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                                {
                                    supplierName.add(dataSnapshot1.child("name").getValue(String.class));
                                    date.add(dataSnapshot1.child("date").getValue(String.class));
                                    amount.add(dataSnapshot1.child("money").getValue(String.class));
                                }
                                String SupplierName[] = new String[supplierName.size()+1];
                                String Date[] = new String[date.size()+1];
                                String Amount[] = new String[amount.size()+1];

                                double total = 0;
                                int count = 0;
                                for (int i = 0; i < supplierName.size(); i++)
                                {
                                    SupplierName[i] = supplierName.get(i);
                                    Date[i] = date.get(i);
                                    Amount[i] = amount.get(i);
                                    count++;
                                    try
                                    {
                                        total += Double.parseDouble(Amount[i]);
                                    }catch (Exception ex)
                                    {

                                    }
                                }

                                try
                                {
                                    SupplierName[supplierName.size()] = count+" Total";
                                }catch (Exception ex)
                                {

                                }
                                try
                                {
                                    Amount[amount.size()] = total+"";

                                    remainAmount.setText("Total Amount : "+total);
                                }catch (Exception ex)
                                {

                                }

                                historyPayListAdapter historyPayList = new historyPayListAdapter(historySupplier.this,SupplierName,Date,Amount);
                                listView.setAdapter(historyPayList);

                                MyProgressBar.HideProgress();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                MyProgressBar.HideProgress();
                            }
                        });



                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
            DatabaseReference mOnHoldSupplier = mRootRef.child(userIdMainStatic+"/payToSupplier");
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
                supplierName.clear();
                date.clear();
                amount.clear();
                MyProgressBar.ShowProgress(this);
                for (int i = 0; i < Dates.size(); i++)
                {
                    final int iFinal = i;
                    Query query = mOnHoldSupplier.orderByChild("date").equalTo(betweenDates[i]);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                            {
                                supplierName.add(dataSnapshot1.child("name").getValue(String.class));
                                date.add(dataSnapshot1.child("date").getValue(String.class));
                                amount.add(dataSnapshot1.child("money").getValue(String.class));
                            }
                            final String SupplierName[] = new String[supplierName.size()+1];
                            String Date[] = new String[date.size()+1];
                            String Amount[] = new String[amount.size()+1];

                            double total = 0;
                            int count = 0;
                            for (int i = 0; i < supplierName.size(); i++)
                            {
                                SupplierName[i] = supplierName.get(i);
                                Date[i] = date.get(i);
                                Amount[i] = amount.get(i);
                                count++;
                                try
                                {
                                    total += Double.parseDouble(Amount[i]);
                                }catch (Exception es)
                                {

                                }
                            }

                            SupplierName[supplierName.size()] = count+" Total";
                            Amount[amount.size()] = total+"";
                            remainAmount.setText("Total Amount : "+total);
                            historyPayListAdapter historyPayList = new historyPayListAdapter(historySupplier.this,SupplierName,Date,Amount);
                            listView.setAdapter(historyPayList);

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
            DatabaseReference mOnHoldSupplier = mRootRef.child(userIdMainStatic+"/payToSupplier");
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
                supplierName.clear();
                date.clear();
                amount.clear();
                MyProgressBar.ShowProgress(this);
                for (int i = 0; i < Dates.size(); i++)
                {
                    final int iFinal = i;

                    Query query = mOnHoldSupplier.orderByChild("dateName").equalTo(betweenDates[i]+"_"+searchProduct);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                            {
                                supplierName.add(dataSnapshot1.child("name").getValue(String.class));
                                date.add(dataSnapshot1.child("date").getValue(String.class));
                                amount.add(dataSnapshot1.child("money").getValue(String.class));
                            }
                            final String SupplierName[] = new String[supplierName.size()+1];
                            String Date[] = new String[date.size()+1];
                            String Amount[] = new String[amount.size()+1];

                            double total = 0;
                            int count = 0;
                            for (int i = 0; i < supplierName.size(); i++)
                            {
                                SupplierName[i] = supplierName.get(i);
                                Date[i] = date.get(i);
                                Amount[i] = amount.get(i);
                                count++;
                                try
                                {
                                    total += Double.parseDouble(Amount[i]);
                                }catch (Exception es)
                                {

                                }
                            }

                            SupplierName[supplierName.size()] = count+" Total";
                            Amount[amount.size()] = total+"";
                            remainAmount.setText("Total Amount : "+total);
                            historyPayListAdapter historyPayList = new historyPayListAdapter(historySupplier.this,SupplierName,Date,Amount);
                            listView.setAdapter(historyPayList);

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
    public void onPause() {
        super.onPause();

        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        NetworkConnectivityCheck.connectionCheck(this);

    }

}
