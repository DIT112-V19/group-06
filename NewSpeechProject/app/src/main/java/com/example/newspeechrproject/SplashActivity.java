package com.example.newspeechrproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Splash screen activity
 *
 * @author Catalin Prata
 *
 * edited by Marcus Olsson for educational use
 */

public class SplashActivity extends AppCompatActivity {

    //checking for "back button press" to avoid starting more activities and not going back to title by mistake
    private boolean mIsBackButtonPressed;
    private static final int SPLASH_DURATION = 2000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();

        //2 seconds delay before Main
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {

                //make sure title is not accessed by mistake
                finish();

                if (!mIsBackButtonPressed) {
                    //start main unless back press
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    SplashActivity.this.startActivity(intent);
                }

            }

        }, SPLASH_DURATION); //time in ms before next activity

    }

    @Override
    public void onBackPressed() {

        //put to true so not to start next activity
        mIsBackButtonPressed = true;
        super.onBackPressed();

    }
}