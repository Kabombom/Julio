package com.example.julio.julio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import io.github.kexanie.library.MathView;

public class TeXEditorActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private static final int CAMERA_REQUEST = 1888;
    private static final int RESULT_LOAD_IMAGE = 1999;

    private Bitmap photo_bitmap = null;

    private Section section;
    private ArrayList<Section> sections;
    private int sectionId;

    private int selectedType = R.id.item_latex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tex_editor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Bundle b = getIntent().getExtras();
        sectionId = b.getInt("SectionId");
        sections = (ArrayList<Section>)b.getSerializable("Sections");
        section = Section.getSectionById(sections, sectionId);

        ImageButton texMenuButton = (ImageButton) findViewById(R.id.tex_menu_button);
        texMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(TeXEditorActivity.this, v);
                popupMenu.setOnMenuItemClickListener(TeXEditorActivity.this);
                popupMenu.inflate(R.menu.menu_sheet_type);
                popupMenu.show();
            }
        });

        final MathView mathView = (MathView) findViewById(R.id.latex_preview);

        TextWatcher inputTextWatcher = new TextWatcher() {
            public void afterTextChanged(Editable s) {

                // auto add missing math mode encapsulation
                String tex = s.toString();
                if(!tex.contains("$"))
                    tex = "$$" + tex + "$$";

                mathView.setText(tex);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after){
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        };

        ((EditText) findViewById(R.id.latex_edittext)).addTextChangedListener(inputTextWatcher);

        Button takePhotoButton = (Button) findViewById(R.id.take_photo);
        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFromCamera(v);
            }
        });

        Button loadImageButton = (Button) findViewById(R.id.load_image);
        loadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFromGallery(v);
            }
        });

        FloatingActionButton applyButton = (FloatingActionButton) findViewById(R.id.applyButton);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (selectedType){
                    case R.id.item_text:
                        EditText editText = (EditText)findViewById(R.id.note_edittext);
                        section.content.add(new Element(Element.ElementType.Text, editText.getText().toString()));
                        break;


                    case R.id.item_latex:
                        MathView mathView2 = (MathView) findViewById(R.id.MathJax_Preview);
                        section.content.add(new Element(Element.ElementType.Latex,mathView2.getText().toString()));
                        break;

                    case R.id.item_image:
                        break;

                    default:

                }
                SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences(StartActivity.MY_PREFS_NAME, Context.MODE_PRIVATE).edit();

                Intent intent = new Intent(getApplicationContext(), DisplayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Sections", sections);
                bundle.putInt("SectionId",sectionId);
                Gson gson = new Gson();

                String json = gson.toJson(sections);
                System.out.println(json);
                editor.putString("Tiles",json).apply();

                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


    }

    public boolean onMenuItemClick(MenuItem item) {

        selectedType = item.getItemId();
        switch (selectedType) {

            case R.id.item_text:
                findViewById(R.id.text_layout).setVisibility(View.VISIBLE);
                findViewById(R.id.latex_layout).setVisibility(View.GONE);
                findViewById(R.id.image_layout).setVisibility(View.GONE);
                return true;

            case R.id.item_latex:
                findViewById(R.id.text_layout).setVisibility(View.GONE);
                findViewById(R.id.latex_layout).setVisibility(View.VISIBLE);
                findViewById(R.id.image_layout).setVisibility(View.GONE);
                return true;

            case R.id.item_image:
                findViewById(R.id.text_layout).setVisibility(View.GONE);
                findViewById(R.id.latex_layout).setVisibility(View.GONE);
                findViewById(R.id.image_layout).setVisibility(View.VISIBLE);
                return true;

            default:
                return false;
        }
    }

    public void getFromCamera(View view) {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    private void displayPreview() {
        ImageView imageView = (ImageView) findViewById(R.id.tex_image_preview);
        imageView.setImageBitmap(photo_bitmap);

    }

    public void getFromGallery(View view) {
        Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {
                photo_bitmap = (Bitmap) data.getExtras().get("data");

                displayPreview();
            }
            else if (requestCode == RESULT_LOAD_IMAGE) {
                try {
                    Bitmap bitmap = decodeUri(data.getData());
                    if(bitmap != null) {
                        photo_bitmap = bitmap;
                        displayPreview();
                    }
                    else
                        Toast.makeText(getBaseContext(), "Oops...", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Toast.makeText(getBaseContext(), "Oops...", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }
    }

    private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(
                getContentResolver().openInputStream(selectedImage), null, o);

        final int REQUIRED_SIZE = 960;

        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(
                getContentResolver().openInputStream(selectedImage), null, o2);
    }

}
