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
import android.widget.ProgressBar;
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
import static com.sanshy.buysellinventory.MyUserStaticClass.userIdMainStatic;

public class UndoExp extends AppCompatActivity {

    AutoCompleteTextView suggestion_box4;

    ListView listView;
    ProgressBar progressBar;

    TextView remainAmount;

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();

    ArrayList<String> Remark = new ArrayList<>();
    ArrayList<String> date = new ArrayList<>();
    ArrayList<String> money = new ArrayList<>();
    ArrayList<String> Eid = new ArrayList<>();
    private int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_undo_exp);

        suggestion_box4 = findViewById(R.id.suggestion_box4);
        listView = findViewById(R.id.listView);
        progressBar = findViewById(R.id.progressBar);
        remainAmount = findViewById(R.id.remainAmount);
//        AdView adView1;
//        adView1 = findViewById(R.id.adView);
//
//        adView1.loadAd(new AdRequest.Builder().build());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try
                {
                    String RemarkS = Remark.get(i);
                    final String DateS = date.get(i);
                    final String MoneyS = money.get(i);
                    final String EidS = Eid.get(i);

                    AlertDialog.Builder builder = new AlertDialog.Builder(UndoExp.this);
                    builder.setTitle("Exp. Details")
                            .setMessage("Remark : "+RemarkS+"\n"+
                                    "Date : "+DateS+"\n"+
                                    "Money : "+MoneyS)
                            .setPositiveButton("Undo!", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    MyProgressBar.ShowProgress(UndoExp.this);
tempo.clear();
                                    DatabaseReference mExp = mRootRef.child(userIdMainStatic+"/Expenditure/"+EidS);
                                    mExp.removeValue();

                                    final DatabaseReference mStatementInventory = mRootRef.child(userIdMainStatic+"/Statement/Inventory/"+DateS);
                                    mStatementInventory.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            double expMoney = Double.parseDouble(MoneyS);
                                            if (dataSnapshot.exists()){

                                                String tExpCloud = (String) dataSnapshot.child(TOTAL_EXPENDITURE).getValue();
                                                String netProfitCloud = (String) dataSnapshot.child(NET_PROFIT).getValue();

                                                double tExp = Double.parseDouble(tExpCloud);
                                                double netPro = Double.parseDouble(netProfitCloud);

                                                double nowExp = tExp - expMoney;
                                                double nowNetPro = netPro + expMoney;

                                                String saveExp = String.valueOf(nowExp);
                                                String saveNetPro = String.valueOf(nowNetPro);

                                                mStatementInventory.child(TOTAL_EXPENDITURE).setValue(saveExp);
                                                mStatementInventory.child(NET_PROFIT).setValue(saveNetPro);


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
                            }).setNeutralButton("Cancel",null);
                    builder.create().show();
                }catch (Exception ex){}

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference mExp = mRootRef.child(userIdMainStatic+"/Expenditure");

        Query query = mExp.limitToLast(50);
        MyProgressBar.ShowProgress(this);
