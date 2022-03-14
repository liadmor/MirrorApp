package com.example.mirrorapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class FeelingsBoard extends AppCompatActivity implements View.OnClickListener{

    private TextView publish, history, dataView;
    private String data;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feelings_board);

        publish = (Button) findViewById(R.id.publishButt);
        publish.setOnClickListener(this);
        history = (Button) findViewById(R.id.history);
        history.setOnClickListener(this);
        dataView = findViewById(R.id.curFeeling);
        data = dataView.getText().toString();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();


        /*publish.setOnClickListener(v -> {
            TextView dataView = findViewById(R.id.curFeeling);
            data = dataView.getText().toString();
            try {
                writeDataToJson(data);
                Intent i = new Intent(getApplicationContext(), DataTableActivity.class);
                startActivity(i);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        history.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(), DataTableActivity.class);
            startActivity(i);
        });*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.publishButt:
                writeNewPost(mAuth.getUid(), mAuth.getCurrentUser().toString(), "first", data);
                break;
            case R.id.history:

                break;
        }
    }

    private void writeNewPost(String userId, String username, String title, String body) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        String key = mDatabase.child("posts").push().getKey();
        Post post = new Post(userId, username, title, body);
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + key, postValues);
        childUpdates.put("/user-posts/" + userId + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
    }


    private void writeToFile(String data, String path) {
        try {
            OutputStream outputStream = new FileOutputStream(path);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            System.out.println("Exception:" + "File write failed: " + e.toString());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void writeDataToJson(String data) throws IOException {
        JSONArray jsonArray;
        JSONObject json;
        InputStream is = null;
        String jsonData;

        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();
        String filePath = String.valueOf(Paths.get(getFilesDir().toString(), getString(R.string.json_file_name)));

        try {

            File histFile = new File(filePath);
            if (!histFile.isFile()) {
                histFile.createNewFile();
                jsonData = "";
            } else {
                is = new FileInputStream(histFile);
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                jsonData = new String(buffer, "UTF-8");
            }


            if (jsonData.length() == 0) {
                jsonArray = new JSONArray();
            } else {
                jsonArray = new JSONArray(jsonData);
            }
            json = new JSONObject();
            json.put("time", ts);
            json.put("quote", data);
            jsonArray.put(json);
            writeToFile(jsonArray.toString(), filePath);

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

}