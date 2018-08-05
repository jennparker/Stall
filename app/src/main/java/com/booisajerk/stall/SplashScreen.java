package com.booisajerk.stall;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by jenniferparker on 3/16/17.
 */


public class SplashScreen extends AppCompatActivity {

    private static final String LOG_TAG = "MYTAG " + SplashScreen.class.getSimpleName();
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashScreen.this, MainActivity.class);
                Log.d(LOG_TAG, "Starting Splash Activity Intent.");

                startActivity(mainIntent);

                // close this activity
                Log.d(LOG_TAG, "Closing splash activity after " + SPLASH_TIME_OUT + " ms.");
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

}

