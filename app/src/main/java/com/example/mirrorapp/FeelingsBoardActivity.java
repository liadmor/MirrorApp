package com.example.mirrorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class FeelingsBoardActivity extends AppCompatActivity {

    FloatingActionButton mcreateboard;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String mUid;
    private DatabaseReference userDatabase;

    RecyclerView mrecycleview;
    TextView welcome;
    StaggeredGridLayoutManager staggeredGridLayoutManager;

    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;

    FirestoreRecyclerAdapter<FirebaseModel, BoardViewHolder> boardAdapter;
    FirestoreRecyclerAdapter<FirebaseModel, BoardViewHolder> emptyBoardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feelings_main);

        mcreateboard = findViewById(R.id.createnewboard);
//        welcome = findViewById(R.id.welcome);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();


        getSupportActionBar().setTitle("HYGGE");

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mUid = mAuth.getCurrentUser().getUid();
        userDatabase = FirebaseDatabase.getInstance().getReference("Users");

//        welcome.setText("Hey " + mAuth.getCurrentUser().getDisplayName());



        mcreateboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FeelingsBoardActivity.this, CreateBoard.class));
            }
        });

        Query query = firebaseFirestore.collection("boards").document(firebaseUser.getUid()).collection("myBoards")
                .orderBy("title", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<FirebaseModel> alluserboards = new FirestoreRecyclerOptions.Builder<FirebaseModel>()
                .setQuery(query, FirebaseModel.class)
                .build();


        boardAdapter = new FirestoreRecyclerAdapter<FirebaseModel, BoardViewHolder>(alluserboards){
            @NonNull
            @Override
            public BoardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.board_layout, parent, false);
                return new BoardViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull BoardViewHolder boardViewHolder, int i, @NonNull FirebaseModel firebasemodel) {
                boardViewHolder.boardtitle.setText(firebasemodel.getTitle());
                boardViewHolder.boardcontent.setText(firebasemodel.getContent());
            }
        };

        mrecycleview = findViewById(R.id.recyclerview);
        mrecycleview.setHasFixedSize(true);
        mrecycleview.setLayoutManager(new LinearLayoutManager(this));
        mrecycleview.setAdapter(boardAdapter);
    }

    public void feelingClicked(View v){
        if(v.isActivated()){
            v.setBackgroundColor(Color.parseColor("#FF6200EE"));
            v.setActivated(false);
        }
        else{
            v.setBackgroundColor(Color.GRAY);
            v.setActivated(true);
        }
    }

    public class BoardViewHolder extends RecyclerView.ViewHolder{

        private TextView boardtitle;
        private TextView boardcontent;
        LinearLayout mboard;

        public BoardViewHolder(@NonNull View itemView) {
            super(itemView);
            boardtitle = itemView.findViewById(R.id.boardtitle);
            boardcontent = itemView.findViewById(R.id.boardcontent);
            mboard = itemView.findViewById(R.id.board);
        }
    }

    @Override
        public boolean onCreateOptionsMenu(Menu menu){
            getMenuInflater().inflate(R.menu.menu, menu);
            return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        switch (item.getItemId()){
            case R.id.logout:
                mAuth.signOut();
                finish();
                startActivity(new Intent(FeelingsBoardActivity.this, LoginForm.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {

        super.onStart();
        boardAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(boardAdapter != null){
            boardAdapter.startListening();
        }
    }
}