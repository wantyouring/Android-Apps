package com.example.crawl_practice;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class ScrapListFrag extends Fragment {
    View view;
    ListView listViewOfArticle;
    ArrayList<String> scraps;
    ScrapListAdapter listAdapter;
    
    //Scrap액티비티에서 fragment 접근하기 위해
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Scrap.fragId = getId();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.slider, container, false);
        listViewOfArticle = (ListView)view.findViewById(R.id.listViewOfArticle);

        //Scrap에서 ScrapPagerAdapter통해 스크랩 데이터 모두 받기
        ArrayList<String> scraps = getArguments().getStringArrayList("scraps");

        final ArrayList<Scrapped> articles = new ArrayList<>();
        for(String scrap:scraps) {
            //[분야, 날짜, 제목, 링크] 순으로 파싱해 Scrapped객체로 하나씩 저장
            String[] parsed = scrap.split("&&&");
            articles.add(new Scrapped(parsed[0],parsed[1],parsed[2],parsed[3]));
        }
        String[] picked_item = scraps.toArray(new String[0]);
        listAdapter = new ScrapListAdapter(getActivity(),picked_item);
        //listAdapter = new ScrapListAdapter(getActivity(),new ArrayList<String>(scraps.keySet()));
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
                Toast.makeText(getActivity(), "기사 선택", Toast.LENGTH_SHORT).show();
            }
        });
        container.addView(view);

        return view;
    }

    public int returnCheckedItemsCount() {
        return listViewOfArticle.getCheckedItemCount();
    }
    
    public void deleteCheckedItems() {
        SparseBooleanArray checkedItems = listViewOfArticle.getCheckedItemPositions();
        int count = listAdapter.getCount();

        for(int i=count-1; i>=0; i--) {
            if(checkedItems.get(i)) {
                scraps.remove(i);
                Log.d("삭제",i+"");
                //@@@@@@@@@@@firbase 서버에서도 삭제 추가

            }
        }
        listViewOfArticle.clearChoices();
        listAdapter.notifyDataSetChanged();

    }
}