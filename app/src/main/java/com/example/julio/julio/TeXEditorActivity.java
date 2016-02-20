package com.example.julio.julio;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import io.github.kexanie.library.MathView;

public class TeXEditorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tex_editor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final MathView mathView = (MathView) findViewById(R.id.MathJax_Preview);

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

        ((EditText) findViewById(R.id.formula_tex)).addTextChangedListener(inputTextWatcher);

    }

}
