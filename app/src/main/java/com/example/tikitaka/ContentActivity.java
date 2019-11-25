package com.example.tikitaka;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ContentActivity extends AppCompatActivity {

    private TextView TextView_content_title;
    private TextView TextView_content_date;
    private TextView TextView_content_writer;
    private String title;
    private String date;
    private String writer;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        TextView_content_title=findViewById(R.id.TextView_content_title);
        TextView_content_date=findViewById(R.id.TextView_content_date);
        Toolbar tb = (Toolbar) findViewById(R.id.app_toolbar_writing) ;
        setSupportActionBar(tb) ;


        Intent profile_intent = getIntent();
        Bundle profile_bundle = profile_intent.getExtras();

        title = profile_bundle.getString("title");
        date = profile_bundle.getString("date");


        System.out.println(title);
        System.out.println(date);


        TextView_content_title.setText(title);
        TextView_content_date.setText(date);

    }
}
