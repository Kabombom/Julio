package com.example.julio.julio;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import io.github.kexanie.library.MathView;

public class DisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle b = getIntent().getExtras();
        final int sectionId = b.getInt("SectionId");
        final ArrayList<Section> sections = (ArrayList<Section>)b.getSerializable("Sections");
        Section section = Section.getSectionById(sections,sectionId);
        getSupportActionBar().setTitle(section.title);

        FloatingActionButton addButton = (FloatingActionButton) findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TeXEditorActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("SectionId", sectionId);
                bundle.putSerializable("Sections", sections);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        final LinearLayout linearLayout = (LinearLayout)findViewById(R.id.displayLayout);

        for(Element element:section.content){
            final View layoutItem = getLayoutInflater().inflate(R.layout.element, null);
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
            else if(element.type == Element.ElementType.Image){
                ImageView imageView = (ImageView)layout.findViewById(R.id.ImageView);
                byte [] encodeByte= Base64.decode(element.content, Base64.DEFAULT);
                Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

                imageView.setImageBitmap(bitmap);
                imageView.setVisibility(View.VISIBLE);
            }

            final View separatorView = new View(this);
            separatorView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 75));
            separatorView.setVisibility(View.INVISIBLE);

            layoutItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //TODO: aqui belem

                    Intent intent = new Intent(getApplicationContext(), TeXEditorActivity.class);
                    Bundle bundle = new Bundle();
                    /*
                    bundle.putSerializable("SectionId", sectionId);
                    bundle.putSerializable("Sections", sections);
                    intent.putExtras(bundle);
                    */
                    startActivity(intent);
                }
            });

            layoutItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    final FloatingActionButton deleteButton = (FloatingActionButton) findViewById(R.id.deleteButton);
                    deleteButton.setVisibility(View.VISIBLE);
                    deleteButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            deleteButton.setVisibility(View.GONE);

                            linearLayout.removeView(layoutItem);
                            linearLayout.removeView(separatorView);
                        }
                    });

                    return false;
                }
            });

            linearLayout.addView(layoutItem);
            linearLayout.addView(separatorView);

        }

    }

}
