package com.example.tikitaka;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<recyclerData> myDataset=new ArrayList<>();
    private String[] title={"논제1","논제2","논제3","논제4"};
    private String[] date={"2019-11-10","2019-11-11","2019-11-12","2019-11-13"};
    private String[] writer={"작성자1","작성자2","작성자3","작성자4"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //현재 날짜 받아오기
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        Date time = new Date();
        String current_time=format.format(time);


        recyclerView = findViewById(R.id.my_recycler_view);
        // use this setting to improve performance if you know that changes
        // in Content_in_list do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(myDataset, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int list_position=(int)v.getTag();
                Intent intent= new Intent(MainActivity.this, Content_in_list.class);
                intent.putExtra("title",title[list_position]);
                intent.putExtra("date",date[list_position]);
                intent.putExtra("writer",writer[list_position]);

                startActivity(intent);
            }
        });
        recyclerView.setAdapter(mAdapter);


        for(int i=0;i<title.length;i++){
            myDataset.add(new recyclerData(title[i], date[i], writer[i]));
        }
    }
}
