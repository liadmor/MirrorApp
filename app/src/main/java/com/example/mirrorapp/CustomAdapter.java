package com.example.mirrorapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class CustomAdapter extends BaseAdapter {

    private int[] imagePhoto;
    private Context context;

    public CustomAdapter(int[] imagePhoto, Context context){
        this.imagePhoto = imagePhoto;
        this.context=context;
    }

    @Override
    public int getCount() {
        return imagePhoto.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
//                convertView = layoutInflater.inflate(R.layout.row_stickers, parent, false);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.row_stickers, null);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
        imageView.setImageResource(imagePhoto[position]);
        return convertView;
    }
}
