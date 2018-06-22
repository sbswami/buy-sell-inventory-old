package com.sanshy.buysellinventory;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class help extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
    }
    public void back(View view){
        this.finish();
    }
}
