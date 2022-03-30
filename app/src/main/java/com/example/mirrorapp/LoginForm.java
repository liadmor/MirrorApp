package com.example.mirrorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginForm extends AppCompatActivity implements View.OnClickListener {

    private TextView login, register;
    private EditText editTextEmail, editTextPassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_form);
        getSupportActionBar().hide(); //hide the title bar


        register = (Button) findViewById(R.id.register);
        register.setOnClickListener(this);

        login = (Button) findViewById(R.id.button_login);
        login.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        //if user already login
        if(firebaseUser != null){
            finish();
            startActivity(new Intent(LoginForm.this, FeelingsBoardActivity.class ));
        }

        editTextEmail = (EditText) findViewById(R.id.et_username);
        editTextPassword = (EditText) findViewById(R.id.et_password);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register:
                startActivity(new Intent(this, RegistrationForm.class));
                break;
            case R.id.button_login:
                loginUser();
        }
    }

    private void loginUser(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(email.isEmpty()){
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }

        if(password.length() < 6 ){
            editTextPassword.setError("minimum 6 character");
            editTextPassword.requestFocus();
            return;
        }

        //new
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    checkmailverification();
                }else{
                    Toast.makeText(LoginForm.this, "Account does not exist!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkmailverification(){
        FirebaseUser firebaseUser= mAuth.getCurrentUser();
        if(firebaseUser.isEmailVerified() == true){
            Toast.makeText(LoginForm.this, "LogIn!", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(LoginForm.this, FeelingsBoardActivity.class));
        }else{
            Toast.makeText(LoginForm.this, "Fail to login!", Toast.LENGTH_SHORT).show();
            mAuth.signOut();
        }
    }
}