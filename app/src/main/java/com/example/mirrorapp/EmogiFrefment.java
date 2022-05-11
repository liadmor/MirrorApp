package com.example.mirrorapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.fragment.app.DialogFragment;

public class EmogiFrefment extends DialogFragment {

    int[] images = {R.drawable.ic_dizzy_face__1f635_, R.drawable.ic_face_blowing_a_kiss__1f618_, R.drawable.ic_face_with_tears_of_joy__1f602_, R.drawable.ic_hugging_face__1f917_,
            R.drawable.ic_kissing_face_with_closed_eyes__1f61a_};
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
