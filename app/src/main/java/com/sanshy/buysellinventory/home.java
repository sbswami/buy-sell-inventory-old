package com.sanshy.buysellinventory;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import co.mobiwise.materialintro.shape.Focus;
import co.mobiwise.materialintro.shape.FocusGravity;
import co.mobiwise.materialintro.shape.ShapeType;
import co.mobiwise.materialintro.view.MaterialIntroView;

import static com.sanshy.buysellinventory.MyUserStaticClass.getUserIdMainStatic;
import static com.sanshy.buysellinventory.MyUserStaticClass.isPaid;
import static com.sanshy.buysellinventory.MyUserStaticClass.loadAds;
import static com.sanshy.buysellinventory.MyUserStaticClass.paid;
import static com.sanshy.buysellinventory.MyUserStaticClass.setPaid;
import static com.sanshy.buysellinventory.MyUserStaticClass.sharedPref;
import static com.sanshy.buysellinventory.MyUserStaticClass.showAds;
import static com.sanshy.buysellinventory.MyUserStaticClass.showCount;
import static com.sanshy.buysellinventory.MyUserStaticClass.userIdMainStatic;
import static com.sanshy.buysellinventory.NetworkConnectivityCheck.connectionCheck;
import static com.sanshy.buysellinventory.TourGuide.guide;


public class home extends AppCompatActivity implements PurchasesUpdatedListener{

    private static final String TAG = "FCM";
    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    TextView profile;
    AdView adView1,adView2;
    // create new Person
    private BillingClient mBillingClient;

    String UserIdFromAdmin;

    LinearLayout productB,supplierB,customerB,buyB,sellB,reportB,stockB,holdersB,expenditureB,sideBusinessB,shareB,rateB,helpB,feedbackB,resetBt,logOutB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        productB = findViewById(R.id.product_b);
        supplierB = findViewById(R.id.supplier_b);
        customerB = findViewById(R.id.customer_b);
        buyB  = findViewById(R.id.buy_b);
        sellB = findViewById(R.id.sell_b);
        reportB = findViewById(R.id.report_b);
        stockB = findViewById(R.id.stock_b);
        holdersB = findViewById(R.id.holders_b);
        expenditureB = findViewById(R.id.exp_b);
        sideBusinessB = findViewById(R.id.side_business_b);
        shareB = findViewById(R.id.share_b);
        rateB = findViewById(R.id.rate_us_b);
        helpB = findViewById(R.id.help_b);
        feedbackB = findViewById(R.id.feedback_b);
        resetBt = findViewById(R.id.reset_b);
        logOutB  = findViewById(R.id.logout_b);



