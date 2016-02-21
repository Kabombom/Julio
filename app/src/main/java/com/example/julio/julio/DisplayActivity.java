package com.example.julio.julio;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.github.kexanie.library.MathView;

public class DisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle b = getIntent().getExtras();
        Section section = (Section)b.getSerializable("Section");
        getSupportActionBar().setTitle(section.title);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TeXEditorActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.displayLayout);

        for(Element element:section.content){
            View layoutItem = getLayoutInflater().inflate(R.layout.element, null);
            LinearLayout layout = (LinearLayout)layoutItem.findViewById(R.id.element_layout);
            if(element.type == Element.ElementType.Text){
                TextView textView = (TextView)layout.findViewById(R.id.TextView);
                textView.setText(element.content);
                textView.setVisibility(View.VISIBLE);
            }
            else if(element.type == Element.ElementType.Latex){
                MathView mathView = (MathView)layout.findViewById(R.id.MathView);
                mathView.setText(element.content);
                mathView.setVisibility(View.VISIBLE);
            }
            linearLayout.addView(layoutItem);
        }

    }

}
