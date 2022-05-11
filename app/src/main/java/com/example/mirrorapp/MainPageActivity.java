package com.example.mirrorapp;

import static java.util.Collections.list;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.DragAndDropPermissions;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.util.ArrayUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;

public class MainPageActivity extends AppCompatActivity implements View.OnLongClickListener {

    @Override
    public boolean onLongClick(View v) {
        ClipData.Item item = new ClipData.Item((CharSequence) v.getTag());
        ClipData dragData = new ClipData(
                (CharSequence) v.getTag(),
                new String[] { ClipDescription.MIMETYPE_TEXT_PLAIN },
                item);
        View.DragShadowBuilder myShadow = new View.DragShadowBuilder(v);
        v.startDrag(dragData,  // The data to be dragged
                myShadow,  // The drag shadow builder
                null,      // No need to use local data
                0          // Flags (not currently used, set to 0)
        );

        Log.v("DRAG", "button long pressed --> " + v.getTag());
        return true;
    }

    private ArrayList<Button> getButtons(ViewGroup root){
        ArrayList<Button> feelings=new ArrayList<>();
        for(int i=0;i<root.getChildCount();i++){
            View v=root.getChildAt(i);
            if(v instanceof Button && v.getTag() != "+"){
                feelings.add((Button)v);
            }else if(v instanceof ViewGroup){
                feelings.addAll(getButtons((ViewGroup)v));
            }
        }
        return feelings;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        HorizontalScrollView feelingsView =  findViewById(R.id.feelings_buttons);

        ArrayList<Button>  feelingsButtons = getButtons(feelingsView);

        for(int i=0; i<feelingsButtons.size();i++){
            feelingsButtons.get(i).setOnLongClickListener(this);
        }

        ImageView resetFeelings = findViewById(R.id.undo);
        resetFeelings.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                RelativeLayout feelingsLayout = findViewById(R.id.feelings_board);
                feelingsLayout.removeAllViewsInLayout();
            }
        });


//        b.setOnLongClickListener(this);
        RelativeLayout targetImageView = findViewById(R.id.feelings_board);

        targetImageView.setOnDragListener(new View.OnDragListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public boolean onDrag(View v, DragEvent e) {
                switch(e.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        if (e.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                            v.invalidate();
                            return true;
                        }

                        return false;

                    case DragEvent.ACTION_DRAG_ENTERED:

                    case DragEvent.ACTION_DRAG_EXITED:

                    case DragEvent.ACTION_DRAG_ENDED:
                        v.invalidate();
                        return true;

                    case DragEvent.ACTION_DRAG_LOCATION:
                        return true;

                    case DragEvent.ACTION_DROP:
                        ClipData.Item buttonItem = e.getClipData().getItemAt(0);
                        CharSequence buttonText = buttonItem.getText();
                        DragAndDropPermissions dropPermissions =
                            requestDragAndDropPermissions(e);
//
                        Button newButton = new Button(MainPageActivity.this, null, R.style.feelingsChosen , R.style.feelingsChosen);
                        newButton.setText(buttonText.toString());
//
                        targetImageView.addView(newButton);
                        newButton.setX(e.getX());
                        newButton.setY(e.getY());
//
                        v.invalidate();

                        return true;

                    default:
                        Log.e("DragDrop Example","Unknown action type received by View.OnDragListener.");
                        break;
                }

                return false;

            }
        });

    }


}