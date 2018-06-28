package com.sanshy.buysellinventory;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class Main2Activity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    EditText name,email,phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);

        mAuth = FirebaseAuth.getInstance();

        AdView adView1,adView2;
        adView1 = findViewById(R.id.adView);
        adView2 = findViewById(R.id.adView2);

        adView1.loadAd(new AdRequest.Builder().build());
        adView2.loadAd(new AdRequest.Builder().build());
    }

    public void save(View view){
        String Name = name.getText().toString();
        String Email = email.getText().toString();
        String Phone = phone.getText().toString();

        if (Name.isEmpty())
        {
            name.setError(getString(R.string.fill_it));
            return;
        }

        FirebaseUser currentUser = mAuth.getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(Name)
                .build();

        currentUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Main2Activity.this, R.string.profile_updated, Toast.LENGTH_SHORT).show();
                            Main2Activity.this.finish();
                        }
                        else
                        {
                            Toast.makeText(Main2Activity.this, getString(R.string.something_is_wrong_), Toast.LENGTH_SHORT).show();
                            Main2Activity.this.finish();
                        }
                    }
                });

    }
    public void cancel(View view){
        this.finish();
    }
}
