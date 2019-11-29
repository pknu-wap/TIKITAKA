package com.example.tikitaka;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class NewsActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        Toolbar tb = (Toolbar) findViewById(R.id.app_toolbar_news) ;
        setSupportActionBar(tb) ;

    }
}
