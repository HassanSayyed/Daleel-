package com.daleel.ghazeihdaleel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



    }
    public void emergency_intent(View view) {
        startActivity(new Intent(MainActivity.this,Emergency.class)) ;
    }

    public void delivery_intent(View view) {
        startActivity(new Intent(MainActivity.this,list.class).putExtra("FireBaseReference","delivery"));
    }

    public void food_intent(View view) {
        startActivity(new Intent(MainActivity.this,list.class).putExtra("FireBaseReference","food"));
    }


    public void shisha_intent(View view) {
        startActivity(new Intent(MainActivity.this,list.class).putExtra("FireBaseReference","shisha"));

    }

    public void medical_intent(View view) {
        startActivity(new Intent(MainActivity.this,Medical.class));

    }

    public void workers_intent(View view) {
        Toast.makeText(this, R.string.might_add_later,Toast.LENGTH_SHORT).show();
    }

    public void teaching_intent(View view) {
        startActivity(new Intent(MainActivity.this,list.class).putExtra("FireBaseReference","teaching"));
    }

    public void taxi_intent(View view) {
        startActivity(new Intent(MainActivity.this,list.class).putExtra("FireBaseReference","taxi"));

    }
}