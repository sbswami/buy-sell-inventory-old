package com.sanshy.buysellinventory;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Statement extends AppCompatActivity {

    ListView listView;

    private int mYear, mMonth, mDay, mHour, mMinute;

    InterstitialAd mInterstitialAd;

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statement);

        AdView adView1,adView2;
        adView1 = findViewById(R.id.adView);
        adView2 = findViewById(R.id.adView2);

        adView1.loadAd(new AdRequest.Builder().build());
        adView2.loadAd(new AdRequest.Builder().build());


//        mInterstitialAd = new InterstitialAd(this);
//        mInterstitialAd.setAdUnitId("ca-app-pub-2563796576069532/7625145053");
//        mInterstitialAd.loadAd(new AdRequest.Builder().build());
//
//        mInterstitialAd.setAdListener(new AdListener(){
//            @Override
//            public void onAdLoaded() {
//                super.onAdLoaded();
//                mInterstitialAd.show();
//
//            }
//        });


    }

        @Override
        protected void onStart() {
            super.onStart();
/*
            final DatabaseReference mSellRef = mRootRef.child(user.getUid()+"/sell");
            final DatabaseReference mBuyRef = mRootRef.child(user.getUid()+"/buy");
            final DatabaseReference mExpRef = mRootRef.child(user.getUid()+"/Expenditure");

            final ArrayList<String> sellDateList = new ArrayList<>();
            final ArrayList<String> buyDateList = new ArrayList<>();
            final ArrayList<String> expDateList = new ArrayList<>();

            final ArrayList<bitem> buyList = new ArrayList<>();
            final ArrayList<sellitem> sellList = new ArrayList<>();
            final ArrayList<String> expMoneyList = new ArrayList<>();

            final ArrayList<String> Date = new ArrayList<>();

            final Set<String> hs = new HashSet<>();

            mSellRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    sellDateList.clear();
                    sellList.clear();
                    hs.clear();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                    {
                        sellDateList.add(dataSnapshot1.child("date").getValue(String.class));
                    }
                    hs.addAll(sellDateList);
                    sellDateList.clear();
                    sellDateList.addAll(hs);
                    hs.clear();

                    mBuyRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            buyDateList.clear();
                            buyList.clear();
                            hs.clear();
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                            {
                               buyDateList.add(dataSnapshot1.child("date").getValue(String.class));
                            }
                            hs.addAll(buyDateList);
                            buyDateList.clear();
                            buyDateList.addAll(hs);
                            hs.clear();

                            mExpRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    expDateList.clear();
                                    expMoneyList.clear();
                                    hs.clear();
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                                    {
                                        expDateList.add(dataSnapshot1.child("date").getValue(String.class));
                                    }
                                    hs.addAll(expDateList);
                                    expDateList.clear();
                                    expDateList.addAll(hs);
                                    hs.clear();

                                    Date.addAll(expDateList);
                                    Date.addAll(buyDateList);
                                    Date.addAll(sellDateList);

                                    hs.addAll(Date);
                                    Date.clear();
                                    Date.addAll(hs);
                                    hs.clear();

                                    final String DateList[] = new String[Date.size()+1];
                                    final ArrayList<String> totalBuyList = new ArrayList<>();
                                    final ArrayList<String> totalSellList = new ArrayList<>();
                                    final ArrayList<String> totalExpList = new ArrayList<>();
                                    for (int i = 0; i < Date.size(); i++)
                                    {
                                        DateList[i] = Date.get(i);
                                        String dateValue = Date.get(i);

                                        Query buyQ = mBuyRef.orderByChild("date").equalTo(dateValue);
                                        final Query sellQ = mSellRef.orderByChild("date").equalTo(dateValue);
                                        final Query expQ = mExpRef.orderByChild("date").equalTo(dateValue);

                                        buyQ.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                double total = 0;
                                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                                                {
                                                    try
                                                    {
                                                        total+= Double.parseDouble(dataSnapshot1.child("price").getValue(String.class));
                                                    }catch (Exception e)
                                                    {

                                                    }
                                                }
                                                totalBuyList.add(total+"");

                                                sellQ.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        double total = 0;
                                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                                                        {
                                                            try
                                                            {
                                                                total+= Double.parseDouble(dataSnapshot1.child("price").getValue(String.class));
                                                            }catch (Exception e)
                                                            {

                                                            }
                                                        }
                                                        totalSellList.add(total+"");

                                                        expQ.addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                double total = 0;
                                                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                                                                {
                                                                    try
                                                                    {
                                                                        total+= Double.parseDouble(dataSnapshot1.child("money").getValue(String.class));
                                                                    }
                                                                    catch (Exception e)
                                                                    {

                                                                    }
                                                                }
                                                                totalExpList.add(total+"");
                                                                String tSellList[] = new String[Date.size()+1];
                                                                String tBuyList[] = new String[Date.size()+1];
                                                                String tExpList[] = new String[Date.size()+1];
                                                                String tGrossList[] = new String[Date.size()+1];
                                                                String tNetList[] = new String[Date.size()+1];
                                                                double grandSell = 0;
                                                                double grandBuy = 0;
                                                                double grandExp = 0;
                                                                double grandGross = 0;
                                                                double grandNet = 0;

                                                                for (int j = 0 ; j< Date.size(); j++)
                                                                {
                                                                    try
                                                                    {
                                                                        tBuyList[j] = totalBuyList.get(j);
                                                                        tSellList[j] = totalSellList.get(j);
                                                                        tExpList[j] = totalExpList.get(j);

                                                                        double gResult = Double.parseDouble(tBuyList[j]);

                                                                        grandBuy += Double.parseDouble(tBuyList[j]);
                                                                        grandSell += Double.parseDouble(tSellList[j]);
                                                                        grandExp += Double.parseDouble(tExpList[j]);
                                                                    }
                                                                    catch (Exception e)
                                                                    {

                                                                    }

                                                                }
                                                                try
                                                                {
                                                                    tBuyList[Date.size()] = grandBuy +"";
                                                                    tSellList[Date.size()] = grandSell + "";
                                                                    tExpList[Date.size()] = grandExp + "";
                                                                    DateList[Date.size()] = "Total";
                                                                }catch (Exception ex)
                                                                {

                                                                }

                                                                    statementAdapter adapter = new statementAdapter(Statement.this,DateList,tBuyList,tSellList,tSellList,tExpList,tSellList);
                                                                listView.setAdapter(adapter);
                                                                System.out.println(Date);
                                                                System.out.println(totalSellList);
                                                                System.out.println(totalBuyList);
                                                                System.out.println(totalExpList);
                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        });
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });

                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });



                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
 */
        }


    public void todaySt(View view)
    {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date1 = new Date();
        String date = dateFormat.format(date1);
        Intent intent = new Intent(this,StatementDay.class);
        intent.putExtra("date",date);
        startActivity(intent);
    }
    public void yesterdaySt(View view)
    {
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        cal.add(Calendar.DATE, -1);
        String date = dateFormat.format(cal.getTime());
        Intent intent = new Intent(this,StatementDay.class);
        intent.putExtra("date",date);
        startActivity(intent);
    }
    public void thisMonth(View view)
    {
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        System.out.println(df.format(cal.getTime()));
        ArrayList<String> Dates = new ArrayList<>();
        for (int i = 1; i < maxDay; i++)
        {
            cal.set(Calendar.DAY_OF_MONTH, i + 1);
            Dates.add(df.format(cal.getTime()));
        }
        String betweenDates[] = new String[Dates.size()];
        for (int j = 0; j<Dates.size(); j++)
        {
            betweenDates[j] = Dates.get(j);
        }
        Intent intent = new Intent(this,StatementSearch.class);
        intent.putExtra("dates",betweenDates);
        startActivity(intent);
    }
    public void lastMonth(View view)
    {
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH);
        cal.set(Calendar.MONTH, month-1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        System.out.println(df.format(cal.getTime()));
        ArrayList<String> Dates = new ArrayList<>();
        for (int i = 1; i < maxDay; i++)
        {
            cal.set(Calendar.DAY_OF_MONTH, i + 1);
            Dates.add(df.format(cal.getTime()));
        }
        String betweenDates[] = new String[Dates.size()];
        for (int j = 0; j<Dates.size(); j++)
        {
            betweenDates[j] = Dates.get(j);
        }
        Intent intent = new Intent(this,StatementSearch.class);
        intent.putExtra("dates",betweenDates);
        startActivity(intent);
    }
    public void thisYear(View view)
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);

        List<Date> dates = new ArrayList<>();
        String sDate1 = "01/01/"+year;
        String sDate2 = "31/12/"+year;
        DateFormat simple = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);
            Date date2 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate2);
            dates = dateInterval(date1, date2);
            ArrayList<String> Dates = new ArrayList<>();
            for (int i = 0; i < dates.size(); i++) {
                Dates.add(simple.format(dates.get(i)));
            }
            Dates.add(simple.format(date2));

            String betweenDates[] = new String[Dates.size()];
            for (int i = 0; i < Dates.size(); i++) {
                betweenDates[i] = Dates.get(i);
            }

            Intent intent = new Intent(this,StatementSearch.class);
            intent.putExtra("dates",betweenDates);
            startActivity(intent);
        }catch (Exception ex){}

        }
    public void lastYear(View view)
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        year--;

        List<Date> dates = new ArrayList<>();
        String sDate1 = "01/01/"+year;
        String sDate2 = "31/12/"+year;
        DateFormat simple = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);
            Date date2 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate2);
            dates = dateInterval(date1, date2);
            ArrayList<String> Dates = new ArrayList<>();
            for (int i = 0; i < dates.size(); i++) {
                Dates.add(simple.format(dates.get(i)));
            }
            Dates.add(simple.format(date2));

            String betweenDates[] = new String[Dates.size()];
            for (int i = 0; i < Dates.size(); i++) {
                betweenDates[i] = Dates.get(i);
            }

            Intent intent = new Intent(this,StatementSearch.class);
            intent.putExtra("dates",betweenDates);
            startActivity(intent);
        }catch (Exception ex){}
    }
    int fday = 0;
    int fmonth = 0;
    int fYear = 0;
    int tday = 0;
    int tmonth = 0;
    int tYear = 0;
    public void fromSt(View view)
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
                        System.out.println(fday+"/"+fmonth+"/"+fYear);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();


    }
    public void toSt(View view)
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
                        System.out.println(tday+"/"+tmonth+"/"+tYear);
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
    public void searchSt(View view)
    {
        if ((fday == 0) && (fmonth == 0) && (fYear == 0) && (tday == 0) && (tmonth == 0) && (tYear == 0))
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Choose Date")
                    .setMessage("Please Choose Any Date")
                    .setPositiveButton("OK",null)
                    .create()
                    .show();
            return;
        }
        List<Date> dates = new ArrayList<>();
        String sDate1 = fday+"/"+fmonth+"/"+fYear;
        String sDate2 = tday+"/"+tmonth+"/"+tYear;
        DateFormat simple = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);
            Date date2 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate2);
            dates = dateInterval(date1, date2);
            ArrayList<String> Dates = new ArrayList<>();
            for (int i = 0; i < dates.size(); i++) {
                Dates.add(simple.format(dates.get(i)));
            }
            Dates.add(simple.format(date2));

            String betweenDates[] = new String[Dates.size()];
            for (int i = 0; i < Dates.size(); i++) {
                betweenDates[i] = Dates.get(i);
            }


            Intent intent = new Intent(this,StatementSearch.class);
            intent.putExtra("dates",betweenDates);
            startActivity(intent);
        }
        catch (Exception ex)
        {

        }
        }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        NetworkConnectivityCheck.connectionCheck(this);

    }
}
