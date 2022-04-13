package com.example.mirrorapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.fragment.app.DialogFragment;

public class EmogiFrefment extends DialogFragment {

    int[] images = {R.drawable.ic_hygge};
    GridView mgridview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View rootview = inflater.inflate(R.layout.sticker_dialog,null);

        mgridview = (GridView) rootview.findViewById(R.id.gridview);

        getDialog().setTitle("Emoji");
        CustomAdapter adapter = new CustomAdapter(images, getActivity());
        mgridview.setAdapter(adapter);
        return rootview;
    }
}
