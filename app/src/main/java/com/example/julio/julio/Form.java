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
import android.widget.EditText;
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

        Button button = (Button) findViewById(R.id.Text);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TeXEditorActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton applyButton = (FloatingActionButton) findViewById(R.id.apply_action);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(StartActivity.MY_PREFS_NAME, Context.MODE_PRIVATE);
                Gson gson = new Gson();
                String json = sharedPreferences.getString("Tiles", "");
                int lastId = sharedPreferences.getInt("LastId",0);
                List<Section> strings;
                if(!json.equals("")) {
                    Section[] stringsArray = gson.fromJson(json, Section[].class);
                    strings = Arrays.asList(stringsArray);
                    strings = new ArrayList<>(strings);
                }
                else{
                    strings = new ArrayList<Section>();
                }
                EditText title = (EditText)findViewById(R.id.TitleInput);
                EditText description = (EditText)findViewById(R.id.TileDescription);
                Section section = new Section(new ArrayList<String>(),title.getText().toString(),description.getText().toString(),lastId+1);
                strings.add(section);
                SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences(StartActivity.MY_PREFS_NAME, Context.MODE_PRIVATE).edit();

                editor.putInt("LastId",lastId+1);
                json = gson.toJson(strings);
                editor.putString("Tiles",json).apply();
                Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                startActivity(intent);
            }
        });

    }

}
