package com.example.tikitaka;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText id_login,pw_login;
    private TextView btn_signup,btn_find,textviewMessage;
    private Button btn_login;
    ProgressDialog progressDialog;
    //define firebase object
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        id_login=findViewById(R.id.id_login);
        pw_login=findViewById(R.id.pw_login);
        btn_find=findViewById(R.id.btn_find);
        textviewMessage=findViewById(R.id.textviewMessage);
        btn_signup = findViewById(R.id.btn_signup);
        btn_login=findViewById(R.id.btn_login);

        btn_login.setOnClickListener(this);
        btn_signup.setOnClickListener(this);
        btn_find.setOnClickListener(this);

        //initializig firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
    }

    //firebase userLogin method
    private void userLogin(){
        String email = id_login.getText().toString().trim();
        String password = pw_login.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "email을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "password를 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("로그인중입니다. 잠시 기다려 주세요...");
        progressDialog.show();

        //logging in the user
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()) {
                            finish();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                            Toast.makeText(getApplicationContext(), "로그인 실패!", Toast.LENGTH_LONG).show();
                            textviewMessage.setText("로그인 실패 유형\n - password가 맞지 않습니다.\n -서버에러");
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        if(view == btn_login) {
            userLogin();
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
