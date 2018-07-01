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

import static com.sanshy.buysellinventory.MyDialogBox.DateRequestDialog;
import static com.sanshy.buysellinventory.MyUserStaticClass.isPaid;

public class Statement extends AppCompatActivity {

    ListView listView;

    private int mYear, mMonth, mDay, mHour, mMinute;

    InterstitialAd mInterstitialAd;

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    AdView adView1,adView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statement);

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
        }


    public void todaySt(View view)
    {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date1 = new Date();
        String date = dateFormat.format(date1);

        String betweenDates[] = {date};

        Intent intent = new Intent(this,StatementSearch.class);
        intent.putExtra("dates",betweenDates);
        startActivity(intent);
    }
    public void yesterdaySt(View view)
    {
        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        cal.add(Calendar.DATE, -1);
        String date = dateFormat.format(cal.getTime());

        String betweenDates[] = {date};

        Intent intent = new Intent(this,StatementSearch.class);
        intent.putExtra("dates",betweenDates);
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
            DateRequestDialog(this);
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
