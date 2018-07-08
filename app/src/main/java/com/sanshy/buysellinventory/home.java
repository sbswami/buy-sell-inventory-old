package com.sanshy.buysellinventory;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static com.sanshy.buysellinventory.MyUserStaticClass.getUserIdMainStatic;
import static com.sanshy.buysellinventory.MyUserStaticClass.isPaid;
import static com.sanshy.buysellinventory.MyUserStaticClass.loadAds;
import static com.sanshy.buysellinventory.MyUserStaticClass.paid;
import static com.sanshy.buysellinventory.MyUserStaticClass.setPaid;
import static com.sanshy.buysellinventory.MyUserStaticClass.showAds;
import static com.sanshy.buysellinventory.MyUserStaticClass.userIdMainStatic;
import static com.sanshy.buysellinventory.NetworkConnectivityCheck.connectionCheck;


public class home extends AppCompatActivity{

    private static final String TAG = "FCM";
    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAuth mAuth;
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    TextView profile;
    AdView adView1,adView2;

    String UserIdFromAdmin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        try{
            Intent intent = getIntent();
            UserIdFromAdmin = intent.getStringExtra("uid");
            if (UserIdFromAdmin!=null){
                MyUserStaticClass.setUserIdMainStatic(UserIdFromAdmin);
            }
        }catch (Exception ex){

        }

