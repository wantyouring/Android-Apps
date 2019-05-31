package com.example.crawl_practice;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ScrapListAdapter extends ArrayAdapter<String> {
    public ScrapListAdapter(Context context, ArrayList<String> items) {
        super(context, R.layout.list_layout,items);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater imageInflater = LayoutInflater.from(getContext()); // layoutinflater : XML에 정의된 resource들을 view형태로 반환.
        View view = imageInflater.inflate(R.layout.list_layout_scrap, parent, false);
        String item = getItem(position); //해당 position의 item string객체 생성.

        TextView tv_title = view.findViewById(R.id.title);
        TextView tv_date = view.findViewById(R.id.date);
        ImageView imageView = view.findViewById(R.id.imageView);

        //items에서 (분야 0,날짜 1,제목 2,링크 3)구분해 처리
        String[] parsed = item.split("&&&");
        //분야 : 해당 분야 이미지 가져오기
        //구현필요@@
        //날짜
        tv_date.setText(parsed[1]);
        //제목
        tv_title.setText(parsed[2]);
        //링크


        //-----------구분선-----------
        /*
        String imageLink = item.split("wantyouring")[1];
        item = item.split("wantyouring")[0];

        textView.setText(item); //item의 string으로 textView에 쓰기
        // 기사 미리보기 이미지 가져오기.
        if(!imageLink.contains("https"))
            imageView.setImageResource(R.drawable.cat);
        else {
            Picasso.with(MainActivity.context)
                    .load(imageLink)
                    .into(imageView);
        }
        //imageView.setImageResource(R.mipmap.ic_launcher);
        */
        return view;
    }
}
