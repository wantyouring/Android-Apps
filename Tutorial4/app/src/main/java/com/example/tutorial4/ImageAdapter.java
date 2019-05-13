package com.example.tutorial4;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageAdapter extends ArrayAdapter<String> {

    public ImageAdapter(Context context, String[] items) {
        super(context, R.layout.image_layout, items);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater imageInflater = LayoutInflater.from(getContext()); // layoutinflater : XML에 정의된 resource들을 view형태로 반환.
        View view = imageInflater.inflate(R.layout.image_layout, parent, false);
        String item = getItem(position); //해당 position의 item string객체 생성.
        TextView textView = (TextView)view.findViewById(R.id.textView); //textView객체 생성
        ImageView imageView = (ImageView)view.findViewById(R.id.imageView); //ImageView객체 생성
        textView.setText(item); //item의 string으로 textView에 쓰기
        imageView.setImageResource(R.mipmap.ic_launcher);
        return view;
    }
}
