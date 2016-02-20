package com.example.julio.julio;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import io.github.kexanie.library.MathView;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Form extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(StartActivity.MY_PREFS_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("Tiles", "");
        List<String> strings;
        if(!json.equals("")) {
            System.out.println("HEYO");
            String[] stringsArray = gson.fromJson(json, String[].class);
            strings = Arrays.asList(stringsArray);
            strings = new ArrayList<>(strings);
        }
        else{
            strings = new ArrayList<String>();
        }
        strings.add("Tile " + new Random().nextInt());

        SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences(StartActivity.MY_PREFS_NAME, Context.MODE_PRIVATE).edit();
        json = gson.toJson(strings);
        editor.putString("Tiles",json).apply();
        Button button = (Button) findViewById(R.id.Text);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CheatSheetActivity.class);
                startActivity(intent);
            }
        });
    }

}
