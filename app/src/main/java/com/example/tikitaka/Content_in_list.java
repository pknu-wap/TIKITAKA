package com.example.tikitaka;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class Content_in_list extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_in_list);

        Intent profile_intent = getIntent();
        Bundle profile_bundle = profile_intent.getExtras();
        String title = profile_bundle.getString("title");
        String date = profile_bundle.getString("date");
        String writer = profile_bundle.getString("writer");
    }
}
