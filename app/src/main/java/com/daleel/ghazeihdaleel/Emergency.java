package com.daleel.ghazeihdaleel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Emergency extends AppCompatActivity  {
    private static final int REQEST_CODE_CALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);
        //Action BAr
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.emergency);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        Button police_maghdoche = findViewById(R.id.btn_police_maghdouche);
        Button police_operation = findViewById(R.id.btn_police_operation);
        Button ambulance_Redcros = findViewById(R.id.btn_ambulance_redcros);
        Button ambulane_resala = findViewById(R.id.btn_ambulance_resala);
        Button firestation = findViewById(R.id.btn_firesation);

        police_operation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall("112");
            }
        });
        police_maghdoche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall("07200795");
            }
        });
        ambulance_Redcros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall("07722532");
            }
        });
        ambulane_resala.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall("03880052");
            }
        });
        firestation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall("07720061");
            }
        });



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQEST_CODE_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void makePhoneCall(String phone_number) {

            if (ContextCompat.checkSelfPermission(Emergency.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Emergency.this,
                        new String[]{Manifest.permission.CALL_PHONE}, REQEST_CODE_CALL);
            } else {
                String dial = "tel:" + phone_number;
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(dial)));
            }
        }


    //Back press
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}