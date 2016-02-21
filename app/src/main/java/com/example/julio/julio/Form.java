package com.example.julio.julio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import yuku.ambilwarna.AmbilWarnaDialog;

public class Form extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final Context context = this;

        final ImageView imageView = (ImageView) findViewById(R.id.section_color);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AmbilWarnaDialog dialog = new AmbilWarnaDialog(context, ((ColorDrawable) imageView.getBackground()).getColor(), new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        // color is the color selected by the user.
                        imageView.setBackgroundColor(color);
                    }

                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {
                        // cancel was selected by the user
                    }

                });
                dialog.show();
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
                int backgroundColor = ((ColorDrawable) imageView.getBackground()).getColor();
                ArrayList<Element> elements = new ArrayList<Element>();
                Section section = new Section(elements,title.getText().toString(),description.getText().toString(),lastId+1,backgroundColor);
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
