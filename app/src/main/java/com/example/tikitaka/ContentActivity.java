package com.example.tikitaka;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tikitaka.models.Comment;
import com.example.tikitaka.models.Post;
import com.example.tikitaka.models.User;
import com.example.tikitaka.viewholder.CommentViewHolder;
import com.example.tikitaka.viewholder.PostViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContentActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "ContentActivity";
    private static final String REQUIRED = "Required";
    public static final String EXTRA_POST_KEY = "post_key";
    public static final String EXTRA_COMMENT_KEY = "comment_key";

    private DatabaseReference mPostReference;
    private ValueEventListener mPostListener;
    private String mPostKey;
    private String mCommentKey;

    private DatabaseReference mCommentsReference;
    private FirebaseRecyclerAdapter<Comment, CommentViewHolder> mCommentAdapter;
    private RecyclerView mCommentsRecycler;
    private LinearLayoutManager mCommentManager;

    private TextView mAuthorView;
    private TextView mTitleView;
    private TextView mBodyView;
    private TextView mDateView;
    private TextView mAffirmativeView;
    private TextView mOppositionView;
    private TextView mReferenceView;
    private EditText mCommentField;
    private Button mCommentButton;
    private RadioGroup radioGroup;
    private RadioButton Rabtn_affirmative;
    private RadioButton Rabtn_opposition;
    private RadioButton Rabtn_neutral;
    private String opinion;
    private static int countstar=0;



    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        Toolbar tb = (Toolbar) findViewById(R.id.app_toolbar_writing) ;
        setSupportActionBar(tb) ;

        // Get post key from intent
        mPostKey = getIntent().getStringExtra(EXTRA_POST_KEY);
        mCommentKey = getIntent().getStringExtra(EXTRA_COMMENT_KEY);

        if (mPostKey == null) {
            throw new IllegalArgumentException("Must pass EXTRA_POST_KEY");
        }

        // Initialize Database
        mPostReference = FirebaseDatabase.getInstance().getReference()
                .child("posts").child(mPostKey);
        mCommentsReference = FirebaseDatabase.getInstance().getReference();

        // Initialize Views
        mAuthorView = findViewById(R.id.postAuthor);
        mTitleView = findViewById(R.id.postTitle);
        mBodyView = findViewById(R.id.postBody);
        mDateView = findViewById(R.id.postDate);
        mAffirmativeView = findViewById(R.id.postAffirmative);
        mOppositionView = findViewById(R.id.postOpposition);
        mReferenceView = findViewById(R.id.postReference);
        mCommentField = findViewById(R.id.fieldCommentText);
        mCommentButton = findViewById(R.id.buttonPostComment);
        mCommentButton.setOnClickListener(this);
        radioGroup=findViewById(R.id.radiogroup);
        Rabtn_affirmative = findViewById(R.id.radioButton);
        Rabtn_opposition = findViewById(R.id.radioButton2);
        Rabtn_neutral = findViewById(R.id.radioButton3);

        mCommentsRecycler = findViewById(R.id.recyclerPostComments);
        mCommentButton.setOnClickListener(this);
        mCommentManager = new LinearLayoutManager(this);
        mCommentManager.setReverseLayout(true);
        mCommentManager.setStackFromEnd(true);
        mCommentsRecycler.setLayoutManager(mCommentManager);

        Query commentsQuery = getQuery(mCommentsReference);

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Comment>()
                .setQuery(commentsQuery, Comment.class)
                .build();

        mCommentAdapter=new FirebaseRecyclerAdapter<Comment, CommentViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CommentViewHolder holder, final int position, @NonNull Comment model) {
                final DatabaseReference PostRef=getRef(position);

                final String PostKey=PostRef.getKey();

                // Determine if the current user has liked this post and set UI accordingly
                if (model.stars.containsKey(getUid())) {
                    holder.starView.setImageResource(R.drawable.ic_toggle_star_24);
                } else {
                    holder.starView.setImageResource(R.drawable.ic_toggle_star_outline_24);
                }

                // Bind Post to ViewHolder, setting OnClickListener for the star button
                holder.bindToPost(model, new View.OnClickListener() {

                    @Override
                    public void onClick(View starView) {
                        // Need to write to both places the post is stored
                        DatabaseReference globalCommentRef = mCommentsReference.child("post-comments").child(mPostKey).child(PostKey);
                        // Run two transactions
                        onStarClicked(globalCommentRef);

                    }
                });
            }

            @NonNull
            @Override
            public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                return new CommentViewHolder(inflater.inflate(R.layout.item_comment,parent,false));
            }
        };
        mCommentsRecycler.setAdapter(mCommentAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();

        // Add value event listener to the post
        // [START post_value_event_listener]
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Post post = dataSnapshot.getValue(Post.class);
                // [START_EXCLUDE]
                mAuthorView.setText(post.author);
                mTitleView.setText(post.title);
                mBodyView.setText(post.body);
                mDateView.setText(post.date);
                mAffirmativeView.setText(post.affirmative);
                mOppositionView.setText(post.opposition);
                mReferenceView.setText(post.reference);
                // [END_EXCLUDE]
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                Toast.makeText(ContentActivity.this, "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        };
        mPostReference.addValueEventListener(postListener);
        // [END post_value_event_listener]

        // Keep copy of post listener so we can remove it when app stops
        mPostListener = postListener;

        if(mCommentAdapter != null){
            mCommentAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        // Remove post value event listener
        if (mPostListener != null) {
            mPostReference.removeEventListener(mPostListener);
        }

        if(mCommentAdapter != null){
            mCommentAdapter.stopListening();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mCommentButton) {
            onPostPressed();
        }
    }

    private void submitComment() {
        // Get now date
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date time = new Date();

        final String text = mCommentField.getText().toString();
        final String date=format.format(time);
        // Text is required
        if (TextUtils.isEmpty(text)) {
            mCommentField.setError(REQUIRED);
            return;
        }

        if(Rabtn_affirmative.isChecked()){
            opinion = "찬성";
        }
        if(Rabtn_opposition.isChecked()){
            opinion = "반대";
        }
        if(Rabtn_neutral.isChecked()){
            opinion = "중립";
        }

        if(opinion==null){
            Toast.makeText(this, "찬성/반대/중립 체크를 해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Disable button so there are no multi-posts
        setEditingEnabled(false);

        // [START single_value_read]
        final String userId = getUid();

        mCommentsReference.child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);

                        // [START_EXCLUDE]
                        if (user == null) {
                            // User is null, error out
                            Log.e(TAG, "User " + userId + " is unexpectedly null");
                            Toast.makeText(ContentActivity.this,
                                    "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Write new post
                            writeNewComment(userId, user.username, text, date, opinion);
                            mCommentField.setText(null);
                        }

                        // Finish this Activity, back to the stream
                        setEditingEnabled(true);
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
        mCommentField.setEnabled(enabled);
        if (enabled) {
            mCommentButton.setClickable(true);
        } else {
            mCommentButton.setClickable(false);
        }
    }
    // [START write_fan_out]
    private void writeNewComment(String userId, String author, String text, String date, String opinion) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        String Postkey = mCommentsReference.child("post-comments").child(mPostKey).getKey();
        String Commentkey = mCommentsReference.child("post-comments").push().getKey();

        Comment comment = new Comment(userId, author, text, date, opinion);
        Map<String, Object> commentValues = comment.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/post-comments/" + Postkey + "/" + Commentkey, commentValues);

        mCommentsReference.updateChildren(childUpdates);
    }
    // [END write_fan_out]


    // [START post_stars_transaction]
    private void onStarClicked(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Comment c = mutableData.getValue(Comment.class);
                if (c == null) {
                    return Transaction.success(mutableData);
                }

                if (c.stars.containsKey(getUid())) {
                    // Unstar the post and remove self from stars
                    c.starCount = c.starCount - 1;
                    c.stars.remove(getUid());
                } else {
                    // Star the post and add self to stars
                    c.starCount = c.starCount + 1;
                    c.stars.put(getUid(), true);
                }

                // Set value and report transaction success
                mutableData.setValue(c);
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

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public  Query getQuery(DatabaseReference databaseReference){
        // [START recent_posts_query]
        // Last 100 posts, these are automatically the 100 most recent
        // due to sorting by push() keys
        Query recentPostsQuery = databaseReference.child("post-comments").child(mPostKey);
        // [END recent_posts_query]

        return recentPostsQuery;
    }

    //댓글쓰기 팝업
    public void onPostPressed() {

        // AlertDialog 빌더를 이용해 종료시 발생시킬 창을 띄운다
        AlertDialog.Builder alBuilder = new AlertDialog.Builder(this);
        alBuilder.setMessage("댓글을 쓰시겠습니까? \n한번 쓴 댓글은 수정할 수 없습니다.");

        // "예" 버튼을 누르면 실행되는 리스너
        alBuilder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                submitComment();
            }
        });
        // "아니오" 버튼을 누르면 실행되는 리스너
        alBuilder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return; // 아무런 작업도 하지 않고 돌아간다
            }
        });
        alBuilder.setTitle("댓글 쓰기");
        alBuilder.show(); // AlertDialog.Bulider로 만든 AlertDialog를 보여준다.
    }

    //뒤로가기 종료 팝업
    @Override
    public void onBackPressed() {

        // AlertDialog 빌더를 이용해 종료시 발생시킬 창을 띄운다
        AlertDialog.Builder alBuilder = new AlertDialog.Builder(this);
        alBuilder.setMessage("메인화면으로 가시겠습니까?\n작성중이던 댓글은 저장되지 않습니다.");

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
        alBuilder.setTitle("메인화면 가기");
        alBuilder.show(); // AlertDialog.Bulider로 만든 AlertDialog를 보여준다.
    }

}
