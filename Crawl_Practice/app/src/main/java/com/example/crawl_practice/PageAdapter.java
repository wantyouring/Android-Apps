package com.example.crawl_practice;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
    final Context context;
    String items[] = new String[60];
    String links[] = new String[60];
    TextView forTest;
    String link_uri;

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

        String picked_item[] = new String[10];//한 분야의 10개 title 추출
        final String picked_link[] = new String[10];

        for(int i=0;i<10;i++) {
            picked_item[i] = items[10*position + i];
            picked_link[i] = links[10*position + i];
        }

        ListAdapter listAdapter = new listAdapter(context,picked_item);
        listViewOfArticle.setAdapter(listAdapter);
        //롱클릭 시 물어보고 기사로 이동
        listViewOfArticle.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                link_uri = picked_link[position]; //링크uri 저장.
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.context);
                builder.setMessage("해당 기사로 이동하시겠습니까?")
                        .setTitle("News")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) { //종료
                                forTest.setText(link_uri);
                                //링크 열기
                                Intent i = new Intent(Intent.ACTION_VIEW,Uri.parse(link_uri));
                                MainActivity.context.startActivity(i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)); //adapter에서 activity실행하려면 addFlags해줘야함.
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) { //종료 안할거임
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return true; // 롱클릭 이후 숏클릭 인식 안되게
            }
        });
        //그냥 클릭 시 기사 요약
        listViewOfArticle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //  내용(or 댓글) 가져오기.
                link_uri = picked_link[position]; //링크uri 저장.
                forTest.setText(link_uri);
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
