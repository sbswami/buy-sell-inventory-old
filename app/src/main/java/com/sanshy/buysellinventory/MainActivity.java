package com.sanshy.buysellinventory;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sanshy.buysellinventory.NetworkConnectivityCheck.connectionCheck;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

    }

    ArrayList<String> allUserList = new ArrayList<>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in

                MyProgressBar.ShowProgress(this);

                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                final FirebaseUser user = mAuth.getCurrentUser();

                final DatabaseReference userIdListRef = mRootRef.child("AdminWork/AllUserList");
                userIdListRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        allUserList.clear();
                        if (dataSnapshot.exists()){
                            allUserList = (ArrayList<String>) dataSnapshot.getValue();

                            for (int k = 0; k < allUserList.size(); k++){
                                if (allUserList.get(k).equals(user.getUid())){
                                    break;
                                }
                                if (k==(allUserList.size()-1)){
                                    allUserList.add(user.getUid());
                                    userIdListRef.setValue(allUserList);


                                    final DatabaseReference allInfoRef = mRootRef.child("AdminWork/AllDataList");
                                    allInfoRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            ArrayList<Map<String,String>> userDList = new ArrayList<>();
                                            Map<String, String> userData = new HashMap<>();
                                            userData.put("Uid",user.getUid());
                                            userData.put("DisplayName",user.getDisplayName());
                                            userData.put("Email",user.getEmail());
                                            try{
                                                userData.put("Photo",user.getPhotoUrl().toString());
                                            }catch (Exception ex){}
                                            userData.put("Phone",user.getPhoneNumber());

                                            if (dataSnapshot.exists()){
                                                try{
                                                    userDList = (ArrayList<Map<String, String>>) dataSnapshot.getValue();
                                                    userDList.add(userData);
                                                    allInfoRef.setValue(userDList);

                                                }catch (Exception ex){}

                                            }
                                            else {
                                                userDList.add(userData);
                                                allInfoRef.setValue(userDList);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                }
                            }
                        }
                        MyProgressBar.HideProgress();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                final DatabaseReference createDate = mRootRef.child(user.getUid()+"/CreateDate");
                createDate.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!(dataSnapshot.exists())){
                            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            Date date = new Date();
                            String Date = dateFormat.format(date);
                            createDate.setValue(Date);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                MyUserStaticClass.userIdMainStatic = user.getUid();

                SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.tour_show_time), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt(getString(R.string.show_key), 1);
                editor.apply();

                startActivity(new Intent(this,home.class));
                this.finish();
                // ...
            } else {
                Toast.makeText(this, R.string.sign_failed_text, Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void helpMain(View view){
        startActivity(new Intent(this,help.class));
    }

    public void loginView(View view)
    {

        MyProgressBar.ShowProgress(this);
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

// Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setLogo(R.drawable.logo)      // Set logo drawable
                        .setTheme(R.style.AppTheme)
                        .build(),
                RC_SIGN_IN);

    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        connectionCheck(this);

    }

}
