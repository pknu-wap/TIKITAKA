package com.example.tikitaka;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tikitaka.models.Post;
import com.example.tikitaka.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
        setContentView(R.layout.activity_writing_test);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        et_title_writing=findViewById(R.id.et_title_writingtest);
        et_content_writing=findViewById(R.id.et_content_writingtest);
        et_affirmative_writing=findViewById(R.id.et_affirmative_writing);
        et_opposition_writing=findViewById(R.id.et_opposition_writing);
        et_references_writing=findViewById(R.id.et_references_writing);
        btn_done_writing=findViewById(R.id.btn_done_writingtest);
        Toolbar tb = (Toolbar) findViewById(R.id.app_toolbar_writing) ;
        setSupportActionBar(tb) ;

        btn_done_writing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WritingActivity.this, "등록 완료", Toast.LENGTH_SHORT).show();
                submitPost();
            }
        });
    }

    private void submitPost() {
        final String title = et_title_writing.getText().toString();
        final String body = et_content_writing.getText().toString();

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
                            writeNewPost(userId, user.username, title, body);
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
    private void writeNewPost(String userId, String username, String title, String body) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        String key = mDatabase.child("posts").push().getKey();
        Post post = new Post(userId, username, title, body);
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + key, postValues);
        childUpdates.put("/user-posts/" + userId + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
    }
    // [END write_fan_out]
}