        adView1 = findViewById(R.id.adView);
        adView2 = findViewById(R.id.adView2);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId  = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }

        // If a notification message is tapped, any data accompanying the notification
        // message is available in the intent extras. In this sample the launcher
        // intent is fired when the notification is tapped, so any accompanying data would
        // be handled here. If you want a different intent fired, set the click_action
        // field of the notification message to the desired intent. The launcher intent
        // is used when no click_action is specified.
        //
        // Handle possible data accompanying notification message.
        // [START handle_data_extras]
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }
        // [END handle_data_extras]



        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mAuth = FirebaseAuth.getInstance();
        profile = findViewById(R.id.profile);
        connectionCheck(this);

        myAds();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (!isPaid()){
            showAds();
        }

    }

    private void myAds() {
        if (!isPaid()){
            adView1.loadAd(new AdRequest.Builder().build());
            adView2.loadAd(new AdRequest.Builder().build());
            loadAds(this);
        }else{
            adView1.setVisibility(View.GONE);
            adView2.setVisibility(View.GONE);
        }

    }

   public void rateUs(View view){
       Intent intent = new Intent(Intent.ACTION_VIEW);
       intent.setData(Uri.parse("market://details?id=com.sanshy.buysellinventory"));
       startActivity(intent);
   }
   public void feedback(View v){
       startActivity(new Intent(this,help.class));

//       Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
//       emailIntent.setData(Uri.parse("mailto: sbswami24@gmail.com"));
//       startActivity(Intent.createChooser(emailIntent, "Send feedback"));
   }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    protected void onStart() {
        super.onStart();
        final DatabaseReference rate = mRootRef.child(userIdMainStatic+"/rate");
        rate.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    boolean rated = dataSnapshot.getValue(Boolean.class);
                    if (!rated){
                        AlertDialog.Builder builder = new AlertDialog.Builder(home.this);

                        builder.setTitle(getString(R.string.rate_us_text))
                                .setMessage(getString(R.string.rate_us_request_dialog_))
                                .setPositiveButton(getString(R.string.rate_us_text), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                        intent.setData(Uri.parse("market://details?id=com.sanshy.buysellinventory"));
                                        startActivity(intent);
                                        rate.setValue(true);
                                    }
                                })
                                .setNegativeButton(getString(R.string.not_now_),null);

                        builder.create().show();
                    }
                }else{
                    rate.setValue(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null)
        {
            startActivity(new Intent(this,MainActivity.class));
            this.finish();
        }
        else
        {
            final DatabaseReference allInfoRef = mRootRef.child("AdminWork/PaidUser");
            allInfoRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ArrayList<String> paidUserList = new ArrayList<>();
                    if (dataSnapshot.exists()){
                        paidUserList = (ArrayList<String>) dataSnapshot.getValue();
                        for (String userId : paidUserList){
                            if (userId.equals(getUserIdMainStatic())){
                                paid=true;
                                MyDialogBox.ShowDialog(home.this,"herre");
                                myAds();
                                break;
                            }
                        }
                    }
                    else {
                        paidUserList.add(getUserIdMainStatic());
                        allInfoRef.setValue(paidUserList);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            mRootRef.child("update").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try{
                        int playVersion = dataSnapshot.getValue(Integer.class);


                    try {
                        PackageInfo pInfo = home.this.getPackageManager().getPackageInfo(getPackageName(), 0);
                        String version = pInfo.versionName;
                        int code = pInfo.versionCode;
                        if (playVersion>code)
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(home.this);
                            builder.setTitle(R.string.update)
                                    .setMessage(getString(R.string.wow_new_version_))
                                    .setPositiveButton(getString(R.string.update_now_), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent = new Intent(Intent.ACTION_VIEW);
                                            intent.setData(Uri.parse("market://details?id=com.sanshy.buysellinventory"));
                                            startActivity(intent);
                                        }
                                    })
                                    .create().show();
                        }

                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    }catch (Exception ex){}
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        try {
            String name = currentUser.getDisplayName();
            if (name.isEmpty())
            {
                profile.setText(R.string.click_to_complete);
            }
            else {
                profile.setText(getString(R.string.welcome)+name);
            }
        }catch (Exception ex){}
    }

    public void Profile(View view){
        startActivity(new Intent(this,Main2Activity.class));
    }

    public void product(View view){
        connectionCheck(this);startActivity(new Intent(this,Product.class));
    }
    public void customer(View view)    {
        connectionCheck(this);startActivity(new Intent(this,Customer.class));
    }
    public void supplier(View view)    {
        connectionCheck(this);startActivity(new Intent(this,Supplier.class));
    }
    public void buy(View view)    {
        connectionCheck(this);startActivity(new Intent(this,Buy.class));
    }
    public void sell(View view)    {
        connectionCheck(this);startActivity(new Intent(this,Sell.class));
    }
    public void statement(View view)    {        connectionCheck(this);startActivity(new Intent(this, Statement.class));
    }
    public void stock(View view)   {
        connectionCheck(this);startActivity(new Intent(this,Stock.class));
    }
    public void holders(View view)    {
        connectionCheck(this);startActivity(new Intent(this,Payment.class));
    }
    public void exp(View view)    {
        connectionCheck(this);startActivity(new Intent(this,Expenditure.class));
    }
    public void share(View view){

        String shareText = getString(R.string.buy_sell_name)+"\n" +
                getString(R.string.share_line_one)+"\n" +
                getString(R.string.share_line_two)+"\n" +
                getString(R.string.share_line_three)+
                "https://play.google.com/store/apps/details?id=com.sanshy.buysellinventory";
        String shareSubject =getString(R.string.share_subject);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,shareText);
        intent.putExtra(Intent.EXTRA_SUBJECT,shareSubject);
        startActivity(Intent.createChooser(intent, getString(R.string.share_app_via)));

    }

    public void logout(View view){
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        home.this.finish();
                        startActivity(new Intent(home.this,MainActivity.class));
                    }
                });
    }

    public void help(View view){

        // Get token
        String token = FirebaseInstanceId.getInstance().getToken();

        // Log and toast
        String msg = getString(R.string.msg_token_fmt, token);
        Log.d(TAG, msg);

        startActivity(new Intent(this,help.class));
    }
    public void SideBusiness(View view){
        startActivity(new Intent(this,SideBusinessWork.class));
    }

    public void resetB(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.warning)
                .setMessage(getString(R.string.warning_line_english))
                .setPositiveButton(getString(R.string.cancel_text),null)
                .setNegativeButton(getString(R.string.reset_text), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final AlertDialog.Builder builder1 = new AlertDialog.Builder(home.this);
                        LayoutInflater inflater = home.this.getLayoutInflater();
                        final View myD = inflater.inflate(R.layout.reset_q, null);
                        builder1.setView(myD)
                                .setPositiveButton(getString(R.string.cancel_text),null)
                                .setNegativeButton(R.string.reset_text, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        EditText ans = myD.findViewById(R.id.ans);
                                        String Ans = ans.getText().toString();
                                        if (Ans.isEmpty())
                                        {
                                            ans.setError(getString(R.string.solve_it));
                                        }
                                        else{
                                            try{
                                                double sol = Double.parseDouble(Ans);
                                                if (sol == 109)
                                                {
                                                    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
                                                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                                                    FirebaseUser user = mAuth.getCurrentUser();

                                                    DatabaseReference mUserRef = mRootRef.child(userIdMainStatic);
                                                    mUserRef.removeValue();

                                                    Toast.makeText(home.this, R.string.app_reset_done, Toast.LENGTH_SHORT).show();

                                                }
                                                else {
                                                    dialogInterface.cancel();
                                                }
                                            }catch (Exception ex){
                                                Toast.makeText(home.this, getString(R.string.feedback_developer_with)+ex, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                })
                                .create().show();

                    }
                })
                .create().show();
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        connectionCheck(this);

    }

}
