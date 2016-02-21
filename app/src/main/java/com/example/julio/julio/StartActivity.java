package com.example.julio.julio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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

                        findViewById(R.id.edit_buttons_layout).setVisibility(View.GONE);

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
                            findViewById(R.id.edit_buttons_layout).setVisibility(View.GONE);
                        }
                    }
                });

                linearLayout.addView(layoutItem);

                final View separatorView = new View(this);
                separatorView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 75));
                separatorView.setVisibility(View.INVISIBLE);
                linearLayout.addView(separatorView);

                layoutItem.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        findViewById(R.id.edit_buttons_layout).setVisibility(View.VISIBLE);

                        FloatingActionButton deleteButton = (FloatingActionButton) findViewById(R.id.deleteButton);
                        deleteButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                sections.remove(section);
                                linearLayout.removeView(layoutItem);
                                linearLayout.removeView(separatorView);
                            }
                        });

                        return true;
                    }
                });

            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
