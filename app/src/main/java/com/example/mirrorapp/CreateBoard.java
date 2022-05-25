package com.example.mirrorapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.xiaopo.flying.sticker.Sticker;
import com.xiaopo.flying.sticker.StickerView;
import com.xiaopo.flying.sticker.TextSticker;

import java.util.HashMap;
import java.util.Map;

public class CreateBoard extends AppCompatActivity{

    private EditText mcreatetitleofboard, mcreacontantofboard;
    private Button msaveboard;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;
    private DrawView paint;
    private ImageButton madddraw, maddtext, maddemoji, maddPic;
    public final int CATEGORY_ID =0;
    private Dialog dialog;
    private ImageView maddphoto;

    private StickerView mstickerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_board);

        msaveboard = findViewById(R.id.saveboard);
        mcreatetitleofboard = findViewById(R.id.createtitleofboard);
        mcreacontantofboard = findViewById(R.id.createcontentofboard);

        paint = (DrawView) findViewById(R.id.draw_view);
        paint.setColor(Color.RED);
        ViewTreeObserver vto = paint.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                paint.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int width = paint.getMeasuredWidth();
                int height = paint.getMeasuredHeight();
                paint.init(height, width);
            }
        });

        madddraw = findViewById(R.id.addDraw);
        madddraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paint.setVisibility(View.VISIBLE);
            }
        });
        maddtext = findViewById(R.id.addText);
        maddtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mcreacontantofboard.setVisibility(View.VISIBLE);
            }
        });

        final FragmentManager fragmentManager = getSupportFragmentManager();
        final EmogiFrefment p = new EmogiFrefment();

        maddemoji = findViewById(R.id.addEmoji);
        maddemoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p.show(fragmentManager, "Emoji");
            }
        });

        maddphoto = findViewById(R.id.add_photo);
        if (ContextCompat.checkSelfPermission(CreateBoard.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(CreateBoard.this, new String[]{
                    Manifest.permission.CAMERA}, 100);
        }

        maddPic = findViewById(R.id.addPic);
        maddPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 100);
            }
        });


//        mstickerView = (StickerView) findViewById(R.id.stickerView);
//        TextSticker sticker = new TextSticker(this);
//        sticker.setDrawable(ContextCompat.getDrawable(getApplicationContext(),
//                R.drawable.sticker_transparent_background));
//        sticker.setText("Hello, world!");
//        sticker.setTextColor(Color.BLACK);
//        sticker.setTextAlign(Layout.Alignment.ALIGN_CENTER);
//        sticker.resizeText();
//        mstickerView.addSticker(sticker);

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
                if (title.isEmpty() || content.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Both field are requiar!", Toast.LENGTH_SHORT);
                } else {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            maddphoto.setImageBitmap(bitmap);
        }
    }
}