package com.daleel.ghazeihdaleel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        LottieAnimationView lottieAnimationView = findViewById(R.id.lottie);
        TextView textView = findViewById(R.id.textView_appname);

            textView.animate().translationY(+1400).setDuration(600).setStartDelay(3000);
            lottieAnimationView.animate().translationY(-1400).setDuration(700).setStartDelay(3000).withEndAction(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                    finish();
                }
            }) ;




    }
}