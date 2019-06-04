package com.example.chat_practice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<ChatData> {

    TextView tv_name;
    TextView tv_message;
    TextView tv_time;

    public ListAdapter(Context context, ArrayList<ChatData> datas) {
        super(context, R.layout.list_view,datas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater imageInflater = LayoutInflater.from(getContext());
        View view = imageInflater.inflate(R.layout.list_view,parent,false);
        ChatData data = getItem(position);

        tv_name = view.findViewById(R.id.name);
        tv_message = view.findViewById(R.id.message);
        tv_time = view.findViewById(R.id.time);

        tv_name.setText(data.getUserName());
        tv_message.setText(data.getMessage());
        tv_time.setText(data.getTime());

        return view;
    }

}
