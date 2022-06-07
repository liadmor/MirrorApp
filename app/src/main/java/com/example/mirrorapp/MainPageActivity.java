package com.example.mirrorapp;

import static java.util.Collections.list;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.util.ArrayUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class MainPageActivity extends AppCompatActivity implements View.OnLongClickListener {
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText emotion;
    private TextView add_button;
    private ImageView exit;
    private Stack<String> emotions = new Stack<String>();


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
        ImageView proceed = findViewById(R.id.arrow);
        RelativeLayout targetImageView = findViewById(R.id.feelings_board);
        Button add_emotion = findViewById(R.id.plus);


        add_emotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEmotionDialog();
            }
        });

        targetImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent boardIntent = new Intent(MainPageActivity.this, CreateBoard.class);
                int i=0;

                while (!emotions.isEmpty()){
                    boardIntent.putExtra("emotion" + i, emotions.pop());
                    i++;
                }

                boardIntent.putExtra("count", String.valueOf(i));
                startActivity(boardIntent);


            }
        });

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent boardIntent = new Intent(MainPageActivity.this, CreateBoard.class);
                int i=0;

                while (!emotions.isEmpty()){
                    boardIntent.putExtra("emotion" + i, emotions.pop());
                    i++;
                }

                boardIntent.putExtra("count", String.valueOf(i));
                startActivity(boardIntent);

            }
        });

        ArrayList<Button>  feelingsButtons = getButtons(feelingsView);

        for(int i=0; i<feelingsButtons.size();i++){
            feelingsButtons.get(i).setOnLongClickListener(this);
        }

        ImageView resetFeelings = findViewById(R.id.undo);
        resetFeelings.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                targetImageView.removeAllViewsInLayout();
                targetImageView.addView(proceed);
            }
        });


//        b.setOnLongClickListener(this);


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

                        Button newButton = new Button(MainPageActivity.this, null, R.style.feelingsChosen , R.style.feelingsChosen);
                        newButton.setText(buttonText.toString());
                        emotions.push(buttonText.toString());
                        targetImageView.addView(newButton);
                        newButton.setX(e.getX());
                        newButton.setY(e.getY());

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

    public void createEmotionDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View addEmotionView = getLayoutInflater().inflate(R.layout.add_emotion, null);

//

        dialogBuilder.setView(addEmotionView);
        dialog = dialogBuilder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        emotion = (EditText) dialog.findViewById(R.id.emotionadd);
        add_button = dialog.findViewById(R.id.addButton);
        exit = dialog.findViewById(R.id.exit);

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence buttonText = emotion.getText();


                Button newButton = new Button(MainPageActivity.this, null, R.style.feelingsChosen , R.style.feelingsChosen);
                newButton.setText(buttonText.toString());
                RelativeLayout targetImageView = findViewById(R.id.feelings_board);
                targetImageView.addView(newButton);
                newButton.setX(200);
                newButton.setY(50);
                emotions.push(buttonText.toString());
                dialog.dismiss();

            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });




    }
}

