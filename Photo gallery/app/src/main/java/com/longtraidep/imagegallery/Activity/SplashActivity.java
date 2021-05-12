package com.longtraidep.imagegallery.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.longtraidep.imagegallery.R;

public class SplashActivity extends AppCompatActivity {

        private static final long SPLASH_TIME_OUT = 2500;

        @Override
        protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_splash);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(SplashActivity.this, CameraActivity.class);
                    startActivity(i);
                    // close this activity
                    finish();
                }
            }, SPLASH_TIME_OUT);
        }
}