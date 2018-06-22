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


public class home extends AppCompatActivity implements RewardedVideoAdListener {

    private static final String TAG = "FCM";
    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAuth mAuth;
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    TextView profile;
    Button Share;
    InterstitialAd mInterstitialAd,mInterstitialAd2;

    private RewardedVideoAd mRewardedVideoAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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


        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        mAuth = FirebaseAuth.getInstance();

        profile = findViewById(R.id.profile);

        Share = findViewById(R.id.share);

        AdView adView1,adView2;
        adView1 = findViewById(R.id.adView);
        adView2 = findViewById(R.id.adView2);

        adView1.loadAd(new AdRequest.Builder().build());
        adView2.loadAd(new AdRequest.Builder().build());

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-2563796576069532/7110085832");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

//        mInterstitialAd2 = new InterstitialAd(this);
//        mInterstitialAd2.setAdUnitId("ca-app-pub-2563796576069532/5197404213");
//        mInterstitialAd2.loadAd(new AdRequest.Builder().build());
//
//        mInterstitialAd.setAdListener(new AdListener(){
//            @Override
//            public void onAdLoaded() {
//                mInterstitialAd.show();
//
//            }
//
//            @Override
//            public void onAdFailedToLoad(int i) {
//                super.onAdFailedToLoad(i);
//                switch (i)
//                {
//                    case ERROR_CODE_INTERNAL_ERROR :
//                        Toast.makeText(home.this, "Internal", Toast.LENGTH_SHORT).show();
//                        break;
//                    case ERROR_CODE_INVALID_REQUEST :
//                        Toast.makeText(home.this, "Invalid Request", Toast.LENGTH_SHORT).show();
//                        break;
//                    case ERROR_CODE_NETWORK_ERROR :
//                        Toast.makeText(home.this, "Network", Toast.LENGTH_SHORT).show();
//                        break;
//                    default:
//                        Toast.makeText(home.this, "No Fill", Toast.LENGTH_SHORT).show();
//                        break;
//                }
//            }
//        });

        isInternetOn();

        loadRewardedVideoAd();

    }
    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd("ca-app-pub-2563796576069532/4960065955",
                new AdRequest.Builder().build());
    }
   public void rateUs(View view){

       if (mRewardedVideoAd.isLoaded()) {
           mRewardedVideoAd.show();
       }

       Intent intent = new Intent(Intent.ACTION_VIEW);
       intent.setData(Uri.parse("market://details?id=com.sanshy.buysellinventory"));
       startActivity(intent);
   }
   public void feedback(View v){

       if (mRewardedVideoAd.isLoaded()) {
           mRewardedVideoAd.show();
       }
       Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
       emailIntent.setData(Uri.parse("mailto: sbswami24@gmail.com"));
       startActivity(Intent.createChooser(emailIntent, "Send feedback"));
   }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (mInterstitialAd2.isLoaded())
