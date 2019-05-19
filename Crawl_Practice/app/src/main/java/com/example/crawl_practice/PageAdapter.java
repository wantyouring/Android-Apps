package com.example.crawl_practice;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class PageAdapter extends PagerAdapter {

    private LayoutInflater inflater;
    private Context context;
    String items[] = new String[60];
    String links[] = new String[60];
    TextView forTest;

    //생성자
    public PageAdapter(Context context, ArrayList<String> items,  ArrayList<String> links, TextView forTest) {
        this.context = context;
        for(int i=0;i<60;i++) {
            this.items[i] = items.get(i);
            this.links[i] = links.get(i);
        }
        this.forTest = forTest;
    }

    //PageAdapter implements
    @Override
    public int getCount() {
        return 6;   //정치, 경제, 사회, 생활/문화, 세계, IT/과학
    }

    //PageAdapter implements
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == (LinearLayout) o;
    }

    //page생성
    @Override
    public Object instantiateItem(final ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View v = inflater.inflate(R.layout.slider, container, false);
        ListView listViewOfArticle = (ListView)v.findViewById(R.id.listViewOfArticle);
        String picked_item[] = new String[10]; //한 분야의 10개 title 추출
        final String picked_link[] = new String[10];
        for(int i=0;i<10;i++) {
            picked_item[i] = items[10*position + i];
            picked_link[i] = links[10*position + i];
        }

        ListAdapter listAdapter = new listAdapter(context,picked_item);
        listViewOfArticle.setAdapter(listAdapter);

        listViewOfArticle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 해당 기사의 링크저장, 내용(or 댓글) 가져오기.
                String link_uri = picked_link[position];
                forTest.setText(link_uri);
                //링크 열기
                Intent i = new Intent(Intent.ACTION_VIEW,Uri.parse(link_uri));
                context.startActivity(i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)); //adapter에서 activity실행하려면 addFlags해줘야함.
            }
        });
        container.addView(v);
        return v;
    }

    //page삭제
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
