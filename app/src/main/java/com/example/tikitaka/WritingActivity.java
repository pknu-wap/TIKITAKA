package com.example.tikitaka;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class WritingActivity extends AppCompatActivity {
    private EditText et_title_writing;
    private EditText et_content_writing;
    private EditText et_affirmative_writing;
    private EditText et_opposition_writing;
    private EditText et_references_writing;
    private Button btn_done_writing;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing);

        et_title_writing=findViewById(R.id.et_title_writing);
        et_content_writing=findViewById(R.id.et_content_writing);
        et_affirmative_writing=findViewById(R.id.et_affirmative_writing);
        et_opposition_writing=findViewById(R.id.et_opposition_writing);
        et_references_writing=findViewById(R.id.et_references_writing);
        btn_done_writing=findViewById(R.id.btn_done_writing);
        Toolbar tb = (Toolbar) findViewById(R.id.app_toolbar_writing) ;
        setSupportActionBar(tb) ;

        btn_done_writing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(WritingActivity.this, "등록 완료", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