//        {
//            mInterstitialAd2.show();
//        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null)
        {
            startActivity(new Intent(this,MainActivity.class));
            this.finish();
        }
        else
        {

            mRootRef.child("update").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try{
                        int playVersion = dataSnapshot.getValue(Integer.class);


                    try {
                        PackageInfo pInfo = home.this.getPackageManager().getPackageInfo(getPackageName(), 0);
                        String version = pInfo.versionName;
                        int code = pInfo.versionCode;
                        if (playVersion>code)
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(home.this);
                            builder.setTitle("Update!!")
                                    .setMessage("Wow!! New Version Available!!")
                                    .setPositiveButton("Update Now!!", new DialogInterface.OnClickListener() {
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
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        try {
            String name = currentUser.getDisplayName();
            if (name.isEmpty())
            {
                profile.setText("Click To Complete Profile!!!");
            }
            else {
                profile.setText("Welcome!! "+name);
            }
        }catch (Exception ex){}
    }

    public void Profile(View view){
        startActivity(new Intent(this,Main2Activity.class));
    }

    public void product(View view){
        connectionCheck();startActivity(new Intent(this,Product.class));
    }
    public void customer(View view)    {
        connectionCheck();startActivity(new Intent(this,Customer.class));
    }
    public void supplier(View view)    {
        connectionCheck();startActivity(new Intent(this,Supplier.class));
    }
    public void buy(View view)    {
        connectionCheck();startActivity(new Intent(this,Buy.class));
    }
    public void sell(View view)    {
        connectionCheck();startActivity(new Intent(this,Sell.class));
    }
    public void statement(View view)    {        connectionCheck();startActivity(new Intent(this, Statement.class));
    }
    public void stock(View view)   {
        connectionCheck();startActivity(new Intent(this,Stock.class));
    }
    public void payment(View view)    {
        connectionCheck();startActivity(new Intent(this,Payment.class));
    }
    public void exp(View view)    {
        connectionCheck();startActivity(new Intent(this,Expenditure.class));
    }
    public void share(View view){
        String shareText = "Buy Sell Inventory Application Online Data Store.\n" +
                "A to Z Work of Shop. Like Supplier Detail, Stock, On Hold Payments Detail, Expenditure System etc. \n" +
                "All Inventory Work In Simple Way.\n" +
                "Download Now!! Click Below!!" +
                "दुकान का पूरा हिसाब, बही खाते का पूरा हिसाब, घरेलु एंव अन्य खर्च का हिसाब और भी अन्य बहुत सारी विशेषताएं आसन तरीके से एंव ऑनलाइन डाटा स्टोर|\n" +
                "अभी डाउनलोड करें !! \n"+
                "https://play.google.com/store/apps/details?id=com.sanshy.buysellinventory";
        String shareSubject ="Buy Sell Inventory | खरीद बेच का हिसाब ";
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,shareText);
        intent.putExtra(Intent.EXTRA_SUBJECT,shareSubject);
        startActivity(Intent.createChooser(intent, "Share App Via"));

    }

    public void logout(View view)
    {

        Log.d(TAG, "Subscribing to news topic");
        // [START subscribe_topics]
        FirebaseMessaging.getInstance().subscribeToTopic("news")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = getString(R.string.msg_subscribed);
                        if (!task.isSuccessful()) {
                            msg = getString(R.string.msg_subscribe_failed);
                        }
                        Log.d(TAG, msg);
                        Toast.makeText(home.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
        // [END subscribe_topics]

        //TODO UnComment It

//        if (mRewardedVideoAd.isLoaded()) {
//            mRewardedVideoAd.show();
//        }
//        AuthUI.getInstance()
//                .signOut(this)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    public void onComplete(@NonNull Task<Void> task) {
//                        home.this.finish();
//                        startActivity(new Intent(home.this,MainActivity.class));
//                    }
//                });
    }

    public void help(View view){

        // Get token
        String token = FirebaseInstanceId.getInstance().getToken();

        // Log and toast
        String msg = getString(R.string.msg_token_fmt, token);
        Log.d(TAG, msg);
        Toast.makeText(home.this, msg, Toast.LENGTH_SHORT).show();

        //TODO Uncomment it
        //startActivity(new Intent(this,help.class));
    }
    public void ad(View view){
        startActivity(new Intent(this,SideBusinessWork.class));
    }

    public void resetB(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Warning !! चेतावनी !!")
                .setMessage("Do You Really Want To Clear Your All Products, Supplier, Customer, Stock, History and Other all Data You Will Not Able to Restore this!!" +
                        "\n क्या आप सभी उत्पाद, वितरक, ग्राहक, Stock, History और अन्य को मिटाना चाहते हैं | आप इसे हमेसा के लिए खो देंगे | ")
                .setPositiveButton("Cancel|छोड़े",null)
                .setNegativeButton("Reset|मिटाएँ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final AlertDialog.Builder builder1 = new AlertDialog.Builder(home.this);
                        LayoutInflater inflater = home.this.getLayoutInflater();
                        final View myD = inflater.inflate(R.layout.reset_q, null);
                        builder1.setView(myD)
                                .setPositiveButton("Cancel|छोड़े",null)
                                .setNegativeButton("Reset|मिटाएँ", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        EditText ans = myD.findViewById(R.id.ans);
                                        String Ans = ans.getText().toString();
                                        if (Ans.isEmpty())
                                        {
                                            ans.setError("Solve It!");
                                        }
                                        else{
                                            try{
                                                double sol = Double.parseDouble(Ans);
                                                if (sol == 109)
                                                {
                                                    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
                                                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                                                    FirebaseUser user = mAuth.getCurrentUser();

                                                    DatabaseReference mUserRef = mRootRef.child(user.getUid());
                                                    mUserRef.removeValue();

                                                    Toast.makeText(home.this, "App Reset Done!!", Toast.LENGTH_SHORT).show();

                                                }
                                                else {
                                                    dialogInterface.cancel();
                                                }
                                            }catch (Exception ex){
                                                Toast.makeText(home.this, "Feedback Developer With ->"+ex, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                })
                                .create().show();

                    }
                })
                .create().show();
    }
//    @Override
//    protected void onPostResume() {
//        super.onPostResume();
//        connectionCheck();
//    }
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

    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {

    }

    @Override
    public void onRewarded(RewardItem rewardItem) {

    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }

    @Override
    public void onRewardedVideoCompleted() {

    }
}
