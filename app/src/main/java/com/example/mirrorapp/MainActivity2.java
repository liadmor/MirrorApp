package com.example.mirrorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.xiaopo.flying.sticker.StickerView;

public class MainActivity2 extends AppCompatActivity {

    private GridView mgridview;

    private StickerView mstickerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sticker_dialog);

//        mgridview = findViewById(R.id.gridview);
//        int[] images = {R.drawable.ic_hygge};
//
//        CreateBoard.CustomAdapter customAdapter = new CreateBoard.CustomAdapter(images, this);
//        mgridview.setAdapter(customAdapter);
    }



}

