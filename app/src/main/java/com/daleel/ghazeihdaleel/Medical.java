package com.daleel.ghazeihdaleel;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Medical extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical);
        //Action BAr
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.medical);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

    }

    public void dental_intent(View view) {
        startActivity(new Intent(Medical.this,list.class).putExtra("FireBaseReference","dental"));

    }

    public void doctor_intent(View view) {
        startActivity(new Intent(Medical.this,list.class).putExtra("FireBaseReference","doctors"));
    }

    public void hospitals_and_medical_centers_intent(View view) {
        startActivity(new Intent(Medical.this,list.class).putExtra("FireBaseReference","hospitals"));
    }

    //Back press
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}