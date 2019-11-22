package com.example.tikitaka;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
    private Button button_write;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //현재 날짜 받아오기
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        Date time = new Date();
        String current_time=format.format(time);

        Toolbar tb = (Toolbar) findViewById(R.id.app_toolbar) ;
        setSupportActionBar(tb) ;

        button_write=findViewById(R.id.button_write);
        button_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WritingActivity.class);
                startActivity(intent);
            }
        });

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar_action, menu) ;

        return true ;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_profile :
                Intent it_profile = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(it_profile);
                break;
            case R.id.action_news:
                Intent it_news = new Intent(MainActivity.this, NewsActivity.class);
                startActivity(it_news);
            default :
                return super.onOptionsItemSelected(item) ;
        }
        return false;
    }

    @Override
    public void onBackPressed() {

        // AlertDialog 빌더를 이용해 종료시 발생시킬 창을 띄운다
        AlertDialog.Builder alBuilder = new AlertDialog.Builder(this);
        alBuilder.setMessage("종료하시겠습니까?");

        // "예" 버튼을 누르면 실행되는 리스너
        alBuilder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish(); // 현재 액티비티를 종료한다. (MainActivity에서 작동하기 때문에 애플리케이션을 종료한다.)
            }
        });
        // "아니오" 버튼을 누르면 실행되는 리스너
        alBuilder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return; // 아무런 작업도 하지 않고 돌아간다
            }
        });
        alBuilder.setTitle("프로그램 종료");
        alBuilder.show(); // AlertDialog.Bulider로 만든 AlertDialog를 보여준다.
    }

}
