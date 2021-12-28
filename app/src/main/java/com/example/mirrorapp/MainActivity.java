package com.example.mirrorapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonParser;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;


public class MainActivity extends AppCompatActivity {


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button publish = findViewById(R.id.publishButt);
        Button history = findViewById(R.id.history);

        publish.setOnClickListener(v -> {
            TextView dataView = findViewById(R.id.curFeeling);
            String data = dataView.getText().toString();
            try {
                writeDataToJson(data);
                Intent i = new Intent(getApplicationContext(),DataTableActivity.class);
                startActivity(i);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        history.setOnClickListener(v -> {
            Intent i = new Intent(getApplicationContext(),DataTableActivity.class);
            startActivity(i);
        });
    }


    private void writeToFile(String data, String path) {
        try {
            OutputStream outputStream = new FileOutputStream(path);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
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
            if (!histFile.isFile()){
                histFile.createNewFile();
                jsonData = "";
            }
            else{
                is = new FileInputStream(histFile);
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                jsonData = new String(buffer,"UTF-8");
            }


            if (jsonData.length()==0){
                jsonArray = new JSONArray();
            }
            else{
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