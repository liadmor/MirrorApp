package com.example.mirrorapp;

import static android.widget.Toast.makeText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationForm extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;

    private EditText editTextFullName, editTextAge, editTextEmail, editTextPassword;
    private TextView banner, registerUser, login;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registar_user);
        getSupportActionBar().hide(); //hide the title bar

        mAuth = FirebaseAuth.getInstance();

        banner = (TextView) findViewById(R.id.banner);
        banner.setOnClickListener(this);

        registerUser = (Button) findViewById(R.id.registerUser);
        registerUser.setOnClickListener(this);

         editTextFullName = (EditText) findViewById(R.id.fullName);
         editTextAge = (EditText) findViewById(R.id.age);
         editTextEmail = (EditText) findViewById(R.id.email);
         editTextPassword = (EditText) findViewById(R.id.password);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.banner:
                startActivity(new Intent(this, RegistrationForm.class));
                break;
            case R.id.registerUser:
                registerUser();
                break;
        }
    }

    private void registerUser(){
        String email = editTextEmail.getText().toString().trim();
        String age = editTextAge.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String name = editTextFullName.getText().toString().trim();

        if(name.isEmpty()){
            editTextFullName.setError("full name is required");
            editTextFullName.requestFocus();
            return;
        }

        if(age.isEmpty()){
            editTextAge.setError("Age is required");
            editTextAge.requestFocus();
            return;
        }

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

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            sendVerificationEmail();
                            User user = new User(name, age, email);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user);
                        }else{
                            Toast.makeText(RegistrationForm.this, "Failed to register!!!!!!!!!!", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void sendVerificationEmail(){
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if(firebaseUser != null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(RegistrationForm.this, "Verification email is sent, Verify and log in again", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    mAuth.signOut();
                    finish();
                    startActivity(new Intent(RegistrationForm.this, RegistrationForm.class));
                }
            });
        }else{
            Toast.makeText(RegistrationForm.this, "fail to sent a verification email", Toast.LENGTH_SHORT).show();

        }
    }

}