        mBillingClient = BillingClient.newBuilder(this).setListener(this).build();
        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@BillingClient.BillingResponse int billingResponseCode) {
                if (billingResponseCode == BillingClient.BillingResponse.OK) {
                    // The billing client is ready. You can query purchases here.
                    List skuList = new ArrayList<> ();
                    skuList.add("buy_sell_test");
                    SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
                    params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
                    mBillingClient.querySkuDetailsAsync(params.build(),
                            new SkuDetailsResponseListener() {
                                @Override
                                public void onSkuDetailsResponse(int responseCode, List<SkuDetails> skuDetailsList) {
                                    // Process the result.

                                    if (responseCode == BillingClient.BillingResponse.OK
                                            && skuDetailsList != null) {
                                        for (SkuDetails skuDetails : skuDetailsList) {
                                            String sku = skuDetails.getSku();
                                            String price = skuDetails.getPrice();
                                            if ("buy_sell_test".equals(sku)) {
                                                Toast.makeText(home.this, "Connected", Toast.LENGTH_SHORT).show();
                                                skuId = "buy_sell_test";
                                            } else{

                                            }
                                        }
                                    }

                                }
                            });

                }
            }
            @Override
            public void onBillingServiceDisconnected() {

                Toast.makeText(home.this, getString(R.string.payment_still_remaining_), Toast.LENGTH_SHORT).show();

                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        });

        sharedPref = this.getSharedPreferences(getString(R.string.tour_show_time), Context.MODE_PRIVATE);
        showCount = sharedPref.getInt(getString(R.string.show_key), showCount);
        if (showCount==1){
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt(getString(R.string.show_key), 1);
            editor.apply();
        }

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
        profile = findViewById(R.id.profile);
        connectionCheck(this);

    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    private void myAds() {
        try{
            if (!isPaid()){
                adView1.loadAd(new AdRequest.Builder().build());
                adView2.loadAd(new AdRequest.Builder().build());
            }else{
                adView1.setVisibility(View.GONE);
                adView2.setVisibility(View.GONE);
            }

        }catch (Exception ex){}
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

    FirebaseUser currentUser = mAuth.getCurrentUser();

    @Override
    protected void onStart() {
        super.onStart();

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

            final DatabaseReference createDate = mRootRef.child(userIdMainStatic+"/CreateDate");
            createDate.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Date date = new Date();
                    String Date = dateFormat.format(date);

                    if (!(dataSnapshot.exists())){
                        createDate.setValue(Date);
                        paid = true;
                    }else{
                        String creDate = dataSnapshot.getValue(String.class);
                        if (creDate.equals(Date)){
                            paid = true;
                        }
                    }

                    myAds();
                    int homeBtCount = 1;
                    homeBtCount = sharedPref.getInt(getString(R.string.home_count), homeBtCount);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt(getString(R.string.home_count), 1+homeBtCount);
                    editor.apply();

                    switch (homeBtCount){
                        case 1 : guide(home.this,"HomeProduct",getString(R.string.home_product_tour_guide),productB,showCount);
                            break;
                        case 2 : guide(home.this,"HomeSupplier",getString(R.string.home_supplier_tour_guide),supplierB,showCount);
                            break;
                        case 3 : guide(home.this,"HomeBuy",getString(R.string.home_buy_tour_guide),buyB,showCount);
                            break;
                        case 4 : guide(home.this,"HomeSell",getString(R.string.home_sell_tour_guide),sellB,showCount);
                            break;
                        case 5 : guide(home.this,"HomeReport",getString(R.string.home_report_tour_guide),reportB,showCount);
                            break;
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
                                .setNegativeButton(getString(R.string.not_now_),null)
                                .setNeutralButton(getString(R.string.never_), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        rate.setValue(true);
                                    }
                                });

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


    public void payMoney(View view){


    }

    String skuId;
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

        BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                .setSku(skuId)
                .setType(BillingClient.SkuType.INAPP) // SkuType.SUB for subscription
                .build();
        int responseCode = mBillingClient.launchBillingFlow(this,flowParams);



//        // Get token
//        String token = FirebaseInstanceId.getInstance().getToken();
//
//        // Log and toast
//        String msg = getString(R.string.msg_token_fmt, token);
//        Log.d(TAG, msg);
//
//        startActivity(new Intent(this,help.class));
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


    @Override
    public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {
        if (responseCode == BillingClient.BillingResponse.OK
                && purchases != null) {

            MyProgressBar.ShowProgress(this);
            final DatabaseReference allInfoRef = mRootRef.child("AdminWork/PaidUser");
            allInfoRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ArrayList<String> paidUserList = new ArrayList<>();
                    if (dataSnapshot.exists()){
                        paidUserList = (ArrayList<String>) dataSnapshot.getValue();
                        paidUserList.add(getUserIdMainStatic());
                        allInfoRef.setValue(paidUserList);
                    }
                    else {
                        paidUserList.add(getUserIdMainStatic());
                        allInfoRef.setValue(paidUserList);
                    }
                    MyProgressBar.HideProgress();
                    MyDialogBox.ShowDialog(home.this,getString(R.string.payment_done));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            for (Purchase purchase : purchases) {

            }
        } else if (responseCode == BillingClient.BillingResponse.USER_CANCELED) {

            Toast.makeText(this, getString(R.string.payment_still_remaining_), Toast.LENGTH_SHORT).show();

            // Handle an error caused by a user cancelling the purchase flow.
        } else {

            

            // Handle any other error codes.
        }
    }
}
