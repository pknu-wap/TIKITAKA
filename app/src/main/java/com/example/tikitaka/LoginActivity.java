package com.example.tikitaka;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class LoginActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "LoginActivity";
    private EditText id_login,pw_login;
    private TextView btn_signup,btn_find,textviewMessage;
    private Button btn_login;
    //ProgressDialog progressDialog;
    //define firebase object
    FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //initializig firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference();

        id_login=findViewById(R.id.id_login);
        pw_login=findViewById(R.id.pw_login);
        btn_find=findViewById(R.id.btn_find);
        textviewMessage=findViewById(R.id.textviewMessage);
        btn_signup = findViewById(R.id.btn_signup);
        btn_login=findViewById(R.id.btn_login);

        btn_login.setOnClickListener(this);
        btn_signup.setOnClickListener(this);
        btn_find.setOnClickListener(this);

        // Check auth on Activity start
        if (firebaseAuth.getCurrentUser() != null) {
            onAuthSuccess(firebaseAuth.getCurrentUser());
        }

    }

    private void signIn() {
        Log.d(TAG, "signIn");
        if (!validateForm()) {
            return;
        }

        showProgressDialog();
        String email = id_login.getText().toString();
        String password = pw_login.getText().toString();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signIn:onComplete:" + task.isSuccessful());
                        hideProgressDialog();

                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser());
                        } else {
                            Toast.makeText(LoginActivity.this, "Sign In Failed",
                                    Toast.LENGTH_SHORT).show();
                            textviewMessage.setText("로그인 실패 유형\n - password가 맞지 않습니다.\n -서버에러");
                        }
                    }
                });
    }

    private void onAuthSuccess(FirebaseUser user) {
        String username = usernameFromEmail(user.getEmail());

        // Write new user
        writeNewUser(user.getUid(), username, user.getEmail());

        // Go to MainActivity
        startActivity(new Intent(LoginActivity.this, PostListActivity.class));
        finish();
    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(id_login.getText().toString())) {
            id_login.setError("Required");
            result = false;
        } else {
            id_login.setError(null);
        }

        if (TextUtils.isEmpty(pw_login.getText().toString())) {
            pw_login.setError("Required");
            result = false;
        } else {
            pw_login.setError(null);
        }

        return result;
    }

    // [START basic_write]
    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);

        mDatabase.child("users").child(userId).setValue(user);
    }
    // [END basic_write]

    @Override
    public void onClick(View view) {

        if(view == btn_login) {
            signIn();
        }
        if(view == btn_signup) {
            finish();
            startActivity(new Intent(this, SignupActivity.class));
        }
        if(view == btn_find) {
            finish();
            startActivity(new Intent(this, FindActivity.class));
        }
    }
}
