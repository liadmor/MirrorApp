package com.example.mirrorapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DataTableActivity extends AppCompatActivity {

    ArrayList<String> numberList = new ArrayList<>();
    ArrayList<String> quoteList = new ArrayList<>();
    RecyclerView recyclerView;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_table);
        get_json();
        recyclerView = findViewById(R.id.dataJsonView);
        DataTableAdapter dataTableAdapter = new DataTableAdapter(this,numberList, quoteList);
        recyclerView.setAdapter(dataTableAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void get_json()
    {
        String json;
        try {
            String filePath = String.valueOf(Paths.get(getFilesDir().toString(), getString(R.string.json_file_name)));
            File file = new File(filePath);
            InputStream is = new FileInputStream(file);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            json = new String(buffer,"UTF-8");
            JSONArray jsonArray = new JSONArray(json);

            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject obj = jsonArray.optJSONObject(i);
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM");
                String dateString = formatter.format(new Date(Long.parseLong(obj.getString("time"))));
                numberList.add(dateString);
                quoteList.add(obj.getString("quote"));
            }
        }
        catch (IOException | JSONException e)
        {
            e.printStackTrace();
        }
    }
}