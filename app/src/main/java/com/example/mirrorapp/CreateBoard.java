package com.example.mirrorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateBoard extends AppCompatActivity{

    private EditText mcreatetitleofboard, mcreacontantofboard;
    private FloatingActionButton msaveboard;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_board);

        msaveboard = findViewById(R.id.saveboard);
        //msaveboard.setOnClickListener(this);
        mcreatetitleofboard = findViewById(R.id.createtitleofboard);
        mcreacontantofboard = findViewById(R.id.createcontentofboard);

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarofcreateboard);
        /*setSupportActionBar(toolbar);*/
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        msaveboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = mcreatetitleofboard.getText().toString();
                String content = mcreacontantofboard.getText().toString();
                if(title.isEmpty() || content.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Both field are requiar!", Toast.LENGTH_SHORT);
                }else{
                    DocumentReference documentReference = firebaseFirestore.collection("boards").document(firebaseUser.getUid())
                            .collection("myBoards")
                            .document();
                    Map<String, Object> board = new HashMap<>();
                    board.put("title", title);
                    board.put("content", content);

                    documentReference.set(board).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(CreateBoard.this, "board create secssesfuly!", Toast.LENGTH_SHORT);
                            startActivity(new Intent(CreateBoard.this, FeelingsBoardActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CreateBoard.this, "Fail to create board!", Toast.LENGTH_SHORT);
                        }
                    });
                }
            }
        });
    }

    //@Override
    /*public boolean onOptionsItemSelected(@NonNull MenuItem item){

        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }*/

    //@Override
    /*public void onClick(View v) {
        switch (v.getId()){
            case R.id.saveboard:
                saveboard();
                break;
        }
    }

    public void saveboard(){
        String title = mcreatetitleofboard.getText().toString();
        String content = mcreacontantofboard.getText().toString();
        if(title.isEmpty() || content.isEmpty()) {
            Toast.makeText(CreateBoard.this, "both field are requiar!", Toast.LENGTH_SHORT);
        }else{
            DocumentReference documentReference = firebaseFirestore.collection("boards").document(firebaseUser.getUid())
                    .collection("myBoards")
                    .document();
            Map<String, Object> board = new HashMap<>();
            board.put("tite", title);
            board.put("content", content);

            documentReference.set(board).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(CreateBoard.this, "board create secssesfuly!", Toast.LENGTH_SHORT);
                    startActivity(new Intent(CreateBoard.this, FeelingsBoardActivity.class));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(CreateBoard.this, "Fail to create board!", Toast.LENGTH_SHORT);
                }
            });
        }
    }*/
}