tempo.clear();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Remark.clear();
                date.clear();
                money.clear();
                Eid.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    Remark.add(dataSnapshot1.child("remark").getValue(String.class));
                    date.add(dataSnapshot1.child("date").getValue(String.class));
                    money.add(dataSnapshot1.child("money").getValue(String.class));
                    Eid.add(dataSnapshot1.child("id").getValue(String.class));
                }
                final String remarkList[] = new String[Remark.size()+1];
                String Date[] = new String[date.size()+1];
                String Amount[] = new String[money.size()+1];

                double total = 0;
                int count = 0;
                for (int i = 0; i < Remark.size(); i++)
                {
                    remarkList[i] = Remark.get(i);
                    Date[i] = date.get(i);
                    Amount[i] = money.get(i);
                    count++;
                    try
                    {
                        total += Double.parseDouble(Amount[i]);
                    }catch (Exception es)
                    {

                    }
                }

                remarkList[Remark.size()] = count+" Total";
                Amount[money.size()] = total+"";

                historyPayListAdapter historyPayList = new historyPayListAdapter(UndoExp.this,Date,remarkList,Amount);
                listView.setAdapter(historyPayList);

                remainAmount.setText("Total Money : "+total);

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
        DatabaseReference mRemarkRef = mRootRef.child(userIdMainStatic+"/remark");
        mRemarkRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    cList.add(dataSnapshot1.child("hint").getValue(String.class));
                }
                String list[] = new String[cList.size()];
                for (int i = 0; i < cList.size() ; i++)
                {
                    list[i] = cList.get(i);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(UndoExp.this,android.R.layout.simple_spinner_dropdown_item, Arrays.asList(list));

                suggestion_box4.setAdapter(adapter);
                suggestion_box4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        progressBar.setVisibility(View.VISIBLE);
                        progressBar.setFocusableInTouchMode(true);
                        DatabaseReference mSearchRef = mRootRef.child(userIdMainStatic+"/Expenditure");

                        Query query = mSearchRef.orderByChild("remark").equalTo(suggestion_box4.getText().toString());
                        MyProgressBar.ShowProgress(UndoExp.this);
tempo.clear();
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Remark.clear();
                                date.clear();
                                money.clear();
                                Eid.clear();
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                                {
                                    Remark.add(dataSnapshot1.child("remark").getValue(String.class));
                                    date.add(dataSnapshot1.child("date").getValue(String.class));
                                    money.add(dataSnapshot1.child("money").getValue(String.class));
                                    Eid.add(dataSnapshot1.child("id").getValue(String.class));
                                }
                                String RemarkList[] = new String[Remark.size()+1];
                                String Date[] = new String[date.size()+1];
                                String Amount[] = new String[money.size()+1];

                                double total = 0;
                                int count = 0;
                                for (int i = 0; i < Remark.size(); i++)
                                {
                                    RemarkList[i] = Remark.get(i);
                                    Date[i] = date.get(i);
                                    Amount[i] = money.get(i);
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
                                    RemarkList[Remark.size()] = count+" Total";
                                }catch (Exception ex)
                                {

                                }
                                try
                                {
                                    Amount[money.size()] = total+"";
                                }catch (Exception ex)
                                {

                                }

                                historyPayListAdapter historyPayList = new historyPayListAdapter(UndoExp.this,Date,RemarkList,Amount);
                                listView.setAdapter(historyPayList);

                                remainAmount.setText("Total Money : "+total);
                                progressBar.setVisibility(View.INVISIBLE);

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
    public void from(View view){
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
    public void to(View view){
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
        progressBar.setVisibility(View.VISIBLE);
        String searchProduct = suggestion_box4.getText().toString();
        if (searchProduct.isEmpty())
        {
            if ((fday == 0) && (fmonth == 0) && (fYear == 0) && (tday == 0) && (tmonth == 0) && (tYear == 0))
            {
                progressBar.setVisibility(View.INVISIBLE);
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                builder.setTitle("Choose Date")
                        .setMessage("Please Choose Any Date")
                        .setPositiveButton("OK",null)
                        .create()
                        .show();
                return;
            }
            DatabaseReference mExpRef = mRootRef.child(userIdMainStatic+"/Expenditure");
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
                Remark.clear();
                date.clear();
                money.clear();
                Eid.clear();
                MyProgressBar.ShowProgress(this);
tempo.clear();
                for (int i = 0; i < Dates.size(); i++)
                {
                    final int iFinal = i;
                    Query query = mExpRef.orderByChild("date").equalTo(betweenDates[i]);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                            {
                                Remark.add(dataSnapshot1.child("remark").getValue(String.class));
                                date.add(dataSnapshot1.child("date").getValue(String.class));
                                money.add(dataSnapshot1.child("money").getValue(String.class));
                                Eid.add(dataSnapshot1.child("id").getValue(String.class));
                            }
                            final String remarkList[] = new String[Remark.size()+1];
                            String Date[] = new String[date.size()+1];
                            String Amount[] = new String[money.size()+1];

                            double total = 0;
                            int count = 0;
                            for (int i = 0; i < Remark.size(); i++)
                            {
                                remarkList[i] = Remark.get(i);
                                Date[i] = date.get(i);
                                Amount[i] = money.get(i);
                                count++;
                                try
                                {
                                    total += Double.parseDouble(Amount[i]);
                                }catch (Exception es)
                                {

                                }
                            }

                            remarkList[Remark.size()] = count+" Total";
                            Amount[money.size()] = total+"";

                            historyPayListAdapter historyPayList = new historyPayListAdapter(UndoExp.this,Date,remarkList,Amount);
                            listView.setAdapter(historyPayList);

                            remainAmount.setText("Total Money : "+total);

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
                progressBar.setVisibility(View.INVISIBLE);
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                builder.setTitle("Choose Date")
                        .setMessage("Please Choose Any Date")
                        .setPositiveButton("OK",null)
                        .create()
                        .show();
                return;
            }
            DatabaseReference mExpRef = mRootRef.child(userIdMainStatic+"/Expenditure");
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
                Remark.clear();
                date.clear();
                money.clear();
                Eid.clear();
                MyProgressBar.ShowProgress(this);
tempo.clear();
                for (int i = 0; i < Dates.size(); i++)
                {
                    final int iFinal = i;
                    Query query = mExpRef.orderByChild("date_remark").equalTo(betweenDates[i]+"_"+searchProduct);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                            {
                                Remark.add(dataSnapshot1.child("remark").getValue(String.class));
                                date.add(dataSnapshot1.child("date").getValue(String.class));
                                money.add(dataSnapshot1.child("money").getValue(String.class));
                                Eid.add(dataSnapshot1.child("id").getValue(String.class));
                            }
                            final String remarkList[] = new String[Remark.size()+1];
                            String Date[] = new String[date.size()+1];
                            String Amount[] = new String[money.size()+1];

                            double total = 0;
                            int count = 0;
                            for (int i = 0; i < Remark.size(); i++)
                            {
                                remarkList[i] = Remark.get(i);
                                Date[i] = date.get(i);
                                Amount[i] = money.get(i);
                                count++;
                                try
                                {
                                    total += Double.parseDouble(Amount[i]);
                                }catch (Exception es)
                                {

                                }
                            }

                            remarkList[Remark.size()] = count+" Total";
                            Amount[money.size()] = total+"";

                            historyPayListAdapter historyPayList = new historyPayListAdapter(UndoExp.this,Date,remarkList,Amount);
                            listView.setAdapter(historyPayList);

                            remainAmount.setText("Total Money : "+total);

                            if (iFinal==(Dates.size()-1)){
                                MyProgressBar.HideProgress();
tempo.add(1);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }
            catch (Exception ex)
            {

            }

        }

        progressBar.setVisibility(View.INVISIBLE);
    }
    ArrayList<Integer> tempo = new ArrayList<>();
    @Override
    protected void onPause() {
        super.onPause();

        if (tempo.size()==0){
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }



    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        progressBar.setVisibility(View.INVISIBLE);

        NetworkConnectivityCheck.connectionCheck(this);
    }

}
