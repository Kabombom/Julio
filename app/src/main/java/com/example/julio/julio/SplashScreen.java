package com.example.julio.julio;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import java.io.LineNumberReader;

public class SplashScreen extends AppCompatActivity {

    private static final long SPLASHSCREEN_DELAY = 2000; // 3000 feels too "long"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        //Wait a few seconds before checking for existing accounts and launching the LoginActivity


        final Handler mainHandler = new Handler(this.getMainLooper());
        final Runnable myRunnable = new Runnable() {
            @Override
            public void run() {showProgress(true);
            } // This is your code
        };

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                mainHandler.post(myRunnable);

                final Intent intent = new Intent(this, StartActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();


            }
        }, SPLASHSCREEN_DELAY);
    }

}
