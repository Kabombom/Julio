package com.example.julio.julio;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends AppCompatActivity {

    private static final long SPLASHSCREEN_DELAY = 2000; // 3000 feels too "long"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        //Wait a few seconds before checking for existing accounts and launching the LoginActivity


        final Handler mainHandler = new Handler(this.getMainLooper());
        final Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                // This is your code
            }
        };

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                mainHandler.post(myRunnable);

                final Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();


            }
        }, SPLASHSCREEN_DELAY);
    }

}
