package com.example.tikitaka;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tikitaka.models.Post;
import com.example.tikitaka.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WritingActivity extends BaseActivity {

    private static final String TAG = "WritingActivity";
    private static final String REQUIRED = "Required";

    private DatabaseReference mDatabase;

    private EditText et_title_writing;
    private EditText et_content_writing;
    private EditText et_affirmative_writing;
    private EditText et_opposition_writing;
    private EditText et_references_writing;
    private Button btn_done_writing;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing);

        mDatabase = FirebaseDatabase.getInstance().getReference();

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
                submitPost();
            }
        });
    }

    private void submitPost() {
        // Get now date
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date time = new Date();

        final String title = et_title_writing.getText().toString();
        final String body = et_content_writing.getText().toString();
        final String affirmative = et_affirmative_writing.getText().toString();
        final String opposition = et_opposition_writing.getText().toString();
        final String reference = et_references_writing.getText().toString();
        final String date=format.format(time);

        // Title is required
        if (TextUtils.isEmpty(title)) {
            et_title_writing.setError(REQUIRED);
            return;
        }

        // Body is required
        if (TextUtils.isEmpty(body)) {
            et_content_writing.setError(REQUIRED);
            return;
        }

        // affirmative is required
        if (TextUtils.isEmpty(affirmative)) {
            et_affirmative_writing.setError(REQUIRED);
            return;
        }

        // opposition is required
        if (TextUtils.isEmpty(opposition)) {
            et_opposition_writing.setError(REQUIRED);
            return;
        }

        // reference is required
        if (TextUtils.isEmpty(reference)) {
            et_references_writing.setError(REQUIRED);
            return;
        }


        // Disable button so there are no multi-posts
        setEditingEnabled(false);
        Toast.makeText(this, "Posting...", Toast.LENGTH_SHORT).show();

        // [START single_value_read]
        final String userId = getUid();

        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);

                        // [START_EXCLUDE]
                        if (user == null) {
                            // User is null, error out
                            Log.e(TAG, "User " + userId + " is unexpectedly null");
                            Toast.makeText(WritingActivity.this,
                                    "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Write new post
                            writeNewPost(userId, user.username, title, body, affirmative, opposition, reference, date);
                        }

                        // Finish this Activity, back to the stream
                        setEditingEnabled(true);
                        finish();
                        // [END_EXCLUDE]
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        // [START_EXCLUDE]
                        setEditingEnabled(true);
                        // [END_EXCLUDE]
                    }
                });
        // [END single_value_read]
    }

    private void setEditingEnabled(boolean enabled) {
        et_title_writing.setEnabled(enabled);
        et_content_writing.setEnabled(enabled);
        if (enabled) {
            btn_done_writing.setClickable(true);
        } else {
            btn_done_writing.setClickable(false);
        }
    }

    // [START write_fan_out]
    private void writeNewPost(String userId, String username, String title, String body, String affirmative, String opposition, String reference, String date) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        String key = mDatabase.child("posts").push().getKey();

        Post post = new Post(userId, username, title, body, affirmative, opposition, reference, date);
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + key, postValues);
        childUpdates.put("/user-posts/" + userId + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
    }
    // [END write_fan_out]

    //글쓰기 취소 팝업
    @Override
    public void onBackPressed() {

        // AlertDialog 빌더를 이용해 종료시 발생시킬 창을 띄운다
        AlertDialog.Builder alBuilder = new AlertDialog.Builder(this);
        alBuilder.setMessage("글쓰기를 취소하시겠습니까?");

        // "예" 버튼을 누르면 실행되는 리스너
        alBuilder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();// 현재 액티비티를 종료한다. (MainActivity에서 작동하기 때문에 애플리케이션을 종료한다.)
                Intent intent= new Intent(WritingActivity.this, PostListActivity.class);
                startActivity(intent);
            }
        });
        // "아니오" 버튼을 누르면 실행되는 리스너
        alBuilder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return; // 아무런 작업도 하지 않고 돌아간다
            }
        });
        alBuilder.setTitle("취소");
        alBuilder.show(); // AlertDialog.Bulider로 만든 AlertDialog를 보여준다.
    }
}
