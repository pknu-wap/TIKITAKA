package com.example.tikitaka;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Content_in_list extends AppCompatActivity {

    private TextView TextView_content_title;
    private TextView TextView_content_date;
    private TextView TextView_content_writer;
    private String title;
    private String date;
    private String writer;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_in_list);

        TextView_content_title=findViewById(R.id.TextView_content_title);
        TextView_content_date=findViewById(R.id.TextView_content_date);
        TextView_content_writer=findViewById(R.id.TextView_content_writer);

        Intent profile_intent = getIntent();
        Bundle profile_bundle = profile_intent.getExtras();

        title = profile_bundle.getString("title");
        date = profile_bundle.getString("date");
        writer = profile_bundle.getString("writer");

        System.out.println(title);
        System.out.println(date);
        System.out.println(writer);

        TextView_content_title.setText(title);
        TextView_content_date.setText(date);
        TextView_content_writer.setText(writer);
    }
}
