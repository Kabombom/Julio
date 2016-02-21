package com.example.julio.julio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

public class StartActivity extends AppCompatActivity {

    public static final String MY_PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton addButton = (FloatingActionButton) findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Form.class);
                startActivity(intent);
            }
        });


        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.tile_layout);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("Tiles", "");
        if(!json.equals("")) {
            final Section[] strings = gson.fromJson(json, Section[].class);
            final ArrayList<Section> sections = new ArrayList<Section>(Arrays.asList(strings));
            for (final Section section : strings) {

                final View layoutItem = getLayoutInflater().inflate(R.layout.tile, null);
                layoutItem.setBackgroundColor(section.color);

                ((TextView) layoutItem.findViewById(R.id.tile_title)).setText(section.title);
                ((TextView) layoutItem.findViewById(R.id.tile_description)).setText(section.description);

                layoutItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        findViewById(R.id.deleteButton).setVisibility(View.GONE);

                        Intent intent = new Intent(getApplicationContext(), DisplayActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("SectionId", section.id);
                        bundle.putSerializable("Sections", sections);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }

                });

                layoutItem.setOnFocusChangeListener(new View.OnFocusChangeListener() {

                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            findViewById(R.id.deleteButton).setVisibility(View.GONE);
                        }
                    }
                });

                final View separatorView = new View(this);
                separatorView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 75));
                separatorView.setVisibility(View.INVISIBLE);

                layoutItem.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        final FloatingActionButton deleteButton = (FloatingActionButton) findViewById(R.id.deleteButton);
                        deleteButton.setVisibility(View.VISIBLE);
                        deleteButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                deleteButton.setVisibility(View.GONE);

                                sections.remove(section);
                                linearLayout.removeView(layoutItem);
                                linearLayout.removeView(separatorView);
                            }
                        });

                        return true;
                    }
                });

                linearLayout.addView(layoutItem);
                linearLayout.addView(separatorView);

            }
        }

    }
}
