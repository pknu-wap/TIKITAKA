package com.example.tikitaka;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tikitaka.models.Post;
import com.example.tikitaka.viewholder.PostViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PostListActivity extends AppCompatActivity {

    private static final String TAG = "PostListFragment";

    private DatabaseReference mDatabase;
    private FirebaseRecyclerAdapter<Post, PostViewHolder> mAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager mManager;
    private Button button_write;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //현재 날짜 받아오기
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        Date time = new Date();
        String current_time=format.format(time);

        //툴바
        Toolbar tb = (Toolbar) findViewById(R.id.app_toolbar_main) ;
        setSupportActionBar(tb) ;

        mDatabase= FirebaseDatabase.getInstance().getReference();
        recyclerView = findViewById(R.id.my_recycler_view);
        // use this setting to improve performance if you know that changes
        // in ContentActivity do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // Set up Layout Manager, reverse layout
        mManager = new LinearLayoutManager(this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mManager);

        // Set up FirebaseRecyclerAdapter with the Query
        Query postsQuery = getQuery(mDatabase);

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Post>()
                .setQuery(postsQuery, Post.class)
                .build();

        mAdapter=new FirebaseRecyclerAdapter<Post, PostViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PostViewHolder holder, int position, @NonNull final Post model) {
                final DatabaseReference postRef=getRef(position);

                //모든 포스트뷰에 클릭리스너 넣기
                final String postKey=postRef.getKey();
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(PostListActivity.this,ContentActivity.class);
                        intent.putExtra(ContentActivity.EXTRA_POST_KEY, postKey);
                        startActivity(intent);
                    }
                });

                // Determine if the current user has liked this post and set UI accordingly
                if (model.good.containsKey(getUid())) {
                    holder.goodView.setImageResource(R.drawable.baseline_thumb_up_black_18dp);
                } else {
                    holder.goodView.setImageResource(R.drawable.outline_thumb_up_black_18dp);
                }

                // Determine if the current user has liked this post and set UI accordingly
                if (model.bad.containsKey(getUid())) {
                    holder.badView.setImageResource(R.drawable.baseline_thumb_down_black_18dp);
                } else {
                    holder.badView.setImageResource(R.drawable.outline_thumb_down_black_18dp);
                }

                // Bind Post to ViewHolder, setting OnClickListener for the star button
                holder.bindToPost(model, new View.OnClickListener() {
                    @Override
                    public void onClick(View starView) {
                        // Need to write to both places the post is stored
                        DatabaseReference globalPostRef = mDatabase.child("posts").child(postRef.getKey());
                        DatabaseReference userPostRef = mDatabase.child("user-posts").child(model.uid).child(postRef.getKey());

                        // Run two transactions
                        onGoodClicked(globalPostRef);
                        onGoodClicked(userPostRef);
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Need to write to both places the post is stored
                        DatabaseReference globalPostRef = mDatabase.child("posts").child(postRef.getKey());
                        DatabaseReference userPostRef = mDatabase.child("user-posts").child(model.uid).child(postRef.getKey());

                        // Run two transactions
                        onBadClicked(globalPostRef);
                        onBadClicked(userPostRef);
                    }
                });

            }

            @NonNull
            @Override
            public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                return new PostViewHolder(inflater.inflate(R.layout.item_post,parent,false));
            }
        };

        recyclerView.setAdapter(mAdapter);

        //글쓰기 버튼
        button_write=findViewById(R.id.button_write);
        button_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostListActivity.this, WritingActivity.class);
                startActivity(intent);
            }
        });
    }

    //툴바 생성
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar_action, menu) ;

        return true ;
    }

    //툴바 아이템
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_profile :
                Intent it_profile = new Intent(PostListActivity.this, ProfileActivity.class);
                startActivity(it_profile);
                break;
            case R.id.action_news:
                Intent it_news = new Intent(PostListActivity.this, NewsActivity.class);
                startActivity(it_news);
            default :
                return super.onOptionsItemSelected(item) ;
        }
        return false;
    }


    //뒤로가기 종료 팝업
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


    // [START post_stars_transaction]
    private void onGoodClicked(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Post p = mutableData.getValue(Post.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }

                if (p.good.containsKey(getUid())) {
                    // Unstar the post and remove self from stars
                    p.goodCount = p.goodCount - 1;
                    p.good.remove(getUid());
                } else {
                    // Star the post and add self to stars
                    p.goodCount = p.goodCount + 1;
                    p.good.put(getUid(), true);
                }

                // Set value and report transaction success
                mutableData.setValue(p);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d(TAG, "postTransaction:onComplete:" + databaseError);
            }
        });
    }
    // [END post_stars_transaction]


    // [START post_stars_transaction]
    private void onBadClicked(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Post p = mutableData.getValue(Post.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }

                if (p.bad.containsKey(getUid())) {
                    // Unstar the post and remove self from stars
                    p.badCount = p.badCount - 1;
                    p.bad.remove(getUid());
                } else {
                    // Star the post and add self to stars
                    p.badCount = p.badCount + 1;
                    p.bad.put(getUid(), true);
                }

                // Set value and report transaction success
                mutableData.setValue(p);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d(TAG, "postTransaction:onComplete:" + databaseError);
            }
        });
    }
    // [END post_stars_transaction]

    @Override
    public void onStart() {
        super.onStart();
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public  Query getQuery(DatabaseReference databaseReference){
        // [START recent_posts_query]
        // Last 100 posts, these are automatically the 100 most recent
        // due to sorting by push() keys
        Query recentPostsQuery = databaseReference.child("posts");
        // [END recent_posts_query]

        return recentPostsQuery;
    }


}
