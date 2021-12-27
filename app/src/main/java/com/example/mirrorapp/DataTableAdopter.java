package com.example.mirrorapp;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DataTableAdopter extends RecyclerView.Adapter<DataTableAdopter.MyViewHolder> {
    ArrayList<String> m_time;
    ArrayList<String> m_qoute;
    Context context;

    public DataTableAdopter(Context ct, ArrayList<String> time, ArrayList<String> qoute){
        context = ct;
        m_time = time;
        m_qoute = qoute;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.data_table_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.time.setText(m_time.get(position));
        holder.qoute.setText(m_qoute.get(position));
    }

    @Override
    public int getItemCount() {
        return m_time.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView time;
        TextView qoute;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.textView);
            qoute = itemView.findViewById(R.id.textView);
        }
    }
}