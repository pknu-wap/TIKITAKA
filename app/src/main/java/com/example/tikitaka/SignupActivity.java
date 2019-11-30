package com.example.tikitaka;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tikitaka.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "SignupActivity";

    Button btn_signup_complete;
    EditText id_signup,pw_signup;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    TextView textviewMessage;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //initializing firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        id_signup=findViewById(R.id.id_signup);
        pw_signup = (EditText)findViewById(R.id.pw_signup);
        btn_signup_complete = findViewById(R.id.btn_signup_complete);
        progressDialog=new ProgressDialog(this);
        textviewMessage=findViewById(R.id.textviewMessage);

        btn_signup_complete.setOnClickListener(this);

    }

    private void signUp() {
        Log.d(TAG, "signUp");
        if (!validateForm()) {
            return;
        }

        showProgressDialog();
        String email = id_signup.getText().toString();
        String password = pw_signup.getText().toString();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUser:onComplete:" + task.isSuccessful());
                        hideProgressDialog();

                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser());
                        } else {
                            textviewMessage.setText("에러유형\n - 이미 등록된 이메일  \n -암호 최소 6자리 이상 \n - 서버에러");
                            Toast.makeText(SignupActivity.this, "Sign Up Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void onAuthSuccess(FirebaseUser user) {
        String username = usernameFromEmail(user.getEmail());

        // Write new user
        writeNewUser(user.getUid(), username, user.getEmail());

        // Go to MainActivity
        startActivity(new Intent(SignupActivity.this, PostListActivity.class));
        finish();
    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    //정상동작 함
    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(id_signup.getText().toString())) {
            id_signup.setError("Required");
            result = false;
        } else {
            id_signup.setError(null);
        }

        if (TextUtils.isEmpty(pw_signup.getText().toString())) {
            pw_signup.setError("Required");
            result = false;
        } else {
            pw_signup.setError(null);
        }

        return result;
    }

    // [START basic_write]
    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);

        mDatabase.child("users").child(userId).setValue(user);
    }
    // [END basic_write]

    //button click event
    @Override
    public void onClick(View view){
        if(view==btn_signup_complete){
            signUp();
        }
    }
}
