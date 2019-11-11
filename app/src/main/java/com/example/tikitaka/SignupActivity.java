package com.example.tikitaka;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class SignupActivity extends AppCompatActivity {

    Button btn_signup_complete;
    EditText pw_first, pw_second;
    ImageView pw_Image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        btn_signup_complete = findViewById(R.id.btn_signup_complete);
        btn_signup_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        pw_first = (EditText)findViewById(R.id.pw_first);
        pw_second = (EditText)findViewById(R.id.pw_second);
        pw_Image = (ImageView)findViewById(R.id.pw_Image);

        pw_second.addTextChangedListener(new TextWatcher() {

            @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if(pw_first.getText().toString().equals(pw_second.getText().toString())) {
                pw_Image.setImageResource(R.drawable.check_img);
            } else {

                pw_Image.setImageResource(R.drawable.red_cross);
            }
        }

        @Override
        public void afterTextChanged(Editable editable)  {

        }
    });

    }
}
