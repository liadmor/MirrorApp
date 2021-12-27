package com.example.mirrorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;


import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button publish = findViewById(R.id.publishButt);
        publish.setOnClickListener(v -> {
            TextView dataView = findViewById(R.id.curFeeling);
            String data = dataView.getText().toString();
            try {
                writeDataToJson(data);

//                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
//                    startActivity(i);

            } catch (IOException e) {
                e.printStackTrace();
            }


        });
    }

    private void writeDataToJson(String data) throws IOException {
        JSONObject json = new JSONObject();
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();

        try (FileOutputStream fos = openFileOutput(getString(R.string.json_file_name), MODE_APPEND)) {
            json.put(ts, data);
            fos.write(json.toString().getBytes());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

}