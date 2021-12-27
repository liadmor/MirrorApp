package com.example.mirrorapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class DataTableActivity extends AppCompatActivity {

    ArrayList<String> numberList = new ArrayList<>();
    ArrayList<String> quoteList = new ArrayList<>();
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_table);
        get_json();
        recyclerView = findViewById(R.id.dataJsonView);
        DataTableAdopter dataTableAdopter= new DataTableAdopter(this,numberList, quoteList);
        recyclerView.setAdapter(dataTableAdopter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void get_json()
    {
        String json;
        try {
            InputStream is = getAssets().open("Test1.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            json = new String(buffer,"UTF-8");
            JSONArray jsonArray = new JSONArray(json);

            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject obj = jsonArray.optJSONObject(i);
                numberList.add(obj.getString("time"));
                quoteList.add(obj.getString("quote"));
            }
        }
        catch (IOException | JSONException e)
        {
            e.printStackTrace();
        }
    }
}