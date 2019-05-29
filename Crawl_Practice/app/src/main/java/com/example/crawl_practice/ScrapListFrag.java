package com.example.crawl_practice;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class ScrapListFrag extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.slider, container, false);
        ListView listViewOfArticle = (ListView)view.findViewById(R.id.listViewOfArticle);

        //Scrap에서 ScrapPagerAdapter통해 스크랩 데이터 모두 받기
        ArrayList<String> scraps = getArguments().getStringArrayList("scraps");

        final ArrayList<Scrapped> articles = new ArrayList<>();

        for(String scrap:scraps) {
            //[분야, 날짜, 제목, 링크] 순으로 파싱해 Scrapped객체로 하나씩 저장
            String[] parsed = scrap.split("&&&");
            articles.add(new Scrapped(parsed[0],parsed[1],parsed[2],parsed[3]));
        }
        String[] picked_item = scraps.toArray(new String[0]);



        ScrapListAdapter listAdapter = new ScrapListAdapter(getActivity(),picked_item);
        listViewOfArticle.setAdapter(listAdapter);
        //롱클릭 시 원본 기사로 이동
        listViewOfArticle.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final String link_uri = articles.get(position).link; //링크uri 저장.
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("해당 기사로 이동하시겠습니까?")
                        .setTitle("News")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) { //기사로 이동
                                //링크 열기
                                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(link_uri));
                                //getActivity().startActivity(i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)); //adapter에서 activity실행하려면 addFlags해줘야함.
                                getActivity().startActivity(i);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) { //기사로 이동x
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return true; // 롱클릭 이후 숏클릭 인식 안되게
            }
        });
        //그냥 클릭 시 선택상태(배경 바꾸기)
        listViewOfArticle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //선택된 상태로 기록, 배경 바꾸기 구현@@@@@@@@@@@
            }
        });
        container.addView(view);

        return view;
    }
}